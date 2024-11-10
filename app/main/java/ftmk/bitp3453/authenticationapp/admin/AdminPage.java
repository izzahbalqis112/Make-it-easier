package ftmk.bitp3453.authenticationapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.activities.LoginActivity;

public class AdminPage extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userId;
    TextView logout;
    BottomNavigationView navigationView;
    CardView addCourse,viewCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();


        logout = findViewById(R.id.textSignOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        navigationView = findViewById(R.id.bottomNavigation);
        navigationView.setSelectedItemId(R.id.profile);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), AdminPage.class));
                        overridePendingTransition(0, 0);
                        return true;


                }
                return false;
            }
        });

        addCourse = findViewById(R.id.addCourse);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPage.this, AddSubjectActivity.class));
            }
        });

        viewCourse = findViewById(R.id.viewCourse);
        viewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPage.this, ViewSubjectActivity.class));
            }
        });

    }

}