package com.example.jobportal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

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

public class RecruiterDashboard extends AppCompatActivity {

    MaterialToolbar materialToolbar;
    int recruiterId;

    Cursor myJobsCursor;
    DBManager dbManager;
    private RecyclerView myJobsList;
    private JobAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recruiter_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        materialToolbar = findViewById(R.id.recruiter_toolbar);
        myJobsList = findViewById(R.id.myJobsList);
        setSupportActionBar(materialToolbar);

        Intent intent = getIntent();
        recruiterId = intent.getIntExtra("rid", -1);

        dbManager = new DBManager(this);
        try {
            dbManager.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myJobsCursor = dbManager.getJobsByRid(recruiterId);

        List<Job> jobList = getJobsFromCursor(myJobsCursor);
        jobAdapter = new JobAdapter(this, jobList, DBHelper.ROLES.RECRUITER, recruiterId);
        myJobsList.setLayoutManager(new LinearLayoutManager(this));
        myJobsList.setAdapter(jobAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recruiter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_post) {

            Intent i = new Intent(RecruiterDashboard.this, AddJob.class);
            i.putExtra("rid", recruiterId);
            startActivity(i);
            return true;
        } else if (id == R.id.action_logout) {
            Intent i = new Intent(RecruiterDashboard.this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<Job> getJobsFromCursor(Cursor cursor) {
        List<Job> jobs = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
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