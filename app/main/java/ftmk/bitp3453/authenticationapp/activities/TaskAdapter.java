package ftmk.bitp3453.authenticationapp.activities;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;



import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ftmk.bitp3453.authenticationapp.R;
import ftmk.bitp3453.authenticationapp.firebase.Model;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Model> list;
    String key, name, desc, date, time, priorLevel,priorColor;
    int mYear, mMonth, mDay, mHour, mMinute;
    String userID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog loader;

    public TaskAdapter(Context context, ArrayList<Model> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.retrieved_layout,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyViewHolder holder, int position) {
        Model model = list.get(position);


        holder.taskID.setText(model.getTask_id());

        holder.taskName.setText(model.getTask_name());
        holder.priorLevel.setText(model.getPriority_level());
        if(model.getPriorColor().equals("A Red"))
        {
            holder.btnPrior.setBackgroundColor(Color.RED);

        }

        else if(model.getPriorColor().equals("B Yellow"))
        {
            holder.btnPrior.setBackgroundColor(Color.YELLOW);
        }

        else if(model.getPriorColor().equals("C Green"))
        {
            holder.btnPrior.setBackgroundColor(Color.GREEN);
        }

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                // Set content view to add task
                View myView = inflater.inflate(R.layout.update_data, null);
                builder.setView(myView);

                // Create alert dialog
                AlertDialog dialog = builder.create();

                TextView mID = myView.findViewById(R.id.textViewTaskID);
                final EditText mName = myView.findViewById(R.id.editTaskName);
                final EditText mDesc = myView.findViewById(R.id.editTaskDescription);
                final EditText mDate = myView.findViewById(R.id.editTaskDate);
                mDate.setEnabled(false);

                final EditText mTime = myView.findViewById(R.id.editTaskTime);
                mTime.setEnabled(false);

                Spinner mPrior = myView.findViewById(R.id.textViewPrior);


                Button btnDate = myView.findViewById(R.id.buttonDate);
                btnDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        mDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();

                    }
                });
                Button btnTime = myView.findViewById(R.id.buttonTime);
                btnTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        mTime.setText(hourOfDay + ":" + minute);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                });

                Button buttonUpdate = myView.findViewById(R.id.buttonUpdate);
                Button buttonDelete = myView.findViewById(R.id.buttonDelete);

                mID.setText(model.getTask_id());
                mName.setText(model.getTask_name());
                mDesc.setText(model.getTask_description());
                mDate.setText(model.getDate());
                mTime.setText(model.getTime());


                for (int i = 0; i < mPrior.getAdapter().getCount(); i++) {
                    if (mPrior.getAdapter().getItem(i).toString().contains(model.getPriority_level())) {
                        mPrior.setSelection(i);
                    }
                }

                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = mID.getText().toString();
                        userID = model.getUser_id();
                        name = mName.getText().toString();
                        desc = mDesc.getText().toString();
                        date = mDate.getText().toString();
                        time = mTime.getText().toString();
                        priorLevel = mPrior.getSelectedItem().toString();

                        if(priorLevel.equals("Low"))
                        {
                            priorColor = "C Green";
                        }


                        if(priorLevel.equals("Medium"))
                        {
                            priorColor = "B Yellow";
                        }

                        if(priorLevel.equals("High"))
                        {
                            priorColor = "A Red";
                        }

                        if (TextUtils.isEmpty(name)) {
                            mID.setError("Code is required.");
                            return;
                        }
                        if (TextUtils.isEmpty(desc)) {
                            mName.setError("Description is required.");
                            return;
                        }
                        if (TextUtils.isEmpty(date)) {
                            mDesc.setError("Date is required.");
                            return;
                        }
                        if (TextUtils.isEmpty(time)) {
                            mDate.setError("Time is required.");
                            return;
                        }
                        if (TextUtils.isEmpty(time)) {
                            mTime.setError("Code is required.");
                            return;
                        }
                        else
                        {
                            loader = new ProgressDialog(context);
                            loader.setMessage("Update your data...");
                            loader.setCanceledOnTouchOutside(false);
                            loader.show();

                            Map<String,Object> tasks = new HashMap<>();
                            tasks.put("task_name",name);
                            tasks.put("task_desc",desc);
                            tasks.put("task_date",date);
                            tasks.put("task_time",time);
                            tasks.put("prior_level",priorLevel);
                            tasks.put("prior_color",priorColor);
                            tasks.put("user_id",userID);

                            db.collection("Task").document(key).update(tasks)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Task has been updated succesfully", Toast.LENGTH_SHORT).show();
                                            loader.dismiss();

                                            Intent myactivity = new Intent(context.getApplicationContext(), TaskActivity.class);
                                            myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                            context.getApplicationContext().startActivity(myactivity);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                            loader.dismiss();
                                        }
                                    });
                            dialog.dismiss();
                        }
                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    String data = mID.getText().toString();
                    @Override
                    public void onClick(View view)
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete?");
                        builder.setTitle("Alert");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which) -> {
                            db.collection("Task").document(data).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Task has been deleted succesfully", Toast.LENGTH_SHORT).show();

                                            Intent myactivity = new Intent(context.getApplicationContext(), TaskActivity.class);
                                            myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                            context.getApplicationContext().startActivity(myactivity);


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();


                                        }
                                    });
                        });

                        builder.setNegativeButton("No",(DialogInterface.OnClickListener) (dialog,which) -> {
                            dialog.cancel();
                        });
                        builder.show();


                    }
                });
                dialog.show();
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView taskName, priorLevel;
        TextView taskID;
        Button btnPrior;

        View mView;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;

            taskName = itemView.findViewById(R.id.textViewTaskName);
            priorLevel = itemView.findViewById(R.id.textViewPrior);
            taskID = itemView.findViewById(R.id.textViewTaskId);
            btnPrior = itemView.findViewById(R.id.buttonPriorColor);


        }
    }

}
