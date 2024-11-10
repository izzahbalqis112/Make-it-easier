package ftmk.bitp3453.authenticationapp.lecturer;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
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
import ftmk.bitp3453.authenticationapp.activities.SubmitAssignment;
import ftmk.bitp3453.authenticationapp.firebase.Assessment;

public class ViewStudentAssignmentAdapter extends RecyclerView.Adapter<ViewStudentAssignmentAdapter.MyViewHolder> {

    Context context;
    ArrayList<SubmitAssignment> list;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseReference = FirebaseStorage.getInstance();
    StorageReference storageReference;


    public ViewStudentAssignmentAdapter(Context context, ArrayList<SubmitAssignment>list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.retrieved_student_submit_assignment,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubmitAssignment assignment = list.get(position);

        holder.workID.setText(assignment.getFile_id());
        holder.subject.setText(assignment.getSubject());
        holder.workName.setText(assignment.getFile_name() +".pdf");
        holder.workDate.setText(assignment.getSubmit_date());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                // Set content view to add task
                View myView = inflater.inflate(R.layout.download_assignment, null);
                builder.setView(myView);

                // Create alert dialog
                AlertDialog dialog = builder.create();

                TextView assKey = myView.findViewById(R.id.editKey);
                TextView assSubject= myView.findViewById(R.id.editSubject);
                TextView assName = myView.findViewById(R.id.editWorkName);
                TextView submitDate = myView.findViewById(R.id.editTextDate);
                TextView assFile = myView.findViewById(R.id.fileName);
                FloatingActionButton btn = myView.findViewById(R.id.floatingActionButton2);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                assKey.setText(assignment.getFile_id());
                assSubject.setText(assignment.getSubject());
                assName.setText(assignment.getFile_name() +".pdf");
                submitDate.setText(assignment.getSubmit_date());
                assFile.setText(assignment.getFile());



                Button btnDownload = myView.findViewById(R.id.buttonDownload);
                btnDownload.setOnClickListener(new View.OnClickListener() {

                    String name = assignment.getFile_name();

                    @Override
                    public void onClick(View view)
                    {
                        storageReference = firebaseReference.getReference().child("Uploads/" + name +".pdf");

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                downloadFile(context, name, ".pdf", DIRECTORY_DOWNLOADS, url);
                                dialog.dismiss();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,  e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });



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
            builder.show();
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
