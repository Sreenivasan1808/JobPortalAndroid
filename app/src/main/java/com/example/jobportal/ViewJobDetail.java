package com.example.jobportal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class ViewJobDetail extends AppCompatActivity {

    private int jobId;
    private DBHelper.ROLES role;
    private int userId;
    private DBManager dbManager;

    private TextView roleVal;
    private TextView companyVal;
    private TextView salaryVal;
    private TextView descVal;
    private TextView locationVal;
    private Button applyBtn;
    private MaterialToolbar materialToolbar;


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_job_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        jobId = getIntent().getIntExtra("jobId", -1);
        int roleInt = getIntent().getIntExtra("role", 0);
        role = roleInt == 0 ? DBHelper.ROLES.SEEKER : DBHelper.ROLES.RECRUITER;
        userId = getIntent().getIntExtra("uid", -1);


        roleVal = findViewById(R.id.role_val);
        salaryVal = findViewById(R.id.salary_val);
        companyVal = findViewById(R.id.company_val);
        descVal = findViewById(R.id.desc_val);
        locationVal = findViewById(R.id.location_val);
        applyBtn = findViewById(R.id.apply_btn);
        materialToolbar = findViewById(R.id.viewJobToolbar);
        materialToolbar.setNavigationOnClickListener(V -> {

            Intent i;
            if(role == DBHelper.ROLES.SEEKER) {
                i = new Intent(this, SeekerDashboard.class);
                i.putExtra("sid", userId);
            }
            else {
                i = new Intent(this, RecruiterDashboard.class);
                i.putExtra("rid", userId);
            }
            startActivity(i);

        });


        dbManager = new DBManager(this);
        try {
            dbManager.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cursor jobDetail = dbManager.getJobByJid(jobId);

        roleVal.setText(jobDetail.getString(jobDetail.getColumnIndex(DBHelper.JOB_ROLE)));
        salaryVal.setText(jobDetail.getString(jobDetail.getColumnIndex(DBHelper.JOB_SALARY)));
        companyVal.setText(jobDetail.getString(jobDetail.getColumnIndex(DBHelper.JOB_COMPANY)));
        descVal.setText(jobDetail.getString(jobDetail.getColumnIndex(DBHelper.JOB_DESCRIPTION)));
        locationVal.setText(jobDetail.getString(jobDetail.getColumnIndex(DBHelper.JOB_LOCATION)));

        boolean hasApplied = dbManager.haveApplied(jobId, userId);

        if (role == DBHelper.ROLES.RECRUITER || hasApplied) {
            applyBtn.setVisibility(View.GONE);

        } else {
            applyBtn.setOnClickListener(V -> {
                long status = dbManager.applyForJob(jobId, userId);
                Log.d("Apply for Job", "Status returned "+ status);
                if(status >= 0 ){
                    Toast.makeText(this, "Successfully applied for the job", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error occurred. Please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}