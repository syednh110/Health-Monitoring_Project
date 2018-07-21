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


public class Login extends AppCompatActivity{

    AppDatabase db;
    Button btnLogin, btnSignup;
    EditText etMobile, etPwd;
//    TextView tvForgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        db= Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AllKeys.DB_NAME)
                .allowMainThreadQueries()
                .build();

        btnLogin = (Button) findViewById(R.id.Login);
        btnSignup = (Button) findViewById(R.id.SignUp);
        etMobile = (EditText) findViewById(R.id.MobileNo);
        etPwd = (EditText) findViewById(R.id.Password);
//        tvForgotPwd = (TextView) findViewById(R.id.ForgotPwd);

//        tvForgotPwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Login.this, ForgotPassword.class));
//            }
//        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user=db.getMyDao().login(etMobile.getText().toString(),etPwd.getText().toString());
                if (null!=user){
                    if (etMobile.getText().toString().equals(user.getMobile())){
                        Toast.makeText(Login.this, "Login successfully !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, AdditionalDetails.class));
                    }
                    else {
                        Toast.makeText(Login.this, "Login failed !", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Login.this, "Login failed !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
