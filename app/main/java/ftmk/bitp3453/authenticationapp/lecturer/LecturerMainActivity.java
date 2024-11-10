package ftmk.bitp3453.authenticationapp.lecturer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.activities.LoginActivity;
import ftmk.bitp3453.authenticationapp.firebase.Users;

public class LecturerMainActivity extends AppCompatActivity {
    EditText editName, editEmail, editPassword, editPhone, editBirthDate, editRole, editAccountStatus;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    LinearLayout header;

    DatabaseReference reference;

    Button btnUpdate, btnCancel;
    BottomNavigationView navigationView;
    TextView logout;

    ProgressDialog loader;

    String key = "";
    String nameUser, phoneUser, birthDateUser,roleUser,emailUser,passwordUser;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_main);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //Get current userID
        userId = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        loader = new ProgressDialog(this);

        header = findViewById(R.id.layoutHeader);
        editName = findViewById(R.id.edtName);
        editEmail = findViewById(R.id.edtEmail);
        editPassword = findViewById(R.id.edtPassword);
        editPhone = findViewById(R.id.edtPhone);
        editRole = findViewById(R.id.edtRole);
        editAccountStatus = findViewById(R.id.edtAccountStatus);
        logout = findViewById(R.id.textSignOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editName.isSelected()) {
                    header.setVisibility(View.INVISIBLE);
                } else {
                    header.setVisibility(View.VISIBLE);
                }
            }
        });


        btnUpdate = findViewById(R.id.buttonUpdateProfile);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        navigationView = findViewById(R.id.bottomNavigation);
        navigationView.setSelectedItemId(R.id.profile);


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

        retrieveUserProfile();

    }

    private void retrieveUserProfile() {

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        editName.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        editEmail.setText(String.valueOf(dataSnapshot.child("email").getValue()));
                        editRole.setText(String.valueOf(dataSnapshot.child("userRole").getValue()));
                        editPhone.setText(String.valueOf(dataSnapshot.child("phone").getValue()));
                        editPassword.setText(String.valueOf(dataSnapshot.child("password").getValue()));

                        editName.setEnabled(false);
                        editEmail.setEnabled(false);
                        editPassword.setEnabled(false);
                        editPhone.setEnabled(false);
                        editRole.setEnabled(false);
                        editAccountStatus.setEnabled(false);
                    } else {
                        Toast.makeText(LecturerMainActivity.this, "Result not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void updateProfile()
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.update_profile, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText name = view.findViewById(R.id.editName);
        EditText phone = view.findViewById(R.id.editPhone);
        //Spinner mPrior = view.findViewById(R.id.textViewPrior);

        name.setText(editName.getText().toString());
        name.setSelection(name.length());


        phone.setText(editPhone.getText().toString());
        phone.setSelection(phone.length());



        Button update = view.findViewById(R.id.buttonUpdate);
        Button cancel = view.findViewById(R.id.buttonCancel);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameUser = name.getText().toString();
                phoneUser = phone.getText().toString();
                roleUser = editRole.getText().toString();
                emailUser = editEmail.getText().toString();
                passwordUser = editPassword.getText().toString();

                Users users = new Users(userId,passwordUser,nameUser,emailUser,roleUser,phoneUser);

                reference.child(userId).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LecturerMainActivity.this, "Data has been updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(LecturerMainActivity.this, "Update failed " + error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.dismiss();
            }
        });

        // When cancel button click
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }



    private void onCancel()
    {
        btnCancel.setVisibility(View.INVISIBLE);
        editName.setEnabled(false);
        editPhone.setEnabled(false);
        editBirthDate.setEnabled(false);

    }


}

