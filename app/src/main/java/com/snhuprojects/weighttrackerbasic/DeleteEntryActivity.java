package com.snhuprojects.weighttrackerbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DeleteEntryActivity extends AppCompatActivity {

    //initializations
    Button btn_datePicker, btn_enterDelete, btn_cancelDelete;
    private DatePickerDialog datePickerDialog;
    String username;

    // onCreate method called when activity is started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_entry);
        //initialize date picker to display today's date
        initDatePicker();
        btn_datePicker = findViewById(R.id.btn_datePickerDeleteEntry);
        btn_datePicker.setText(getTodaysDate());
        //get username included with intent
        Bundle extras = getIntent().getExtras();
        //The key argument here must match that used in the other activity
        username = extras.getString("key");
        // Bind buttons to variables
        btn_cancelDelete = findViewById(R.id.btn_cancelDelete);
        btn_enterDelete = findViewById(R.id.btn_enterDelete);

        // button logic to return home without making any changes. must pass pack username.
        btn_cancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(DeleteEntry.this, "No entries were deleted.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeleteEntryActivity.this, WeightDataActivity.class);
                intent.putExtra("key", username);
                startActivity(intent);
            }
        });

        // Button logic to find and delete from weight.db based on username and given date.
        btn_enterDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = btn_datePicker.getText().toString();
                DatabaseHelperUserWeight databaseHelperUserWeight = new DatabaseHelperUserWeight(DeleteEntryActivity.this);
                boolean success = databaseHelperUserWeight.deleteOne(username, date);
                if (success) {
                    Toast.makeText(DeleteEntryActivity.this, "Entry deleted.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DeleteEntryActivity.this, "No entries were deleted. Check date and try again.", Toast.LENGTH_SHORT).show();
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
        // today's date is default
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //style setting for date picker
        int style = AlertDialog.THEME_HOLO_LIGHT;

        // date picker with max date being today's date
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year,month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    // Method to return formatted integer dates into String format
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

    // method called from onClick layout setting in activity_delete_entry.xml (layout file)
    public void openDatePickerDelete(View view) {
        datePickerDialog.show();
    }
}