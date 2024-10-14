package com.example.jobportal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class AppliedJobs extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private int userId;
    private DBManager dbManager;
    private Cursor appliedJobsCursor;
    private JobAdapter jobAdapter;
    private RecyclerView appliedJobList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_applied_jobs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getIntExtra("sid", -1);

        dbManager = new DBManager(this);
        try {
            dbManager.open();
        }catch (Exception e ){
            e.printStackTrace();
        }

        materialToolbar = findViewById(R.id.appliedJobToolbar);
        materialToolbar.setNavigationOnClickListener(V -> {
            Intent i = new Intent(this, SeekerDashboard.class);
            i.putExtra("uid", userId);
            dbManager.close();
            startActivity(i);
        });
        appliedJobList = findViewById(R.id.appliedJobList);

        appliedJobsCursor = dbManager.getAppliedJobs(userId);
        List<Job> jobList = getJobsFromCursor(appliedJobsCursor);
        jobAdapter = new JobAdapter(this, jobList, DBHelper.ROLES.SEEKER, userId);
        appliedJobList.setLayoutManager(new LinearLayoutManager(this));
        appliedJobList.setAdapter(jobAdapter);

        appliedJobsCursor.close();
        dbManager.close();
    }

    public List<Job> getJobsFromCursor(Cursor cursor) {
        List<Job> jobs = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String[] cols = cursor.getColumnNames();
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DBHelper.JOB_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(DBHelper.JOB_ROLE));
                @SuppressLint("Range") String salary = cursor.getString(cursor.getColumnIndex(DBHelper.JOB_SALARY));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(DBHelper.JOB_LOCATION));
                @SuppressLint("Range") String organization = cursor.getString(cursor.getColumnIndex(DBHelper.JOB_COMPANY));

                jobs.add(new Job(id, title, salary, location, organization));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return jobs;
    }
}