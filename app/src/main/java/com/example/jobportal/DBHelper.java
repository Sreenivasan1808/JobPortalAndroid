package com.example.jobportal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "job_portal.db";
    static final int DB_VERSION = 1;

    //User Data
    static final String USER_TABLE = "USERS";
    static final String USER_ID = "_ID";
    static final String USER_FIRST_NAME = "FIRST_NAME";
    static final String USER_LAST_NAME = "LAST_NAME";
    static final String USER_EMAIL = "EMAIL";
    static final String USER_PASSWORD = "PASSWORD";
    static final String USER_ROLE = "ROLE";

    static enum ROLES {SEEKER, RECRUITER};

    private static final String CREATE_USER_TABLE_QUERY = "CREATE TABLE " + USER_TABLE + " ("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_EMAIL + " TEXT NOT NULL UNIQUE, "
            + USER_FIRST_NAME + " TEXT NOT NULL, "
            + USER_LAST_NAME + " TEXT NOT NULL, "
            + USER_ROLE + " INTEGER NOT NULL, "
            + USER_PASSWORD + " TEXT);";


    //Job Details
    static final String JOB_TABLE = "JOBS";
    static final String JOB_ID = "_JID";
    static final String JOB_ROLE = "ROLE";
    static final String JOB_DESCRIPTION = "DESCRIPTION";
    static final String JOB_COMPANY = "COMPANY";
    static final String JOB_SALARY = "SALARY";
    static final String JOB_LOCATION = "LOCATION";
    static final String JOB_RECRUITER_ID = "RECRUITER_ID";


    private static final String CREATE_JOB_TABLE_QUERY = "CREATE TABLE " + JOB_TABLE + " ("
            + JOB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + JOB_ROLE + " TEXT NOT NULL, "
            + JOB_DESCRIPTION + " TEXT, "
            + JOB_COMPANY + " TEXT NOT NULL, "
            + JOB_SALARY + " TEXT, "
            + JOB_LOCATION + " TEXT, "
            + JOB_RECRUITER_ID + " INTEGER, "
            + "FOREIGN KEY ("+ JOB_RECRUITER_ID + ") REFERENCES " + USER_TABLE +"("+ USER_ID +"));";

    static final String APPLICATION_TABLE = "APPLICATIONS";
    static final String APPLICATION_JOB_ID = "JOB_ID";
    static final String APPLICATION_SEEKER_ID = "SEEKER_ID";

    private static final String CREATE_APPLICATIONS_TABLE_QUERY = "CREATE TABLE " + APPLICATION_TABLE + " ("
            + APPLICATION_JOB_ID + " INTEGER,"
            + APPLICATION_SEEKER_ID + " INTEGER,"
            + "PRIMARY KEY(" + APPLICATION_JOB_ID + "," + APPLICATION_SEEKER_ID + "),"
            + "FOREIGN KEY (" + APPLICATION_SEEKER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "),"
            + "FOREIGN KEY (" + APPLICATION_JOB_ID + ") REFERENCES " + JOB_TABLE + "(" + JOB_ID + "));";




    public DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE_QUERY);
        db.execSQL(CREATE_JOB_TABLE_QUERY);
        db.execSQL(CREATE_APPLICATIONS_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + JOB_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + APPLICATION_TABLE);
    }
}
