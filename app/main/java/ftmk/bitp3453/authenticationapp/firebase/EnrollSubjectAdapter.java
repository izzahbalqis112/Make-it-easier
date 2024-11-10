package ftmk.bitp3453.authenticationapp.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import ftmk.bitp3453.authenticationapp.R;

public class EnrollSubjectAdapter extends RecyclerView.Adapter<EnrollSubjectAdapter.MyViewHolder>
{
    Context context;
    ArrayList<EnrollSubject> list;
    FirebaseFirestore  db = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    ProgressDialog loader;

    String subjectCode,subjectName,key;

    public EnrollSubjectAdapter(Context context, ArrayList<EnrollSubject> list) {
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
        EnrollSubject course = list.get(position);

        holder.courseCode.setText(course.getSubjectCode());
        holder.courseName.setText(course.getSubjectName());
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
