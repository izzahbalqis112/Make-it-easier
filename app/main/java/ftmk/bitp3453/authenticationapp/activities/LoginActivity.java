package ftmk.bitp3453.authenticationapp.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import ftmk.bitp3453.authenticationapp.R;

import ftmk.bitp3453.authenticationapp.admin.AdminPage;
import ftmk.bitp3453.authenticationapp.lecturer.LecturerMainActivity;
import ftmk.bitp3453.authenticationapp.utilities.PreferenceManager;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername,edtPassword;
    TextView tvSignUp,tvForgotPassword;
    Button btnLogin;
    ProgressBar progressBar;
    PreferenceManager preferenceManager;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore;

    FirebaseDatabase db;
    DatabaseReference reference;
    String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceManager = new PreferenceManager(getApplicationContext());

        edtUsername = findViewById(R.id.editUsername);
        edtPassword = findViewById(R.id.editPassword);

        tvSignUp = findViewById(R.id.tvGoToRegister);
        tvForgotPassword = findViewById(R.id.textViewForgetPassword);

        btnLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        tvSignUp.setOnClickListener(v->redirectToRegister());

    }

    // To register page
    public void redirectToRegister()
    {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    // To login function
    public void loginUser()
    {
        String email, password;

        email = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        boolean isValidate = validateData(email,password);
        if(!isValidate)
        {
            return;
        }

        loginAccountInFirebase(email,password);
    }

    private void loginAccountInFirebase(String email, String password)
    {
        fStore = FirebaseFirestore.getInstance();

        changesInProgress(true);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changesInProgress(false);

                if(task.isSuccessful())
                {
                    if(firebaseAuth.getCurrentUser().isEmailVerified())
                    {
                        String userID = firebaseAuth.getCurrentUser().getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    if(task.getResult().exists())
                                    {
                                        DataSnapshot dataSnapshot = task.getResult();
                                        role = String.valueOf(dataSnapshot.child("userRole").getValue());
                                        Log.d(TAG, "Value is: " + role);
                                    }

                                    else
                                    {
                                        Toast.makeText(LoginActivity.this,"Result not exist",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if(role!=null && role.equals("Lecturer"))
                                {
                                    startActivity(new Intent(LoginActivity.this, LecturerMainActivity.class));
                                }

                                if(role!=null && role.equals("Student"))
                                {
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));

                                }

                                if(role!=null && role.equals("Admin"))
                                {
                                    startActivity(new Intent(LoginActivity.this, AdminPage.class));

                                }
                            }

                        });

                    }

                    else if(!firebaseAuth.getCurrentUser().isEmailVerified())
                    {
                        Toast.makeText(LoginActivity.this,"Email not verified.Please check your inbox.",Toast.LENGTH_SHORT).show();

                    }

                    else
                    {
                        Toast.makeText(LoginActivity.this,"User not found.",Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    Toast.makeText(LoginActivity.this,"Invalid username and password",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void changesInProgress(boolean inProgress)
    {
        if(inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password)
    {

        if(TextUtils.isEmpty(email))
        {
            edtUsername.setError("Email is required");
            return false;
        }

        if(TextUtils.isEmpty(password))
        {
            edtPassword.setError("Password is required");
            return false;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edtUsername.setError("Email is invalid");
            return false;
        }

        if(password.length()<=6)
        {
            edtPassword.setError("Password length is invalid");
            return false;
        }

        return true;
    }

    public void resetPassword(View vw)
    {
        EditText resetEmail = new EditText(vw.getContext());

        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(vw.getContext());
        passwordResetDialog.setTitle("Reset Password?");
        passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
        passwordResetDialog.setView(resetEmail);
        passwordResetDialog.setPositiveButton("Yes" , (dialog, which) -> {
            String mail = resetEmail.getText().toString();
            firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(unused -> Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show()

            ).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error reset link" + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
                passwordResetDialog.setNegativeButton("No", (dialog, which) -> {

                });

                passwordResetDialog.create().show();

            }
        }