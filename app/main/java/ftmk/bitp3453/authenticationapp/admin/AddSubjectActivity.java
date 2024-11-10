package ftmk.bitp3453.authenticationapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ftmk.bitp3453.authenticationapp.R;

public class AddSubjectActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText edtCourse,edtCourseCode;
    Button btnAdd;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSubjectActivity.this, AdminPage.class);
                startActivity(intent);
            }
        });


        edtCourse = findViewById(R.id.editCourseName);
        edtCourseCode = findViewById(R.id.editCourseCode);
        btnAdd = findViewById(R.id.buttonAddCourse);





        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value = edtCourse.getText().toString();
                String code = edtCourseCode.getText().toString();

                if(TextUtils.isEmpty(value) && TextUtils.isEmpty(code))
                {
                    edtCourse.setError("This field is required");
                    edtCourseCode.setError("This field is required");
                    return;
                }

                if(TextUtils.isEmpty(value))
                {
                    edtCourse.setError("This field is required");
                    return;
                }

                if(TextUtils.isEmpty(code))
                {
                    edtCourseCode.setError("This field is required");
                    return;
                }

                else {
                    Map<String,Object> course = new HashMap<>();
                    course.put("course_id",code);
                    course.put("course_name",value);



                    db.collection("Course")
                            .add(course)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                            {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(AddSubjectActivity.this,"Course successfully added",Toast.LENGTH_SHORT).show();

                                    edtCourse.setText("");
                                    edtCourseCode.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddSubjectActivity.this, "Error adding document",Toast.LENGTH_SHORT).show();

                                }
                            });

                }







            }
        });


    }


}