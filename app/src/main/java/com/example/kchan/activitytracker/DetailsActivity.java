package com.example.kchan.activitytracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;

import com.example.kchan.activitytracker.Database.UserDatabase;
import com.example.kchan.activitytracker.Database.UserInfo;
import com.example.kchan.activitytracker.Singleton.User;

public class DetailsActivity extends AppCompatActivity {

    EditText dateEdit;
    EditText weightEdit;
    EditText heightEdit;
    TextView welcomeText;
    User user;

    private String dateString;
    private String weightString;
    private String heightString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        dateEdit = (EditText) findViewById(R.id.date_edit);
        weightEdit = (EditText) findViewById(R.id.weight_edit);
        heightEdit = (EditText) findViewById(R.id.height_edit);
        welcomeText = (TextView) findViewById(R.id.welcome_text);

        dateEdit.setInputType(InputType.TYPE_NULL);
        user = User.getInstance();
        String name = user.getAccount().getDisplayName();
        String temp = "Welcome " + name;
        welcomeText.setText(temp);


    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.save_button:

                if(TextUtils.isEmpty(dateEdit.getText()) && TextUtils.isEmpty(heightEdit.getText()) && TextUtils.isEmpty(weightEdit.getText()))
                {
                    Toast.makeText(this,"Enter all details",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(dateEdit.getText()))
                {
                    Toast.makeText(this, "Enter date", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(heightEdit.getText()))
                {
                    Toast.makeText(this,"Enter height", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(weightEdit.getText()))
                {
                    Toast.makeText(this,"Enter weight", Toast.LENGTH_SHORT).show();
                }
                else {

                    UserInfo userInfo = new UserInfo();

                    userInfo.setEmail(user.getAccount().getEmail());
                    userInfo.setName(user.getAccount().getDisplayName());
                    userInfo.setDob(dateEdit.getText().toString());
                    userInfo.setHeight(heightEdit.getText().toString());
                    userInfo.setWeight(weightEdit.getText().toString());

                    UserDatabase userDB = new UserDatabase();
                    userDB.storeUser(user, userInfo);

                    Intent intent = new Intent(this, MapsActivity.class);
                    this.startActivity(intent);
                }

            /*case R.id.date_edit:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog.OnDateSetListener mDateSetListener;

                mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;

                        String date = month + "/" + day + "/" + year;
                        dateEdit.setText(date);
                    }
                };

                DatePickerDialog dialog = new DatePickerDialog(
                        this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                try {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                catch (Exception e)
                {
                    Log.e("Inside Details Activity","Caught Exception" + e);
                }
                dialog.show();*/

        }
    }

    public void onClickDate(View view)
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener mDateSetListener;

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //datePicker.setMaxDate(System.currentTimeMillis() - 1000);
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                dateEdit.setText(date);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                DetailsActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        catch (Exception e)
        {
            Log.e("Inside Details Activity","Caught Exception" + e);
        }
        dialog.show();
    }

    @Override
    public void onBackPressed()
    {

    }

}
