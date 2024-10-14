package com.example.jobportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class AddJob extends AppCompatActivity {


    EditText roleTxt;
    EditText orgTxt;
    EditText descTxt;
    EditText salaryTxt;
    EditText locTxt;
    Button saveBtn;
    Button cancelBtn;
    int recruiterId;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        toolbar.setNavigationOnClickListener(v -> {
            Intent i = new Intent(AddJob.this, RecruiterDashboard.class);
            i.putExtra("rid", recruiterId);
            startActivity(i);
        });

        roleTxt = findViewById(R.id.roleTxt);
        orgTxt = findViewById(R.id.orgTxt);
        descTxt = findViewById(R.id.descTxt);
        salaryTxt = findViewById(R.id.salaryTxt);
        locTxt = findViewById(R.id.locTxt);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        dbManager = new DBManager(this);
        try {
            dbManager.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        recruiterId = getIntent().getIntExtra("rid", -1);
        if(recruiterId == -1){
            getOnBackPressedDispatcher().onBackPressed();
        }

        saveBtn.setOnClickListener(V -> {
            String role = roleTxt.getText().toString();
            String organization = orgTxt.getText().toString();
            String description = descTxt.getText().toString();
            String salary = salaryTxt.getText().toString();
            String location = locTxt.getText().toString();
            long status = dbManager.addJob(role, organization, description, salary, location, recruiterId);
            if(status >= 0){
                Toast.makeText(this, "Posted a new job successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddJob.this, RecruiterDashboard.class);
                i.putExtra("rid", recruiterId);
                startActivity(i);

            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(V -> {
            Intent i = new Intent(AddJob.this, RecruiterDashboard.class);
            i.putExtra("rid", recruiterId);
            startActivity(i);
        });

    }
}