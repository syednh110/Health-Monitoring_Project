package com.respireplus.respire;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class HeartRateResult extends AppCompatActivity{

    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart_rate_result);

        tvResult=findViewById(R.id.tvResult);
        Intent intent=getIntent();
        tvResult.setText(intent.getStringExtra("result"));
    }

}
