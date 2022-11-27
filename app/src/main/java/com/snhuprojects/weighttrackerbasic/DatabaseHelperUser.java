package com.snhuprojects.weighttrackerbasic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperUser extends SQLiteOpenHelper {


    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_USER_PASSWORD = "USER_PASSWORD";
    public static final String COLUMN_USER_GOAL_WEIGHT = "USER_GOAL_WEIGHT";
    public static final String COLUMN_USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    public static final String COLUMN_USER_SMS_ENABLED = "USER_SMS_ENABLED";
    public static final String COLUMN_USERNAME = "COLUMN_USERNAME";
    public static final String COLUMN_ID = "ID";

    public DatabaseHelperUser(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }

    // this is called the first time a database is accessed. This is contains code to create the database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_USER_PASSWORD + " TEXT, " + COLUMN_USER_GOAL_WEIGHT + " INTEGER, " + COLUMN_USER_PHONE_NUMBER + " TEXT, " + COLUMN_USER_SMS_ENABLED + " BOOL)";
        db.execSQL(createTableStatement);
    }

    // this is called if the database version is upgraded. Allows for future upgrades without breaking the user's database.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Method used when registering new user. Only adds username and password - all other values are set to default until updated in settings.
    public boolean addOne(UserModel userModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, userModel.getUserName());
        cv.put(COLUMN_USER_PASSWORD, userModel.getPassword());
        cv.put(COLUMN_USER_GOAL_WEIGHT, 0);
        cv.put(COLUMN_USER_PHONE_NUMBER, "");
        cv.put(COLUMN_USER_SMS_ENABLED,0);

        long insert = db.insert(USER_TABLE, null, cv);
        if (insert == -1) {
            return false;
        }
        else{
            return true;
        }
    }

    // Updates user.db with the provided current weight value.
    public boolean updateGoalWeight(String userName, int goalWeight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        String password = cursor.getString(2);
        String phoneNumber = cursor.getString(4);
        int smsPerms = cursor.getInt(5);
        int idINT = cursor.getInt(0);
        String id = String.valueOf(idINT);
        cv.put(COLUMN_USERNAME, userName);
        cv.put(COLUMN_USER_PASSWORD, password);
        cv.put(COLUMN_USER_GOAL_WEIGHT, goalWeight);
        cv.put(COLUMN_USER_PHONE_NUMBER, phoneNumber);
        cv.put(COLUMN_USER_SMS_ENABLED, smsPerms);
        //db.insert(USER_TABLE, null, cv);
        int success =  db.update(USER_TABLE, cv, "ID=?", new String[] {id});
        cursor.close();
        db.close();
        if (success == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    // update user.db with phone number provided for the given username
    public boolean updatePhoneNumber(String username, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        String password = cursor.getString(2);
        int goalWeight = cursor.getInt(3);
        int smsPerms = cursor.getInt(5);
        int idINT = cursor.getInt(0);
        String id = String.valueOf(idINT);
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_USER_PASSWORD, password);
        cv.put(COLUMN_USER_GOAL_WEIGHT, goalWeight);
        cv.put(COLUMN_USER_PHONE_NUMBER, phoneNumber);
        cv.put(COLUMN_USER_SMS_ENABLED, smsPerms);
        //db.insert(USER_TABLE, null, cv);
        int success =  db.update(USER_TABLE, cv, "ID=?", new String[] {id});
        cursor.close();
        db.close();
        if (success == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    // update user.db with new password
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        int goalWeight = cursor.getInt(3);
        String phoneNumber = cursor.getString(4);
        int smsPerms = cursor.getInt(5);
        int idINT = cursor.getInt(0);
        String id = String.valueOf(idINT);
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_USER_PASSWORD, newPassword);
        cv.put(COLUMN_USER_GOAL_WEIGHT, goalWeight);
        cv.put(COLUMN_USER_PHONE_NUMBER, phoneNumber);
        cv.put(COLUMN_USER_SMS_ENABLED, smsPerms);
        //db.insert(USER_TABLE, null, cv);
        int success =  db.update(USER_TABLE, cv, "ID=?", new String[] {id});
        cursor.close();
        db.close();
        if (success == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    // update smsPerms in user.db with given value for given username
    public boolean updateSmsPerms(String username, int smsPerms) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        int goalWeight = cursor.getInt(3);
        String phoneNumber = cursor.getString(4);
        String password = cursor.getString(2);
        int idINT = cursor.getInt(0);
        String id = String.valueOf(idINT);
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_USER_PASSWORD, password);
        cv.put(COLUMN_USER_GOAL_WEIGHT, goalWeight);
        cv.put(COLUMN_USER_PHONE_NUMBER, phoneNumber);
        cv.put(COLUMN_USER_SMS_ENABLED, smsPerms);
        //db.insert(USER_TABLE, null, cv);
        int success =  db.update(USER_TABLE, cv, "ID=?", new String[] {id});
        cursor.close();
        db.close();
        if (success == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    // retrieve the current smsPerms from user.db
    public int getSmsPerms(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        int smsPerms = cursor.getInt(5);
        return smsPerms;
    }

    public String getPhoneNumber(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        String phoneNumber = cursor.getString(4);
        return phoneNumber;
    }


    // Retrieves the current weight value for the given user
    public int getUserGoalWeight(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        int goalWeight = cursor.getInt(3);
        cursor.close();
        db.close();
        return goalWeight;
    }

    // Search
    public List<String> findUser(String username) {

        List<String> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // loop through the cursor and return the username and password.
            do {
                String password = cursor.getString(2);
                returnList.add(username);
                returnList.add(password);
            } while (cursor.moveToNext());
        }
        else {
            //failure. did not find and users with given username.
        }
        //close both cursor and db
        cursor.close();
        db.close();
        return returnList;
    }
    public boolean checkUsername(String username) {
        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            // username exists
            return true;
        }
        else {
            return false;
        }
    }
}
