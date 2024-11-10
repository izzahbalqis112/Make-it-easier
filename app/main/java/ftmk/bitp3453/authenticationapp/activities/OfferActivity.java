package ftmk.bitp3453.authenticationapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.firebase.Users;

public class OfferActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnPayment;

    DatabaseReference reference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userID;

    String nameUser, phoneUser,roleUser,emailUser,passwordUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        userID = firebaseAuth.getCurrentUser().getUid();

        btnBack = findViewById(R.id.buttonBackHome);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OfferActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });


        btnPayment = findViewById(R.id.buttonPay);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(OfferActivity.this);

                            // Set the message show for the Alert time
                            builder.setMessage("You have successfully upgrade to Premium Account!");

                            // Set Alert Title
                            builder.setTitle("Notice");

                            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                            builder.setCancelable(false);

                            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                            builder.setPositiveButton("Ok", (DialogInterface.OnClickListener) (dialog, which) -> {
                                // When the user click yes button then app will close
                                Intent intent = new Intent(OfferActivity.this, HomeActivity.class);
                                startActivity(intent);
                            });

                            // Create the Alert dialog
                            AlertDialog alertDialog = builder.create();
                            // Show the Alert Dialog box
                            alertDialog.show();

            }
        });
    }

    private void retrieveUserProfile() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.activity_main, null);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();

                        View myView = inflater.inflate(R.layout.activity_main, null);

                        TextView editName = myView.findViewById(R.id.edtName);
                        TextView editEmail = myView.findViewById(R.id.edtEmail);
                        TextView editPassword = myView.findViewById(R.id.edtPassword);
                        TextView editPhone = myView.findViewById(R.id.edtPhone);
                        TextView editRole = myView.findViewById(R.id.edtRole);
                        TextView editAccountStatus = myView.findViewById(R.id.edtAccountStatus);


                        editName.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        editEmail.setText(String.valueOf(dataSnapshot.child("email").getValue()));
                        editRole.setText(String.valueOf(dataSnapshot.child("userRole").getValue()));
                        editPhone.setText(String.valueOf(dataSnapshot.child("phone").getValue()));
                        editPassword.setText(String.valueOf(dataSnapshot.child("password").getValue()));

                        nameUser = editName.getText().toString();
                        phoneUser =editPhone.getText().toString();
                        roleUser = "Premium Acccount";
                        emailUser = editEmail.getText().toString();
                        passwordUser = editPassword.getText().toString();

                        editName.setEnabled(false);
                        editEmail.setEnabled(false);
                        editPassword.setEnabled(false);
                        editPhone.setEnabled(false);
                        editRole.setEnabled(false);
                        editAccountStatus.setEnabled(false);
                    } else {
                        Toast.makeText(getApplicationContext(), "Result not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }

}