package com.sketch.developer.gowireless.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by Developer on 7/17/17.
 */
public class Reset_Password extends AppCompatActivity {
    EditText input_otp, input_new_pass, input_renter_pass;
    TextView txtview_reset_pass;
    RelativeLayout rl_submit_btn;
    TextView submit_btn, txtview_note;
    String TAG = "reset";


    ProgressDialog pd;
    // Global_class global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.reset_password);


        pd=new ProgressDialog(Reset_Password.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        input_otp = findViewById(R.id.input_otp);
        input_new_pass = findViewById(R.id.input_new_pass);
        input_renter_pass = findViewById(R.id.input_renter_pass);
        txtview_reset_pass = findViewById(R.id.txtview_reset_pass);
        txtview_note = findViewById(R.id.txtview_note);
        submit_btn = findViewById(R.id.submit_btn);



        rl_submit_btn = findViewById(R.id.rl_submit_btn);


        rl_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* Intent intent = new Intent(Reset_Password.this,Login_Screen.class);
                startActivity(intent);
                finish();*/

                if (!input_otp.getText().toString().trim().isEmpty()) {
                    if (!input_new_pass.getText().toString().trim().isEmpty()) {
                        if (!input_renter_pass.getText().toString().trim().isEmpty()) {
                            if (input_new_pass.getText().toString().trim().equals(input_renter_pass.getText().toString().trim())) {
                                if (input_new_pass.getText().toString().length() >= 6) {
                                    reset_url();
                                } else {
                                    Toasty.error(Reset_Password.this, "Enter minimum 6 characters.", Toast.LENGTH_SHORT, true).show();
                                }
                            } else {
                                Toasty.error(Reset_Password.this, "Password mismatch", Toast.LENGTH_SHORT, true).show();
                            }
                        } else {
                            Toasty.warning(Reset_Password.this, "Please renter the new password.", Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.warning(Reset_Password.this, "Please enter the new password.", Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    Toasty.warning(Reset_Password.this, "Please enter the code.", Toast.LENGTH_SHORT, true).show();
                }

            }
        });


    }



    public void reset_url(){

        pd.show();

        String url = WebserviceUrl.customerResetPassword;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("store_name",WebserviceUrl.Storename);
        params.put("otp_code",input_otp.getText().toString());
        params.put("new_password",input_new_pass.getText().toString());

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
                        String message = result.getString("message");

                        if (status == 1) {



                                Toasty.success(Reset_Password.this, message, Toast.LENGTH_SHORT, true).show();
                                Intent intent = new Intent(Reset_Password.this, LoginScreen.class);
                                startActivity(intent);
                                finish();
                                pd.dismiss();

                        }else{
                            Toasty.error(Reset_Password.this, message, Toast.LENGTH_LONG, true).show();
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
