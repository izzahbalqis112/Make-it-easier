package ftmk.bitp3453.authenticationapp.activities;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.firebase.EnrollSubject;
import ftmk.bitp3453.authenticationapp.firebase.EnrollSubjectAdapter;

public class ViewEnrollSubject extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    String userID;
    ImageView btnBack;
    RecyclerView recyclerView;

    ArrayList<EnrollSubject> list;
    EnrollSubjectAdapter adapter;

    FirebaseFirestore db;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_enroll_subject);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEnrollSubject.this,StudentSubjectMenu.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerViewCourse);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        db = FirebaseFirestore.getInstance();
        list = new ArrayList<EnrollSubject>();
        adapter = new EnrollSubjectAdapter(ViewEnrollSubject.this,list);

        db.collection("Student_Enroll")
                .whereEqualTo("user_id",userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {

                            EnrollSubject enroll = new EnrollSubject(
                                    snapshot.getId(),
                                    snapshot.getString("user_id"),
                                    snapshot.getString("course_name"),
                                    snapshot.getString("course_id"));
                            list.add(enroll);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewEnrollSubject.this, "Opps", Toast.LENGTH_SHORT).show();
                    }
                });


        recyclerView.setAdapter(adapter);



    }
}