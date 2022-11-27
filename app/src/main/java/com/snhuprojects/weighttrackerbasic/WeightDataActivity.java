package com.snhuprojects.weighttrackerbasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Logic for the weight data screen (main app page)
public class WeightDataActivity extends AppCompatActivity {

    Button btn_logout, btn_addEntry, btn_settings, btn_editEntry, btn_deleteEntry;
    String username = "";
    WeightModel weightModel;
    RecyclerView rv_weight;
    DatabaseHelperUserWeight databaseHelperUserWeight;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // initialize activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_data);

        // bind buttons and edit text fields to the corresponding objects
        btn_logout = findViewById(R.id.btn_logout);
        btn_settings = findViewById(R.id.btn_settings);
        btn_addEntry = findViewById(R.id.btn_addEntry);
        btn_editEntry = findViewById(R.id.btn_editEntry);
        btn_deleteEntry = findViewById(R.id.btn_deleteEntry);
        databaseHelperUserWeight = new DatabaseHelperUserWeight(WeightDataActivity.this);

        // username was sent over from LoginActivity, so we must save that information as a variable
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("key");
            //The key argument here must match that used in the other activity
        }

        // add entry button logic
        btn_addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeightDataActivity.this, AddEntryActivity.class);
                intent.putExtra("key", username);
                startActivity(intent);
            }
        });

        // logout button logic
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // triggers the sms permission request based on the user's selected preference from within the SettingsActivity
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS)  == PackageManager.PERMISSION_GRANTED) {
                        DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(WeightDataActivity.this);
                        String phonenumber = databaseHelperUser.getPhoneNumber(username);
                        String SMS = "Thank you for using Weight Tracker basic! Check out our website for workouts and diet planning tips.";

                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phonenumber, null, SMS, null, null);
                            Toast.makeText(WeightDataActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(WeightDataActivity.this, "Error sending sms", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
                    }
                }
                // sends user back to the login page (LoginActivity)
                Intent intent = new Intent(WeightDataActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // start SettingsActivity, passing the username with the intent
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeightDataActivity.this, SettingsActivity.class);
                intent.putExtra("key", username);
                startActivity(intent);
            }
        });

        //start the EditEntryActivity with the username in the intent
        btn_editEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeightDataActivity.this, EditEntryActivity.class);
                intent.putExtra("key", username);
                startActivity(intent);
            }
        });

        //start the DeleteActivity with the username in the intent
        btn_deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeightDataActivity.this, DeleteEntryActivity.class);
                intent.putExtra("key", username);
                startActivity(intent);
            }
        });

        // recycler view variables and logic

        //identify the recycler view
        recyclerView = findViewById(R.id.rv_weights);

        // this setting improves performance
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter and receive data from database
        mAdapter = new RecycleViewAdapter(databaseHelperUserWeight.getUserWeights(username), WeightDataActivity.this);
        recyclerView.setAdapter(mAdapter);
    }
    //Send sms function
    DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(WeightDataActivity.this);
}