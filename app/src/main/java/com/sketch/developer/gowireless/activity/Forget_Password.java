package com.sketch.developer.gowireless.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;


/**
 * Created by Developer on 6/20/17.
 */
public class Forget_Password extends AppCompatActivity {

    String TAG = "f_pass";
    EditText fpass_email;
    TextView submit_btn,txtview_forgot_pass,txtview_note;
    RelativeLayout rl_submit_btn;
    ProgressDialog pd;
    EditText input_email;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.forgot_password);


        pd=new ProgressDialog(Forget_Password.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");


        input_email  = findViewById(R.id.input_email);
        submit_btn = findViewById(R.id.submit_btn);
        txtview_forgot_pass = findViewById(R.id.txtview_forgot_pass);
        txtview_note = findViewById(R.id.txtview_note);
        rl_submit_btn = findViewById(R.id.rl_submit_btn);


        rl_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Intent intent = new Intent(Forget_Password.this, Reset_Password.class);
                startActivity(intent);
                finish();*/

                    if (!input_email.getText().toString().trim().isEmpty()) {
                        if(input_email.getText().toString().matches(emailPattern)){
                            forgot_pass_url();
                        }else {
                            Toasty.error(Forget_Password.this,"Invalid email address.",Toast.LENGTH_LONG,true).show();}

                    } else {
                        Toasty.warning(Forget_Password.this, "Please enter the email id.", Toast.LENGTH_LONG, true).show();
                    }
                }

        });
    }

    public void forgot_pass_url(){

        pd.show();

        String url = WebserviceUrl.customerForgotPassword;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("store_name",WebserviceUrl.Storename);
        params.put("email",input_email.getText().toString());





        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "frag_home- " + response.toString());
                    try {

                        JSONObject result = response.getJSONObject("result");

                        int status = result.getInt("status");
                        String key = result.getString("key");
                        String message = result.getString("message");

                        if (status == 1) {


                                Toasty.success(Forget_Password.this, "OTP send to your registered email id.", Toast.LENGTH_SHORT, true).show();
                                Intent intent = new Intent(Forget_Password.this, Reset_Password.class);
                                startActivity(intent);
                                finish();
                                pd.dismiss();


                        }else{


                            Toasty.error(Forget_Password.this, message, Toast.LENGTH_LONG, true).show();
                            pd.dismiss();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();
    }


}
