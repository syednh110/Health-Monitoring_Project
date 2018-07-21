package com.respireplus.respire;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.respireplus.respire.apis.AppDatabase;
import com.respireplus.respire.models.User;
import com.respireplus.respire.utils.AllKeys;

public class SignUp extends AppCompatActivity {

    Button btnLogin, btnSignup;
    EditText etMobile, etPwd, etName, etMail;
    TextView tvPolicy;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        db= Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AllKeys.DB_NAME)
                .allowMainThreadQueries()
                .build();

        btnLogin = (Button) findViewById(R.id.Login);
        btnSignup = (Button) findViewById(R.id.SignUp);
        etMail = (EditText) findViewById(R.id.Email);
        etMobile = (EditText) findViewById(R.id.MobileNo);
        etName = (EditText) findViewById(R.id.Name);
        etPwd = (EditText) findViewById(R.id.Password);
        tvPolicy = (TextView) findViewById(R.id.tnc);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(etName.getText().toString(),etMobile.getText().toString(),etMail.getText().toString(),etPwd.getText().toString());
            }
        });


    }

    private void signup(String name, String mobile, String email, String password) {
        User user=new User(name,mobile,email,password);
        System.out.println(user);
        if (db.getMyDao().getUserCount(mobile)<1) {
            db.getMyDao().register(user);
            if (db.getMyDao().getUserCount(mobile)>0) {
                Toast.makeText(getApplicationContext(), "Registered successfully !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Registration failed !", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Already exists !", Toast.LENGTH_SHORT).show();
        }
    }
}
