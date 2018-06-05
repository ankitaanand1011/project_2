package com.sketch.developer.gowireless.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.Shared_Prefrence;
import com.sketch.developer.gowireless.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 2/14/18.
 */
public class Signup extends AppCompatActivity {

    EditText input_user,input_email,input_password,input_con_password,input_mobile;
    CheckBox checkbox_terms;
    TextView tv_login;
    Global_Class globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    String TAG = "Signup";
    ImageView img_eye_1, img_eye_2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        globalClass = (Global_Class)getApplicationContext();
        prefrence = new Shared_Prefrence(Signup.this);
        pd=new ProgressDialog(Signup.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        input_user = findViewById(R.id.input_user);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_con_password = findViewById(R.id.input_con_password);
        input_mobile = findViewById(R.id.input_mobile);
        checkbox_terms = findViewById(R.id.checkbox_terms);
        tv_login = findViewById(R.id.tv_login);
        img_eye_1 = findViewById(R.id.img_eye_1);
        img_eye_2 = findViewById(R.id.img_eye_2);
        img_eye_1.setImageResource(R.mipmap.eye_show);
        img_eye_2.setImageResource(R.mipmap.eye_show);


        img_eye_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input_password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    //password is visible
                    img_eye_1.setImageResource(R.mipmap.viewoff96);
                    input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else if(input_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    //password is hidden
                    img_eye_1.setImageResource(R.mipmap.eye_show);
                    input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        img_eye_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input_con_password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    //password is visible
                    img_eye_2.setImageResource(R.mipmap.viewoff96);
                    input_con_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else if(input_con_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    //password is hidden
                    img_eye_2.setImageResource(R.mipmap.eye_show);
                    input_con_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!input_user.getText().toString().isEmpty()){
                    if(!input_email.getText().toString().isEmpty()) {
                        if (isValidEmail(input_email.getText().toString())){
                            if (!input_password.getText().toString().isEmpty()) {
                                if (!input_con_password.getText().toString().isEmpty()) {
                                    if (!input_mobile.getText().toString().isEmpty()) {
                                        if (checkbox_terms.isChecked()) {
                                            if (input_password.getText().toString().length() > 5) {
                                                if (input_password.getText().toString().matches(input_con_password.getText().toString())) {
                                                    registration_url();
                                                } else {Toasty.error(Signup.this, "Password mismatch.", Toast.LENGTH_SHORT, true).show();}
                                            } else {Toasty.info(Signup.this, "Minimum password length is 6.", Toast.LENGTH_SHORT, true).show();}
                                        } else {Toasty.info(Signup.this, "Please agree with the terms and conditions.", Toast.LENGTH_SHORT, true).show();}
                                    } else {Toasty.warning(Signup.this, "Please enter mobile number.", Toast.LENGTH_SHORT, true).show();}
                                } else {Toasty.warning(Signup.this, "Please enter confirm password.", Toast.LENGTH_SHORT, true).show();}
                            } else {Toasty.warning(Signup.this, "Please enter password.", Toast.LENGTH_SHORT, true).show();}
                        }else{ Toasty.warning(Signup.this, "Please enter a valid email.", Toast.LENGTH_SHORT, true).show();}
                    }else {Toasty.warning(Signup.this, "Please enter email.", Toast.LENGTH_SHORT, true).show();}
                }else {Toasty.warning(Signup.this, "Please enter username.", Toast.LENGTH_SHORT, true).show();}
            }
        });



    }
    public void registration_url(){

        pd.show();

        String url = WebserviceUrl.CustomerRegistration;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("store_name",WebserviceUrl.Storename);
        params.put("username",input_user.getText().toString());
        params.put("email",input_email.getText().toString());
        params.put("password",input_password.getText().toString());
        params.put("mobile",input_password.getText().toString());
        params.put("device_type","android");
        params.put("device_id","12345678");




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

                        if (status == 1) {

                            JSONArray data_1 = result.getJSONArray("data");
                            for(int i = 0 ; i<data_1.length(); i++){

                                JSONObject jsonObject = data_1.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String fname = jsonObject.getString("fname");
                                String lname = jsonObject.getString("lname");
                                String phone = jsonObject.getString("phone");
                                String email = jsonObject.getString("email");
                                String password = jsonObject.getString("password");

                             //   String name = fname+" "+lname;






                               // Log.d(TAG, "name: "+name);

                                Toasty.success(Signup.this, "Registration successful.", Toast.LENGTH_SHORT, true).show();
                                Intent intent = new Intent(Signup.this, LoginScreen.class);
                                startActivity(intent);
                                pd.dismiss();

                            }




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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
