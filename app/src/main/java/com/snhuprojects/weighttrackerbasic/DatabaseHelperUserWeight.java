package com.snhuprojects.weighttrackerbasic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperUserWeight extends SQLiteOpenHelper {
    public static final String WEIGHT_TABLE = "WEIGHT_TABLE";
    public static final String COLUMN_ID = "COLUMN_ID";
    public static final String COLUMN_USERNAME = "COLUMN_USERNAME";
    public static final String COLUMN_DATE = "COLUMN_DATE";
    public static final String COLUMN_WEIGHT = "COLUMN_WEIGHT";
    public static final String COLUMN_DELTA = "COLUMN_DELTA";
    public static final String COLUMN_CATEGORY = "COLUMN_CATEGORY";

    public DatabaseHelperUserWeight(@Nullable Context context) {
        super(context, "weight.db", null, 1);
    }

    // This is called the first time the database is accessed. This code will create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + WEIGHT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_WEIGHT + " INTEGER, " + COLUMN_DELTA + " INTEGER, " + COLUMN_CATEGORY + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // add row to database table
    public boolean addOne(WeightModel weightModel, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_DATE, weightModel.getDate());
        cv.put(COLUMN_WEIGHT, weightModel.getWeight());
        cv.put(COLUMN_DELTA,weightModel.getDelta());
        cv.put(COLUMN_CATEGORY, weightModel.getWorkoutType());

        long insert = db.insert(WEIGHT_TABLE, null, cv);
        db.close();
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    //delete rows from the weight database
    public boolean deleteOne(String username, String date) {
        // delete entry based on username and given date
        SQLiteDatabase db= this.getWritableDatabase();
        int success = db.delete(WEIGHT_TABLE, "COLUMN_USERNAME = ? and COLUMN_DATE =?", new String[]{username, date});
        db.close();
        if (success == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateUserWeight(String username, String date, int weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WEIGHT_TABLE + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_DATE + " = ?", new String[]{username, date});
        // if cursor movesToFirst then the query returned an entry from weight.db that had the same username and date.
        if (cursor.moveToFirst()) {
            // database returns id as int but needs to be queried as a string
            int idINT = cursor.getInt(0);
            String id = String.valueOf(idINT);
            // calculate new delta
            int oldDelta = cursor.getInt(4);
            int oldWeight = cursor.getInt(3);
            int goalWeight = oldWeight + oldDelta;
            int delta = goalWeight - weight;
            // create content values object for update method
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USERNAME, username);
            cv.put(COLUMN_DATE, date);
            cv.put(COLUMN_WEIGHT, weight);
            cv.put(COLUMN_DELTA, delta);
            db.update(WEIGHT_TABLE, cv, "COLUMN_ID=?", new String[] {id});
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    // return weight list for users with given username
    public List<WeightModel> getUserWeights(String username) {
        List<WeightModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + WEIGHT_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // loop through the cursor for all the instances where the username matches
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                int weight = cursor.getInt(3);
                int delta = cursor.getInt(4);
                String workoutCategory = cursor.getString(5);
                // create WeightModel object with retrieved data and add to list
                WeightModel newWeight = new WeightModel(id, name, date, weight,delta, workoutCategory);
                returnList.add(newWeight);
            }
            while (cursor.moveToNext());
        }
        else {
            // Failure. Users entries not found for given user.
        }
        // close the cursor and the database when done.
        cursor.close();
        db.close();
        return returnList;
    }

    // update delta column for all entries with the given username.
    public boolean updateDelta(String username, int goalWeight) {
        int delta;
        String queryString = "SELECT * FROM " + WEIGHT_TABLE + " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        do {
            int idINT = cursor.getInt(0);
            String id = String.valueOf(idINT);
            String date = cursor.getString(2);
            int currWeight = cursor.getInt(3);
            delta = goalWeight - currWeight;
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USERNAME, username);
            cv.put(COLUMN_DATE, date);
            cv.put(COLUMN_WEIGHT, currWeight);
            cv.put(COLUMN_DELTA, delta);
            db.update(WEIGHT_TABLE, cv, "COLUMN_ID=?", new String[] {id});
        } while (cursor.moveToNext());
        cursor.close();
        db.close();
        return true;
    }

    // Check if an entry exists for the given date
    public boolean findDate(String username, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String queryString = "SELECT * FROM " + WEIGHT_TABLE + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_DATE + " = ?";
        Cursor cursor = db.rawQuery("SELECT * FROM " + WEIGHT_TABLE + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_DATE + " = ?", new String[]{username, date});
        // if cursor movesToFirst then the query returned an entry from weight.db that had the same username and date.
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }
}