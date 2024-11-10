package ftmk.bitp3453.authenticationapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.firebase.Assessment;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentViewAssignment extends AppCompatActivity{


    ImageView btnBack;

    RecyclerView recyclerView;
    ArrayList<Assessment> assignmentList;
    FirebaseFirestore db;
    StudentAssessmentAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_assignment);

        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentViewAssignment.this,StudentSubjectMenu.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.recyclerViewWork);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        assignmentList = new ArrayList<Assessment>();
        adapter = new StudentAssessmentAdapter(StudentViewAssignment.this, assignmentList);

        db.collection("Assignment")
                .orderBy("assignment_date_created", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        assignmentList.clear();


                        for (DocumentSnapshot snapshot : task.getResult()) {

                            Assessment assessment= new Assessment(
                                    snapshot.getString("subject_name"),
                                    snapshot.getId(),
                                    snapshot.getString("assignment_name"),
                                    snapshot.getString("assignment_description"),
                                    snapshot.getString("assignment_date_created"),
                                    snapshot.getString("assignment_due"),
                                    snapshot.getString("file"),
                                    snapshot.getString("lecturer_id"));
                            assignmentList.add(assessment);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentViewAssignment.this, "Opps", Toast.LENGTH_SHORT).show();
                    }
                });


        recyclerView.setAdapter(adapter);

    }
}