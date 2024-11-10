package ftmk.bitp3453.authenticationapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;
import java.util.regex.Pattern;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.firebase.Users;


public class RegisterActivity extends AppCompatActivity {

    EditText edtName,edtEmail, edtPhone,edtUsername,edtPassword,edtConfirmPassword;
    TextView tvLogin;
    Button btnSignUp;
    ProgressBar progressBar;
    Spinner edtUserRole;
    DatePickerDialog datePicker;

    FirebaseDatabase db;
    DatabaseReference reference;
    final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    String name, email, phone, userRole, username, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Edit text
        edtName = findViewById(R.id.editName);
        edtEmail = findViewById(R.id.editEmail);
        edtPhone = findViewById(R.id.editPhone);
        edtPassword = findViewById(R.id.editPassword);
        edtConfirmPassword = findViewById(R.id.editConfirmPassword);

        //Spinner
        edtUserRole = findViewById(R.id.editUserRole);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.state_arrays,R.layout.spinner_list);
        edtUserRole.setAdapter(adapter);

        //Text view
        tvLogin = findViewById(R.id.tvGoToLogin);

        //Button
        btnSignUp = findViewById(R.id.buttonSignUp);

        //Progress bar
        progressBar = findViewById(R.id.progressBar);

        tvLogin.setOnClickListener(v->redirectLoginPage());

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                phone = edtPhone.getText().toString();

                userRole = edtUserRole.getSelectedItem().toString();
                password = edtPassword.getText().toString();
                confirmPassword = edtConfirmPassword.getText().toString();

                if(name.matches("")&& email.matches("") && phone.matches("")  && password.matches("") && confirmPassword.matches(""))
                {
                    Toast.makeText(RegisterActivity.this,"Every fields required to fill",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(name.matches(""))
                {
                    Toast.makeText(RegisterActivity.this,"Name required to fill",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(email.matches(""))
                {
                    Toast.makeText(RegisterActivity.this,"Email required to fill",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(phone.matches(""))
                {
                    Toast.makeText(RegisterActivity.this,"Phone required to fill",Toast.LENGTH_SHORT).show();
                    return;
                }


                else if(password.matches(""))
                {
                    Toast.makeText(RegisterActivity.this,"Password required to fill",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(confirmPassword.matches(""))
                {
                    Toast.makeText(RegisterActivity.this,"Confirm password required to fill",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(!password.equals(confirmPassword))
                {
                    Toast.makeText(RegisterActivity.this,"Password and confirm password not matching.",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (!PASSWORD_PATTERN.matcher(password).matches() && password.length() < 8) {
                    Toast.makeText(RegisterActivity.this,"Password is too weak. \nAt least 8 characters.",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(RegisterActivity.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
                    return;
                }


                else{
                    changesInProgress(true);
                    fnAddToREST();
                }
            }
        });


    }


    //Insert Data into Database
    private void fnAddToREST()
    {
        //Get data from sign up page
        name = edtName.getText().toString();
        email = edtEmail.getText().toString();
        phone = edtPhone.getText().toString();
        userRole = edtUserRole.getSelectedItem().toString();
        password = edtPassword.getText().toString();

        createAccountInFirebase(name,email,phone,userRole,password);
    }

    //Create data into firebase
    private void createAccountInFirebase(String name, String email, String phone, String userRole,String password) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                changesInProgress(false);
                if(task.isSuccessful())
                {
                    String userId = firebaseAuth.getCurrentUser().getUid();

                    Users users = new Users(userId,password,name,email,userRole,phone);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Users");
                    reference.child(userId).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            changesInProgress(true);
                            Toast.makeText(RegisterActivity.this,"Successfully added",Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                }
                else
                {
                    Toast.makeText(RegisterActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Redirect to login page
    private void redirectLoginPage()
    {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    // Visibility of progress bar
    private void changesInProgress(boolean inProgress)
    {
        if(inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.VISIBLE);
        }
    }




}