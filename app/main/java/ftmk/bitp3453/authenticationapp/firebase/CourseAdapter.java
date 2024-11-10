package ftmk.bitp3453.authenticationapp.firebase;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.admin.ViewSubjectActivity;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Course> list;
    FirebaseFirestore  db = FirebaseFirestore.getInstance();

    ProgressDialog loader;

    String subjectCode,subjectName,key;

    public CourseAdapter(Context context, ArrayList<Course> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.retrieved_subject,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Course course = list.get(position);

        holder.id.setText(course.getKey());
        holder.courseCode.setText(course.getCourse_code());
        holder.courseName.setText(course.getCourse_name());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                // Set content view to add task
                View myView = inflater.inflate(R.layout.update_course, null);
                builder.setView(myView);

                // Create alert dialog
                AlertDialog dialog = builder.create();

                // Binding the element from retrieve course
                final EditText editTextCode = myView.findViewById(R.id.editCode);
                EditText editTextName = myView.findViewById(R.id.editCName);
                TextView editKey = myView.findViewById(R.id.editKey);
                Button buttonUpdate = myView.findViewById(R.id.buttonUpdateCourse);
                Button buttonDelete = myView.findViewById(R.id.buttonDeleteCourse);

                editTextCode.setText(course.getCourse_code());
                editTextName.setText(course.getCourse_name());
                editKey.setText(course.getKey());

                buttonUpdate.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        subjectCode = editTextCode.getText().toString();
                        subjectName = editTextName.getText().toString();
                        key = editKey.getText().toString();

                        if (TextUtils.isEmpty(subjectCode)) {
                            editTextCode.setError("Code is required.");
                            return;
                        } else if (TextUtils.isEmpty(subjectName)) {
                            editTextName.setError("Name is required");
                        } else {
                            loader = new ProgressDialog(context);
                            loader.setMessage("Update your data...");
                            loader.setCanceledOnTouchOutside(false);
                            loader.show();

                            Map<String, Object> course = new HashMap<>();
                            course.put("course_id", subjectCode);
                            course.put("course_name", subjectName);

                            db.collection("Course").document(key).update(course)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Task has been inserted succesfully", Toast.LENGTH_SHORT).show();
                                            loader.dismiss();

                                            Intent myactivity = new Intent(context.getApplicationContext(), ViewSubjectActivity.class);
                                            myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                            context.getApplicationContext().startActivity(myactivity);


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                            loader.dismiss();

                                        }
                                    });
                            dialog.dismiss();
                        }
                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {

                    String data = editKey.getText().toString();
                    @Override
                    public void onClick(View view)
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete?");
                        builder.setTitle("Alert");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog,which) -> {
                            db.collection("Course").document(data).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Task has been deleted succesfully", Toast.LENGTH_SHORT).show();

                                        Intent myactivity = new Intent(context.getApplicationContext(), ViewSubjectActivity.class);
                                        myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                        context.getApplicationContext().startActivity(myactivity);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();


                                    }
                                });


                        });

                        builder.setNegativeButton("No",(DialogInterface.OnClickListener) (dialog,which) -> {
                            dialog.cancel();
                        });
                        builder.show();




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


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView courseName, courseCode;
        TextView id;
        ImageView btnEdit, btnDelete;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            cardView = itemView.findViewById(R.id.cardView);
            courseCode = itemView.findViewById(R.id.textViewCourseCode);
            courseName = itemView.findViewById(R.id.textViewCourseName);
            id = itemView.findViewById(R.id.textViewCourseId);
        }
    }
}
