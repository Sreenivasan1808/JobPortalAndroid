package com.example.jobportal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.jobportal.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Button loginBtn;
    private Button registerBtn;
    private EditText emailTxt;
    private EditText passwordTxt;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginBtn = (Button) findViewById(R.id.login_button);
        registerBtn = findViewById(R.id.register_button);
        emailTxt = findViewById(R.id.email);
        passwordTxt = findViewById(R.id.password);

        dbManager = new DBManager(this);

        try {
            dbManager.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginBtn.setOnClickListener(V -> {
            String email = emailTxt.getText().toString();
            String password = passwordTxt.getText().toString();
            int id = dbManager.verifyCredentials(email, password);
            if (id == -1) {
                Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "Logged in successfully", Toast.LENGTH_LONG).show();
                return;
            }

            DBHelper.ROLES role = dbManager.getRole(email);
            if (role == DBHelper.ROLES.RECRUITER) {
                Intent i = new Intent(MainActivity.this, RecruiterDashboard.class);
                i.putExtra("rid", id);
                startActivity(i);
            } else {
                Intent i = new Intent(MainActivity.this, SeekerDashboard.class);
                i.putExtra("sid", id);
                startActivity(i);
            }

            dbManager.close();



        });

        registerBtn.setOnClickListener(V -> {
            Intent i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
        });


    }


}