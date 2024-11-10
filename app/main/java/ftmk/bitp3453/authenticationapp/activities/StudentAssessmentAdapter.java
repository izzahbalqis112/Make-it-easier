package ftmk.bitp3453.authenticationapp.activities;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.firebase.Assessment;

public class StudentAssessmentAdapter extends RecyclerView.Adapter<StudentAssessmentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Assessment> list;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseReference = FirebaseStorage.getInstance();
    StorageReference storageReference;


    public StudentAssessmentAdapter(Context context, ArrayList<Assessment>list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.retrieved_assignment,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Assessment assessment = list.get(position);

        holder.workID.setText(assessment.getFile_id());
        holder.subject.setText(assessment.getSubject());
        holder.workName.setText(assessment.getFile_name());
        holder.workDate.setText("Due date: "+assessment.getDue_date());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                // Set content view to add task
                View myView = inflater.inflate(R.layout.update_assignment, null);
                builder.setView(myView);

                // Create alert dialog
                AlertDialog dialog = builder.create();

                TextView tvKey = myView.findViewById(R.id.textViewWorkId);
                TextView tvSubject= myView.findViewById(R.id.editSubject);
                TextView tvName = myView.findViewById(R.id.editWorkName);
                TextView tvDesc = myView.findViewById(R.id.editTextDescription);
                TextView tvDate = myView.findViewById(R.id.editTextDate);
                TextView tvFile = myView.findViewById(R.id.fileName);
                FloatingActionButton btn = myView.findViewById(R.id.floatingActionButton2);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                tvKey.setText(assessment.getFile_id());
                tvSubject.setText(assessment.getSubject());
                tvName.setText(assessment.getFile_name());
                tvDesc.setText(assessment.getFile_description());
                tvDate.setText(assessment.getDue_date());
                tvFile.setText(assessment.getUrl());


                Button btnDownload = myView.findViewById(R.id.buttonDownload);
                btnDownload.setOnClickListener(new View.OnClickListener() {

                    String name = tvName.getText().toString();

                    @Override
                    public void onClick(View view)
                    {
                        storageReference = firebaseReference.getReference().child("Uploads/" + name +".pdf");

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                downloadFile(context, name, ".pdf", DIRECTORY_DOWNLOADS, url);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "something went wrong " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();


                    }

                    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

                        DownloadManager downloadmanager = (DownloadManager) context.
                                getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(url);
                        DownloadManager.Request request = new DownloadManager.Request(uri);

                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

                        downloadmanager.enqueue(request);
                    }

                });


                Button btnDelete = myView.findViewById(R.id.buttonDelete);
                btnDelete.setText("Submit");
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Set content view to add task
                        View myView = inflater.inflate(R.layout.activity_student_submit_assignment, null);


                        Intent intent = new Intent(context.getApplicationContext(),StudentSubmitAssignment.class);
                        intent.putExtra("subject",assessment.getSubject());
                        context.startActivity(intent);


                    }


                });

                dialog.show();




            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        View mView;
        TextView subject,workName,workDate;
        TextView workID;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;

            subject = itemView.findViewById(R.id.textViewSubject);
            workName = itemView.findViewById(R.id.textViewWorkName);
            workDate = itemView.findViewById(R.id.textViewWorkDate);

            workID = itemView.findViewById(R.id.textViewWorkId);

        }

    }


}