package com.sketch.developer.gowireless.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.Shared_Prefrence;
import com.sketch.developer.gowireless.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.provider.Settings.Secure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 2/14/18.
 */
public class LoginScreen extends AppCompatActivity{

    TextView tv_sign_up,tv_login,tv_forget_pass;
    String TAG = "login_screen";
    Global_Class globalClass;
    Shared_Prefrence prefrence;
    EditText input_email,input_password;
    ProgressDialog pd;
    String device_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        globalClass = (Global_Class)getApplicationContext();
        prefrence = new Shared_Prefrence(LoginScreen.this);
        prefrence.loadPrefrence();
        pd=new ProgressDialog(LoginScreen.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        device_id =Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        Log.d(TAG, "device_id: "+device_id);
        globalClass.setDeviceid(device_id);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_forget_pass = (TextView) findViewById(R.id.tv_forget_pass);

        if (globalClass.getLogin_status()){
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(intent);*/

                if(!input_email.getText().toString().isEmpty()){
                    if(!input_password.getText().toString().isEmpty()){

                        login_url();

                    }else {Toasty.warning(LoginScreen.this, "Please enter the password.", Toast.LENGTH_SHORT, true).show();}
                }else {Toasty.warning(LoginScreen.this, "Please enter the email.", Toast.LENGTH_SHORT, true).show();}


            }
        });
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, Signup.class);
                startActivity(intent);
            }
        });
        tv_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, Forget_Password.class);
                startActivity(intent);
            }
        });




    }


    public void login_url(){

        pd.show();

        String url = WebserviceUrl.login;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("store_name",WebserviceUrl.Storename);
        params.put("email",input_email.getText().toString());
        params.put("password",input_password.getText().toString());
        params.put("device_type","android");
        params.put("device_id",globalClass.getDeviceid());
        params.put("fcm_token","12345678");



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

                            JSONArray data_1 = result.getJSONArray("data");
                            for(int i = 0 ; i<data_1.length(); i++){

                                JSONObject jsonObject = data_1.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String fname = jsonObject.getString("fname");
                                String lname = jsonObject.getString("lname");
                                String phone = jsonObject.getString("phone");
                                String email = jsonObject.getString("email");
                                String image = jsonObject.getString("image");
                                String password = jsonObject.getString("password");

                                String name = fname+" "+lname;
                                globalClass.setName(fname);
                                globalClass.setId(id);
                                globalClass.setLogin_status(true);
                                globalClass.setPhone_number(phone);
                                globalClass.setEmail(email);
                                globalClass.setProfil_pic(image);
                                globalClass.setDeviceid(device_id);


                                prefrence.savePrefrence();

                                Log.d(TAG, "image: "+image);


                                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                pd.dismiss();

                            }


                        }else{

                            Toasty.error(LoginScreen.this, message, Toast.LENGTH_SHORT, true).show();
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
