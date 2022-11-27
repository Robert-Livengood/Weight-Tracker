package com.snhuprojects.weighttrackerbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

//Activity for editing database entries
public class EditEntryActivity extends AppCompatActivity {

    //initializations
    Button btn_datePicker, btn_home, btn_apply;
    EditText et_weight;
    private DatePickerDialog datePickerDialog;
    String username;

    // main logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);
        // initialize datepicker
        initDatePicker();
        btn_datePicker = findViewById(R.id.btn_datePickerEditScreen);
        btn_datePicker.setText(getTodaysDate());
        // link variables to the view
        btn_home = findViewById(R.id.btn_homeEditScreen);
        et_weight = findViewById(R.id.et_weightEdit);
        btn_apply = findViewById(R.id.btn_applyEdit);

        // retrieve username from intent
        Bundle extras = getIntent().getExtras();
        //The key argument here must match that used in the other activity
        username = extras.getString("key");

        // listener and logic for home button
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send home with the username attached
                Intent intent = new Intent(EditEntryActivity.this, WeightDataActivity.class);
                intent.putExtra("key", username);
                startActivity(intent);
            }
        });

        // listener and logic for the Apply button
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gets user entered information
                int weight = Integer.parseInt(et_weight.getText().toString());
                String date = btn_datePicker.getText().toString();
                // open database instance
                DatabaseHelperUserWeight databaseHelperUserWeight = new DatabaseHelperUserWeight(EditEntryActivity.this);
                // send information to data base helper and return bool for success
                boolean success = databaseHelperUserWeight.updateUserWeight(username, date, weight);
                if (success) {
                    Toast.makeText(EditEntryActivity.this, "Entry updated.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditEntryActivity.this, "No entries were updated. Check date and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to provide today's date returned to a string
    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month +1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    // method called during onCreate to initialize the date picker
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String date = makeDateString(day, month, year);
                btn_datePicker.setText(date);
            }
        };

        // today's date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // style for the date picker
        int style = AlertDialog.THEME_HOLO_LIGHT;

        // date picker with max date set as today
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year,month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    // input raw dat information and output string
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    //Return string for the month based on month number
    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    // method called from onClick layout setting in corresponding layout file
    public void openDatePickerEdit(View view) {
        datePickerDialog.show();
    }
}