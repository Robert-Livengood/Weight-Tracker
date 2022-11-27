package com.snhuprojects.weighttrackerbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

//Activity for adding entries to the weight database
public class AddEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // initializations
    EditText et_weight;
    Button btn_enter;
    Button btn_cancel;
    String username = "";
    Button btn_datePicker;
    EditText et_workoutCategory;
    private DatePickerDialog datePickerDialog;
    Spinner sp_category;

    //TODO
    // enforce unique dates for each user.

    // main logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        // initialize date picker
        initDatePicker();
        btn_datePicker = findViewById(R.id.btn_datePicker);
        btn_datePicker.setText(getTodaysDate());
        // assign buttons to corresponding view variables
        et_weight = findViewById(R.id.et_weight);
        btn_enter = findViewById(R.id.btn_enter);
        btn_cancel = findViewById(R.id.btn_cancel);
        sp_category = findViewById(R.id.sp_category);

        //get username from intent
        Bundle extras = getIntent().getExtras();
        //The key argument here must match that used in the other activity
        username = extras.getString("key");

        // logic for enter button
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // logic to get data from edit texts and add to weight.db
                WeightModel weightModel;

                // Confirm that a date and/or workout category was entered and convert to strings
                String weightEntered = et_weight.getText().toString();
                String category = sp_category.getSelectedItem().toString();
                // if no weight entered -> toast error message
                if (weightEntered.matches("")) {
                    Toast.makeText(AddEntryActivity.this, "Error: No weight entered.", Toast.LENGTH_SHORT).show();
                }
                // create WeightModel object with the given information
                else if (!weightEntered.matches("") && !category.matches("")) {
                    try {
                        weightModel = new WeightModel(-1, username,btn_datePicker.getText().toString(), Integer.parseInt(et_weight.getText().toString()), category);
                    }
                    catch (Exception e) {
                        Toast.makeText(AddEntryActivity.this, "Error creating entry.", Toast.LENGTH_SHORT).show();
                        weightModel = new WeightModel(-1, "error", "error", 0, "error");
                    }

                    // update weightModel with delta if goal weight is set
                    DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(AddEntryActivity.this);
                    int goalWeight = databaseHelperUser.getUserGoalWeight(username);
                    if (goalWeight !=0) {
                        int delta = goalWeight - Integer.parseInt(et_weight.getText().toString());
                        weightModel.setDelta(delta);
                    }

                    // get database instance
                    DatabaseHelperUserWeight databaseHelperUserWeight = new DatabaseHelperUserWeight(AddEntryActivity.this);
                    //return true if there is already a weight entry for the given date
                    boolean dateUsed = databaseHelperUserWeight.findDate(username, btn_datePicker.getText().toString());

                    // if date previously used for the user then do not add, else add
                    if (dateUsed) {
                        Toast.makeText(AddEntryActivity.this, "User already has a weight saved for this date.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // date not used
                        //return true if the above weightModel is added successfully to weight.db
                        Boolean success = databaseHelperUserWeight.addOne(weightModel, username);
                        // Toast success status
                        Toast.makeText(AddEntryActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();

                        //return to previous screen with same button press
                        Intent intent = new Intent(AddEntryActivity.this, WeightDataActivity.class);
                        intent.putExtra("key", username);
                        startActivity(intent);
                    }
                }
                else {
                    try {
                        weightModel = new WeightModel(-1,username, btn_datePicker.getText().toString(), Integer.parseInt(et_weight.getText().toString()), "None Specified");
                    }
                    catch (Exception e) {
                        Toast.makeText(AddEntryActivity.this, "Error creating entry.", Toast.LENGTH_SHORT).show();
                        weightModel = new WeightModel(-1, "error", "error", 0, "error");
                    }

                    // update weightModel with delta if goal weight is set
                    DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(AddEntryActivity.this);
                    int goalWeight = databaseHelperUser.getUserGoalWeight(username);
                    if (goalWeight !=0) {
                        int delta = goalWeight - Integer.parseInt(et_weight.getText().toString());
                        weightModel.setDelta(delta);
                    }

                    // get database instance
                    DatabaseHelperUserWeight databaseHelperUserWeight = new DatabaseHelperUserWeight(AddEntryActivity.this);
                    //return true if there is already a weight entry for the given date
                    boolean dateUsed = databaseHelperUserWeight.findDate(username, btn_datePicker.getText().toString());

                    // if date previously used for the user then do not add, else add
                    if (dateUsed) {
                        Toast.makeText(AddEntryActivity.this, "User already has a weight saved for this date.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // date not used
                        //return true if the above weightModel is added successfully to weight.db
                        Boolean success = databaseHelperUserWeight.addOne(weightModel, username);
                        // Toast success status
                        Toast.makeText(AddEntryActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();

                        //return to previous screen with same button press
                        Intent intent = new Intent(AddEntryActivity.this, WeightDataActivity.class);
                        intent.putExtra("key", username);
                        startActivity(intent);
                    }
                }
            }
        });

        // cancel button -> return home
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEntryActivity.this, WeightDataActivity.class);
                startActivity(intent);
            }
        });

        // logic for category selection spinner
        Spinner category = findViewById(R.id.sp_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(this);
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

        // style setting for date picker
        int style = AlertDialog.THEME_HOLO_LIGHT;

        // date picker with max date being today's date
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year,month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    // input raw date information and outputs date as string
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    // Return string for the month based on month number
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

    // method called from onClick layout setting in activity_add_entry.xml (layout file)
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    // methods called for spinner selection
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        String text = "";
    }
}