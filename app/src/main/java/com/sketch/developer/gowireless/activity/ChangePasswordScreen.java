package com.sketch.developer.gowireless.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;


/**
 * Created by Developer on 8/21/17.
 */
public class ChangePasswordScreen extends AppCompatActivity {


    EditText new_password,old_password;
    RelativeLayout rl_submit;
    Global_Class global_class;
    ProgressDialog pd;
    String TAG = "change_password";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);


        global_class = (Global_Class)getApplicationContext();

        pd=new ProgressDialog(ChangePasswordScreen.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");


        old_password = findViewById(R.id.input_old_pass);
        new_password = findViewById(R.id.input_new_pass);
        rl_submit =  findViewById(R.id.rl_submit);


        rl_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (global_class.isNetworkAvailable()) {
                    if (!old_password.getText().toString().trim().isEmpty()) {
                        if (!new_password.getText().toString().trim().isEmpty()) {
                            if (new_password.getText().toString().length() >= 6) {
                                customerChangePassword_url();
                            }else {Toasty.error(ChangePasswordScreen.this, "Enter minimum 6 characters.", Toast.LENGTH_SHORT, true).show();}
                        } else {Toasty.warning(ChangePasswordScreen.this, "Please enter the new password.", Toast.LENGTH_SHORT, true).show();}
                    }else {Toasty.warning(ChangePasswordScreen.this, "Please enter the old password.", Toast.LENGTH_SHORT, true).show();          }
                }else{Toasty.info(ChangePasswordScreen.this,"Check your internet connection.",Toast.LENGTH_LONG,true).show();}
            }
        });





    }




    public void customerChangePassword_url(){

        pd.show();

        String url = WebserviceUrl.customerChangePassword;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("store_name",WebserviceUrl.Storename);
        params.put("old_password",old_password.getText().toString());
        params.put("new_password",new_password.getText().toString());
        params.put("user_id",global_class.getId());




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

                            Toasty.success(ChangePasswordScreen.this,message,Toast.LENGTH_LONG,true).show();
                            finish();


                        }else {
                            Toasty.error(ChangePasswordScreen.this,message,Toast.LENGTH_LONG,true).show();
                        }
                        pd.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                pd.dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });


    }
}
