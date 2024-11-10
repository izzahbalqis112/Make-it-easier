package ftmk.bitp3453.authenticationapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giphy.sdk.core.models.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.lecturer.AddAssignment;
import ftmk.bitp3453.authenticationapp.lecturer.ViewAssignment;

public class StudentSubmitAssignment extends AppCompatActivity {

    EditText subject,workName;
    TextView fileName;
    Button upload, submit;

    ImageView btnBack;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_submit_assignment);

        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentSubmitAssignment.this, StudentSubjectMenu.class);
                startActivity(intent);
            }
        });

        userID = firebaseAuth.getCurrentUser().getUid();

        subject = findViewById(R.id.editSubject);
        subject.setEnabled(false);
        workName = findViewById(R.id.editWorkName);
        fileName = findViewById(R.id.fileName);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("subject");
            subject.setText(value);
        }

        upload = findViewById(R.id.buttonUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });

        submit = findViewById(R.id.buttonSubmit);
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
            submit.setOnClickListener(new View.OnClickListener() {
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

        StorageReference reference = storageReference.child("Uploads/" + workName.getText().toString() + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        // Get file url
                        Uri url = uriTask.getResult();

                        Toast.makeText(StudentSubmitAssignment.this,"File Uploaded!!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        //Data to store
                        String sub = subject.getText().toString();
                        String fileName = workName.getText().toString();
                        String fileDateSubmit = DateFormat.getDateInstance().format(new Date());
                        String file = url.toString();
                        String student_id = userID;



                        addAssignment(sub,fileName,fileDateSubmit,file,student_id);



                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress =(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("File Uploaded..." + (int)progress +"%");

                    }
                });


    }

    public void addAssignment(String sub,String fileName,String fileDateSubmit,String file,String student_id)
    {

        Map<String,Object> assignment = new HashMap<>();
        assignment.put("subject_name",sub);
        assignment.put("file_name",fileName);
        assignment.put("date_submit",fileDateSubmit);
        assignment.put("file",file);
        assignment.put("student_id",student_id);


        db.collection("Student_Assignment")
                .add(assignment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(StudentSubmitAssignment.this,"Assignment successfully added",Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), StudentViewAssignment.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentSubmitAssignment.this, "Error adding document",Toast.LENGTH_SHORT).show();

                    }
                });
    }


}