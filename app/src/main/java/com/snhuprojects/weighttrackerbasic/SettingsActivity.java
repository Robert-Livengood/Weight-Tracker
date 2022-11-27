package com.snhuprojects.weighttrackerbasic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

//Activity for setting screen
public class SettingsActivity extends AppCompatActivity {

    EditText et_goalWeight, et_phoneNumber, et_changePassword;
    Switch sw_textPerms;
    Button btn_apply, btn_home;
    DatabaseHelperUser databaseHelperUser;
    String username = "Error";
    private int SMS_PERMISSION_CODE = 1;

    // main logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // bind variables to corresponding layout objects
        et_goalWeight = findViewById(R.id.et_goalWeight);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        et_changePassword = findViewById(R.id.et_changePassword);
        sw_textPerms = findViewById(R.id.sw_textPerms);
        btn_apply = findViewById(R.id.btn_apply);
        btn_home = findViewById(R.id.btn_home);

        //get username included with intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("key"); // Key must match from the screen that passed the intent to here.
        }
        else {}

        // Get the current smsPerms
        DatabaseHelperUser databaseHelperUser =  new DatabaseHelperUser(SettingsActivity.this);
        int smsPerms = databaseHelperUser.getSmsPerms(username);
        sw_textPerms.setChecked(smsPerms == 1);

        // home button logic
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to WeightActivity -> does not apply any unsaved changes
                Intent intent = new Intent(SettingsActivity.this, WeightDataActivity.class);
                intent.putExtra("key", username);
                startActivity(intent);
            }
        });

        // Apply changes button logic
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // saves user input as variables
                String goalWeight = et_goalWeight.getText().toString();
                String phoneNumber = et_phoneNumber.getText().toString();
                String newPassword = et_changePassword.getText().toString();
                boolean smsPerms = sw_textPerms.isChecked();

                // update goal weight if string is not empty
                if (!goalWeight.matches("")) {
                    // user.db instance
                    DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(SettingsActivity.this);
                    boolean success = databaseHelperUser.updateGoalWeight(username, Integer.parseInt(et_goalWeight.getText().toString()));
                    if (success){
                        Toast.makeText(SettingsActivity.this, "Goal updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                }
                // update weight.db delta values upon changing the goal weight
                if (!goalWeight.matches("")) {
                    // weight.db instance
                    DatabaseHelperUserWeight databaseHelperUserWeight = new DatabaseHelperUserWeight(SettingsActivity.this);
                    boolean success = databaseHelperUserWeight.updateDelta(username, Integer.parseInt(et_goalWeight.getText().toString()));
                    if (success){
                        Toast.makeText(SettingsActivity.this, "Delta updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "Delta update failed.", Toast.LENGTH_SHORT).show();
                    }
                }
                // update user.db with new phone number
                if (!phoneNumber.matches("")) {
                    DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(SettingsActivity.this);
                    boolean success = databaseHelperUser.updatePhoneNumber(username, et_phoneNumber.getText().toString());
                    if (success){
                        Toast.makeText(SettingsActivity.this, "Phone number updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "Phone number update failed.", Toast.LENGTH_SHORT).show();
                    }
                }
                // update user.db with new password
                if (!newPassword.matches("")) {
                    DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(SettingsActivity.this);
                    boolean success = databaseHelperUser.updatePassword(username, newPassword);
                    if (success){
                        Toast.makeText(SettingsActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "Password number update failed.", Toast.LENGTH_SHORT).show();
                    }
                }

                // update user.db with sms perms value ->
                if (smsPerms) {
                    DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(SettingsActivity.this);
                    boolean success = databaseHelperUser.updateSmsPerms(username, 1);
                    if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SettingsActivity.this, "Permission already granted.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        requestSMSPermission();
                    }
                    if (success){
                        Toast.makeText(SettingsActivity.this, "SMS permissions updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "SMS permissions update failed.", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!smsPerms) {
                    DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(SettingsActivity.this);
                    boolean success = databaseHelperUser.updateSmsPerms(username, 0);
                    if (success){
                        Toast.makeText(SettingsActivity.this, "SMS permissions updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "SMS permissions update failed.", Toast.LENGTH_SHORT).show();
                    }
                }


                // Return home AFTER updating user model
                Intent intent = new Intent(SettingsActivity.this, WeightDataActivity.class);
                intent.putExtra("key", username);
                startActivity(intent);
            }
        });
    }

    // sms permission method
    private void requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("This permission is needed to send workout and diet websites.").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }
    // updates resource files with response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();

            }
        }
    }
}