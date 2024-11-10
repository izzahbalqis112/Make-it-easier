package ftmk.bitp3453.authenticationapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.firebase.Course;
import ftmk.bitp3453.authenticationapp.firebase.CourseAdapter;

public class ViewSubjectActivity extends AppCompatActivity {

    ImageView btnBack;

    RecyclerView recyclerView;
    ArrayList<Course> courseList;
    FirebaseFirestore db;
    CourseAdapter adapterCourse;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subject);


        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewSubjectActivity.this, AdminPage.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerViewCourse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        courseList = new ArrayList<Course>();
        adapterCourse = new CourseAdapter(ViewSubjectActivity.this, courseList);


        db.collection("Course").orderBy("course_name", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        courseList.clear();


                        for (DocumentSnapshot snapshot : task.getResult()) {

                            Course course = new Course(snapshot.getId(), snapshot.getString("course_id"), snapshot.getString("course_name"));
                            courseList.add(course);
                        }
                        adapterCourse.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewSubjectActivity.this, "Opps", Toast.LENGTH_SHORT).show();
                    }
                });


        recyclerView.setAdapter(adapterCourse);


    }


}

