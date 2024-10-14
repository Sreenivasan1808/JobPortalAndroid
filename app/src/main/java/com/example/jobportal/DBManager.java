package com.example.jobportal;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;

public class DBManager {
    private Context context;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBManager(Context ctx) {
        context = ctx;
    }

    public DBManager open() throws SQLDataException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }

    public long insertUser(String email, String firstName, String lastName, String password, DBHelper.ROLES role) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.USER_EMAIL, email);
        cv.put(DBHelper.USER_FIRST_NAME, firstName);
        cv.put(DBHelper.USER_LAST_NAME, lastName);
        cv.put(DBHelper.USER_PASSWORD, password);
        cv.put(DBHelper.USER_ROLE, role.ordinal());
        System.out.println(role.name());
        System.out.println(role.ordinal());
        return database.insert(DBHelper.USER_TABLE, null, cv);
    }

    public int verifyCredentials(String email, String password) {
        String[] columns = new String[]{DBHelper.USER_EMAIL, DBHelper.USER_PASSWORD, DBHelper.USER_ID};
        String selection = DBHelper.USER_EMAIL + "='" + email + "'";
        System.out.println(selection);
        String[] selectionArgs = new String[]{email, password};
        Cursor cursor = database.query(DBHelper.USER_TABLE, columns, selection, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            System.out.println(cursor.getString(1));
            if (cursor.getString(1).equals(password)) {
                int id = cursor.getInt(2);
                cursor.close();
                return id;
            } else {
                cursor.close();
                return -1;
            }
        } else {
            return -1;
        }
    }

    DBHelper.ROLES getRole(String email) {
        DBHelper.ROLES role;
        String[] columns = new String[]{DBHelper.USER_ROLE};
        String selection = DBHelper.USER_EMAIL + "='" + email + "'";
        Cursor cursor = database.query(DBHelper.USER_TABLE, columns, selection, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int roleInt = cursor.getInt(0);
            cursor.close();
            role = roleInt == 0 ? DBHelper.ROLES.SEEKER : DBHelper.ROLES.RECRUITER;
            return role;
        } else {
            return null;
        }
    }

    public long addJob(String role, String org, String desc, String salary, String loc, int rid) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.JOB_ROLE, role);
        cv.put(DBHelper.JOB_COMPANY, org);
        cv.put(DBHelper.JOB_DESCRIPTION, desc);
        cv.put(DBHelper.JOB_SALARY, salary);
        cv.put(DBHelper.JOB_LOCATION, loc);
        cv.put(DBHelper.JOB_RECRUITER_ID, rid);
        return database.insert(DBHelper.JOB_TABLE, null, cv);
    }

    public Cursor getAllJobs() {
        Cursor cursor = database.query(DBHelper.JOB_TABLE, null, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor getJobsByRid(int recruiterId){
        String selection = DBHelper.JOB_RECRUITER_ID + "= ?";
        String[] selectionArgs = new String[]{recruiterId+""};
        Cursor cursor = database.query(DBHelper.JOB_TABLE, null, selection, selectionArgs, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor getJobByJid(int jobId){
        String selection = DBHelper.JOB_ID + "= ?";
        String[] selectionArgs = new String[]{jobId+""};
        Cursor cursor = database.query(DBHelper.JOB_TABLE, null, selection, selectionArgs, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public long applyForJob(int jobId, int userId) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.APPLICATION_JOB_ID, jobId);
        cv.put(DBHelper.APPLICATION_SEEKER_ID, userId);
        return database.insert(DBHelper.APPLICATION_TABLE, null, cv);
    }

    public boolean haveApplied(int jobId, int userId){
        String selection = DBHelper.APPLICATION_JOB_ID + "=? AND " + DBHelper.APPLICATION_SEEKER_ID + "=?";
        String[] selectionArgs = new String[]{jobId+"", userId+""};
        Cursor cursor = database.query(DBHelper.APPLICATION_TABLE, null, selection, selectionArgs, null, null, null, null);
        return cursor != null && cursor.getCount() != 0;
    }

    public Cursor getAppliedJobs(int seekerId){
        String query = "SELECT * FROM "
                + DBHelper.JOB_TABLE
                + " INNER JOIN "
                + DBHelper.APPLICATION_TABLE
                + " ON " + DBHelper.JOB_TABLE + "." + DBHelper.JOB_ID + " = " + DBHelper.APPLICATION_TABLE + "." + DBHelper.APPLICATION_JOB_ID
                + " WHERE " + DBHelper.APPLICATION_SEEKER_ID + " = ?;";

        Cursor cursor = database.rawQuery(query, new String[]{seekerId+""});
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
}