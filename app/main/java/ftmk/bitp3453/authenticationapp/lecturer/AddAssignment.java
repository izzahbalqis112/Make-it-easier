package ftmk.bitp3453.authenticationapp.lecturer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ftmk.bitp3453.authenticationapp.R;

public class AddAssignment extends AppCompatActivity {

    // To get current user id
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;

    //Database, Storage
    StorageReference storageReference;
    FirebaseFirestore db;
    // name, due date, description
    EditText assignmentName,dueDate,assignmentDescription;

    //file
    TextView fileName;

    //Upload,submit,button date
    Button buttonUpload,buttonSubmit,btnDate;
    ImageView btnBack;

    //year, month, day
    int mYear,mDay;
    int mMonth;

    // arraylist, adapter, querysnapshot, spinner
    ArrayList<String> subjectList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    QuerySnapshot querySnapshot;
    Spinner spinnerSubject;

    //Progress dialog for loading
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddAssignment.this, ViewAssignment.class);
                startActivity(i);
            }
        });


        dueDate = findViewById(R.id.editTextDate);
        btnDate = findViewById(R.id.buttonWorkDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth =c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAssignment.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dueDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        assignmentName = findViewById(R.id.editWorkName);
        assignmentDescription = findViewById(R.id.editTextDescription);
        fileName = findViewById(R.id.fileName);

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonUpload = findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });

        loader = new ProgressDialog(AddAssignment.this);
        loader.setTitle("Loading");
        loader.setMessage("Please wait...");


        spinnerSubject = findViewById(R.id.spinnerSubject);
        db = FirebaseFirestore.getInstance();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,subjectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapter);
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),adapter.getItem(i),Toast.LENGTH_SHORT).show();
                Log.e("Course",querySnapshot.getDocuments().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getSubject();


    }

    // get subject from cloud
    private void getSubject()
    {
        db.collection("Course")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        loader.dismiss();
                        querySnapshot = queryDocumentSnapshots;
                        if(queryDocumentSnapshots.size()>0)
                        {
                            subjectList.clear();
                            for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                            {
                                subjectList.add(documentSnapshot.getString("course_name"));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loader.dismiss();
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void selectPDF() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            fileName.setText(data.getDataString());
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    uploadPDFToFirebase(data.getData());

                }
            });
        }
    }

    private void uploadPDFToFirebase(Uri data)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("Uploads/" + assignmentName.getText().toString() + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            // Get file url
                            Uri url = uriTask.getResult();

                            Toast.makeText(AddAssignment.this,"File Uploaded!!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            //Data to store
                            String subject = spinnerSubject.getSelectedItem().toString();
                            String fileName = assignmentName.getText().toString();
                            String fileDesc = assignmentDescription.getText().toString();
                            String fileDueDate = dueDate.getText().toString();
                            String fileDateCreated = DateFormat.getDateInstance().format(new Date());
                            String file = url.toString();
                            String lecturer_id = userID;



                            addAssignment(subject,fileName,fileDesc,fileDueDate,fileDateCreated,file,lecturer_id);



                        }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress =(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("File Uploaded..." + (int)progress +"%");

                    }
                });


    }

    public void addAssignment(String subject,String fileName,String fileDesc,String fileDueDate,String fileDateCreated,String file,String lecturer_id)
    {

        Map<String,Object> assignment = new HashMap<>();
        assignment.put("subject_name",subject);
        assignment.put("assignment_name",fileName);
        assignment.put("assignment_description",fileDesc);
        assignment.put("assignment_due",fileDueDate);
        assignment.put("assignment_date_created",fileDateCreated);
        assignment.put("file",file);
        assignment.put("lecturer_id",lecturer_id);

        db.collection("Assignment")
                .add(assignment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddAssignment.this,"Assignment successfully added",Toast.LENGTH_SHORT).show();
                        loader.dismiss();
                        startActivity(new Intent(getApplicationContext(),ViewAssignment.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddAssignment.this, "Error adding document",Toast.LENGTH_SHORT).show();
                        loader.dismiss();
                    }
                });
    }

}