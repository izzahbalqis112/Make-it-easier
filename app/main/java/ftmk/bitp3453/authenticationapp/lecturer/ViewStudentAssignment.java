package ftmk.bitp3453.authenticationapp.lecturer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.activities.SubmitAssignment;


public class ViewStudentAssignment extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<SubmitAssignment> assignmentList;
    FirebaseFirestore db;
    ViewStudentAssignmentAdapter adapter;

    ImageView btnBack;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_submit_assignment);

        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewStudentAssignment.this, LecturerSubjectMenu.class);
                startActivity(intent);
            }
        });

        userID = firebaseAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerViewWork);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        assignmentList = new ArrayList<SubmitAssignment>();
        adapter = new ViewStudentAssignmentAdapter(ViewStudentAssignment.this, assignmentList);

        db.collection("Student_Assignment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        assignmentList.clear();


                        for (QueryDocumentSnapshot snapshot : task.getResult()) {

                            SubmitAssignment assignment= new SubmitAssignment(
                                    snapshot.getId(),
                                    snapshot.getString("file_name"),
                                    snapshot.getString("student_id"),
                                    snapshot.getString("date_submit"),
                                    snapshot.getString("file"),
                                    snapshot.getString("subject_name"));
                            assignmentList.add(assignment);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewStudentAssignment.this, "Opps", Toast.LENGTH_SHORT).show();
                    }
                });


        recyclerView.setAdapter(adapter);

    }
}