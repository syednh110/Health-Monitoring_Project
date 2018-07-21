package com.respireplus.respire;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.respireplus.respire.apis.ApiDao;
import com.respireplus.respire.models.HealthResponse;
import com.respireplus.respire.utils.AskPermission;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdditionalDetails extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AdditionalDetails";
    EditText etDOB, etChestPain, etRBP, etCholesterol, etFBS, etRestECG, etThalach, etExang, etOld_Peak, etSlope, etThal;
    RadioButton rbMale, rbFemale;
    Spinner spEIA;
    Button btnSubmit;
    ImageButton ibGetECG;
    ProgressDialog progressDialog;
    AskPermission askPermission;

    public static final int REQUEST_CODE=10;
    public static final int CAMERA_PERMISSION_REQUEST_CODE=100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additional_details);

        etDOB=findViewById(R.id.etDOB);
        etChestPain=findViewById(R.id.etChestPain);
        etRBP=findViewById(R.id.etRBP);
        etCholesterol=findViewById(R.id.etCholesterol);
        etFBS=findViewById(R.id.etFBS);
        etRestECG=findViewById(R.id.etRestECG);
        etThalach=findViewById(R.id.etThalach);
        etExang=findViewById(R.id.etExang);
        etOld_Peak=findViewById(R.id.etOld_Peak);
        etSlope=findViewById(R.id.etSlope);
        etThal=findViewById(R.id.etThal);
        rbMale=findViewById(R.id.rbMale);
        rbFemale=findViewById(R.id.rbFemale);
        spEIA=findViewById(R.id.spEIA);

        ibGetECG=findViewById(R.id.ibGetECG);
        btnSubmit = findViewById(R.id.btnSubmit);

        ibGetECG.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);

        askPermission=new AskPermission(this);

        askPermission.askPermission(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit: {
                submitNow();
            } break;
            case R.id.ibGetECG: {
                if (askPermission.isPermissionAllowed(Manifest.permission.CAMERA)) {
                    Intent intent = new Intent(AdditionalDetails.this, HeartRateProcess.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                else{
                    Toast.makeText(this, "Please give camera permission from app settings.", Toast.LENGTH_SHORT).show();
                    askPermission.askPermission(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
                }
            } break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==CAMERA_PERMISSION_REQUEST_CODE){
            // TODO permission
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode==REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                String bpm=intent.getStringExtra("bpm");
//                Toast.makeText(this, data, Toast.LENGTH_LONG).show();
                etThalach.setText(bpm);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }
    }

    private void submitNow() {
        if (validate()){
            float age=Float.parseFloat(etDOB.getText().toString());
            float sex=1.0f;
            if(rbMale.isChecked()){
                sex=1.0f;
            }
            if(rbFemale.isChecked()){
                sex=0.0f;
            }
            float cp=Float.parseFloat(etChestPain.getText().toString());
            float rbp=Float.parseFloat(etRBP.getText().toString());
            float chol=Float.parseFloat(etCholesterol.getText().toString());
            float fbs=Float.parseFloat(etFBS.getText().toString());
            float ecg=Float.parseFloat(etRestECG.getText().toString());
            float thalach=Float.parseFloat(etThalach.getText().toString());
            float exang=Float.parseFloat(etExang.getText().toString());
            float old_peak=Float.parseFloat(etOld_Peak.getText().toString());
            float slope=Float.parseFloat(etSlope.getText().toString());
            float eia=(float) spEIA.getSelectedItemPosition();
            float thal=Float.parseFloat(etThal.getText().toString());

            final String input_data=age+" "+ sex+" "+ cp+" "+ rbp+" "+ chol+" "+ fbs+" "+
                    ecg+" "+thalach+" "+exang+" "+old_peak+" "+slope+" "+eia+" "+thal;
            Log.d(TAG, "submitNow: "+input_data);

            progressDialog.setTitle("Checking Up");
            progressDialog.setMessage("Wait until server response...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ApiDao.getApis().checkUp(input_data).enqueue(new Callback<HealthResponse>() {
                @Override
                public void onResponse(Call<HealthResponse> call, Response<HealthResponse> response) {
                    progressDialog.dismiss();
                    if (response.code()==200) {
                        HealthResponse health = response.body();
                        if (null!=health){
                            if (health.isStatus()){
                                Intent intent=new Intent(AdditionalDetails.this,HeartRateResult.class);
                                if (health.isResult()){
                                    intent.putExtra("result","Yes");
                                }
                                else {
                                    intent.putExtra("result","No");
                                }
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(AdditionalDetails.this, "Wrong input data !", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(AdditionalDetails.this, "Wrong input data !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AdditionalDetails.this, "Something wrong with server !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<HealthResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AdditionalDetails.this, "Faild to connect server !", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validate() {
        // TODO if not validate then just return false for  each case.
        if(TextUtils.isEmpty(etDOB.getText().toString()) || !TextUtils.isDigitsOnly(etDOB.getText().toString())){
            return false;
        }
        // TODO next checking...
        return true;
    }
}
