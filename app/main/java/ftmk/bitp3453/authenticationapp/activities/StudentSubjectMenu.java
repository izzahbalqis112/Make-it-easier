package ftmk.bitp3453.authenticationapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.lecturer.ViewAssignment;

public class StudentSubjectMenu extends AppCompatActivity {

    BottomNavigationView navigationView;
    CardView enrollCourse,viewCourse, viewAssignment ,viewStudentAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_menu);

        navigationView = findViewById(R.id.bottomNavigation);
        navigationView.setSelectedItemId(R.id.subject);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.subject:
                        startActivity(new Intent(getApplicationContext(), StudentSubjectMenu.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.meeting:
                        startActivity(new Intent(getApplicationContext(),MeetingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        enrollCourse = findViewById(R.id.enrollCourse);
        viewCourse = findViewById(R.id.viewCourse);
        viewAssignment = findViewById(R.id.viewAssignment);
        viewStudentAssignment = findViewById(R.id.viewSubmitAssignment);

        enrollCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), StudentEnrollActivity.class));

            }
        });
        viewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getApplicationContext(),ViewEnrollSubject.class));

            }
        });
        viewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),StudentViewAssignment.class));

            }
        });
        viewStudentAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(),StudentViewSubmitAssignment.class));

            }
        });

    }
}