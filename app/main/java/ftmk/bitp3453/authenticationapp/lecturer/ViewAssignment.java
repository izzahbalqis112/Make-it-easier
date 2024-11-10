package ftmk.bitp3453.authenticationapp.lecturer;

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
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewAssignment extends AppCompatActivity{

    FloatingActionButton addAssignment;
    BottomNavigationView navigationView;

    RecyclerView recyclerView;
    ArrayList<Assessment> assignmentList;
    FirebaseFirestore db;
    AssignmentAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);

        addAssignment = findViewById(R.id.buttonAddWork);
        addAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewAssignment.this, AddAssignment.class));
            }
        });

        navigationView = findViewById(R.id.bottomNavigation);
        navigationView.setSelectedItemId(R.id.subject);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.subject:
                        startActivity(new Intent(getApplicationContext(), LecturerSubjectMenu.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), LecturerMainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerViewWork);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        assignmentList = new ArrayList<Assessment>();
        adapter = new AssignmentAdapter(ViewAssignment.this, assignmentList);

        db.collection("Assignment").orderBy("assignment_date_created", Query.Direction.ASCENDING).get()
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
                        Toast.makeText(ViewAssignment.this, "Opps", Toast.LENGTH_SHORT).show();
                    }
                });


        recyclerView.setAdapter(adapter);

    }
}