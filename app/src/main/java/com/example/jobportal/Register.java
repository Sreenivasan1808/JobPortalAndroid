package com.example.jobportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;
    private EditText emailTxt;
    private EditText firstNameTxt;
    private EditText lastNameTxt;
    private EditText passwordTxt;
    private EditText confirmPassTxt;
    private RadioButton jobSeekerRbtn;
    private RadioButton recruiterRbtn;
    private RadioGroup roleGroup;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.login_button);
        emailTxt = findViewById(R.id.email);
        firstNameTxt = findViewById(R.id.firstName);
        lastNameTxt = findViewById(R.id.lastName);
        passwordTxt = findViewById(R.id.password);
        confirmPassTxt = findViewById(R.id.confirmPass);
        jobSeekerRbtn = findViewById(R.id.roleJobSeeker);
        recruiterRbtn = findViewById(R.id.roleRecruiter);
        roleGroup = findViewById(R.id.roleGroup);

        dbManager = new DBManager(this);

        try {
            dbManager.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerButton.setOnClickListener(V -> {
            boolean isValid = validateFields();
            if (isValid) {
                String email = emailTxt.getText().toString();
                String firstName = firstNameTxt.getText().toString();
                String lastName = lastNameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                DBHelper.ROLES role;
                if(recruiterRbtn.isChecked()){
                    role = DBHelper.ROLES.RECRUITER;
                }else {
                    role = DBHelper.ROLES.SEEKER;
                }


                long status = dbManager.insertUser(email, firstName, lastName, password, role);

                if (status >= 0) {
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Register.this, MainActivity.class);
                    startActivity(i);
                } else {

                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginButton.setOnClickListener(V -> {
            Intent i = new Intent(Register.this, MainActivity.class);
            startActivity(i);
        });


    }

    private boolean validateFields() {
        String email = emailTxt.getText().toString();
        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String confirmPass = confirmPassTxt.getText().toString();

        if (email.length() == 0) {
            emailTxt.setError("This is a required field");
            return false;
        }
        if (firstName.length() == 0) {
            firstNameTxt.setError("This is a required field");
            return false;
        }
        if (lastName.length() == 0) {
            lastNameTxt.setError("This is a required field");
            return false;
        }

        if (password.length() == 0) {
            passwordTxt.setError("This is a required field");
            return false;
        }
        if (confirmPass.length() == 0) {
            confirmPassTxt.setError("This is a required field");
            return false;
        }
        if (!confirmPass.equals(password)) {
            confirmPassTxt.setError("Should match the password field");
            return false;
        }
        return true;
    }
}