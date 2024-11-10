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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.admin.AddSubjectActivity;
import ftmk.bitp3453.authenticationapp.admin.ViewSubjectActivity;

public class EnrollCourseAdapter extends RecyclerView.Adapter<EnrollCourseAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Course> list;
    FirebaseFirestore  db = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    ProgressDialog loader;

    String subjectCode,subjectName,key;

    public EnrollCourseAdapter(Context context, ArrayList<Course> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.enroll_subject,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Course course = list.get(position);

        holder.id.setText(course.getKey());
        holder.courseCode.setText(course.getCourse_code());
        holder.courseName.setText(course.getCourse_name());

        holder.enroll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String userID = firebaseAuth.getCurrentUser().getUid();
                subjectCode = holder.courseCode.getText().toString();
                subjectName = holder.courseName.getText().toString();

                Map<String,Object> course = new HashMap<>();
                course.put("course_id",subjectCode);
                course.put("course_name",subjectName);
                course.put("user_id",userID);


                db.collection("Student_Enroll")
                        .add(course)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(context.getApplicationContext(), "Course successfully enroll",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context.getApplicationContext(), "Error adding document",Toast.LENGTH_SHORT).show();

                            }
                        });

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
        Button enroll;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            cardView = itemView.findViewById(R.id.cardView);
            courseCode = itemView.findViewById(R.id.textViewCourseCode);
            courseName = itemView.findViewById(R.id.textViewCourseName);
            id = itemView.findViewById(R.id.textViewCourseId);
            enroll = itemView.findViewById(R.id.buttonEnroll);
        }
    }
}
