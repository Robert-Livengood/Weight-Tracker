package com.snhuprojects.weighttrackerbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

// logic for the login/register screen
public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    private TextView eAttemptsInfo;
    private int counter = 5;
    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link variables to corresponding buttons and edit text boxes
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        //TODO
        // eAttemptsInfo = findViewById(R.id.tvAttemptsInfo);
        // -> this is to notify user and disable login button after too many failed attempts

        // Logic for when the register button is pressed
        // Creates the userModel database using DatabaseHelper
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userModel;
                // try/catch helps to avoid crashing in case of user not inputting all constructor elements
                try {
                    userModel = new UserModel(-1, et_username.getText().toString(), et_password.getText().toString());
                    //Toast.makeText(LoginActivity.this, userModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Error registering user.", Toast.LENGTH_SHORT).show();
                    userModel = new UserModel(-1, "Error", "Error");
                }

                // initializes user database
                DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(LoginActivity.this);
                boolean userExists = databaseHelperUser.checkUsername(et_username.getText().toString());
                if (userExists) {
                    Toast.makeText(LoginActivity.this, "Error username taken. Enter a unique username.", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean success = databaseHelperUser.addOne(userModel);
                    Toast.makeText(LoginActivity.this, "User registered = " +success, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Logic for when the login button is pressed
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from input to variables
                String inputName = et_username.getText().toString();
                String inputPassword = et_password.getText().toString();

                // Checks for input and validation
                if(inputName.isEmpty()  || inputPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please input an email and password.", Toast.LENGTH_SHORT).show();
                }else{

                    isValid = validate(inputName, inputPassword);

                    // if not valid decrement counter, else start new activity (WeightData)
                    // allows for lockout mechanic
                    if(!isValid){
                        counter--;
                        Toast.makeText(getApplicationContext(), "Incorrect credentials entered.", Toast.LENGTH_SHORT).show();

                        //eAttemptsInfo.setText("Number of attempts remaining: " + counter);

                        if(counter == 0){
                            btn_login.setEnabled(false);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, WeightDataActivity.class);
                        intent.putExtra("key", et_username.getText().toString());
                        startActivity(intent);
                    }

                }
            }
        });
    }
    //boolean check for username and password
    private boolean validate(String name, String password) {

        DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(LoginActivity.this);
        try {
            List<String> user = databaseHelperUser.findUser(name);
            if (name.equals(user.get(0)) && password.equals(user.get(1))) {
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Error: User does not exist.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}