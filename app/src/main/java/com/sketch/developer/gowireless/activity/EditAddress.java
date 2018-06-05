package com.sketch.developer.gowireless.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 3/12/18.
 */

public class EditAddress extends AppCompatActivity {

    String TAG = "add_address";
    ImageView img_back;
    EditText input_firstname,input_lastname,input_address,input_mobile,input_zip;
    TextView tv_submit;
    Global_Class globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    AutoCompleteTextView ac_city,ac_state,ac_country;
    List<String> array_country_id = new ArrayList<>();
    List<String> array_country_name = new ArrayList<>();

    List<String> array_state_id = new ArrayList<>();
    List<String> array_state_name = new ArrayList<>();

    List<String> array_city_id = new ArrayList<>();
    List<String> array_city_name = new ArrayList<>();

    String country_idd = "",state_idd = "",city_idd = "";
    String id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaddress);

        globalClass = (Global_Class)getApplicationContext();
        prefrence = new Shared_Prefrence(EditAddress.this);
        pd=new ProgressDialog(EditAddress.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");


        Intent intent = getIntent();
        id = intent.getStringExtra("id");


        img_back = findViewById(R.id.img_back);
        input_firstname = findViewById(R.id.input_firstname);
        input_lastname = findViewById(R.id.input_lastname);
        input_address = findViewById(R.id.input_address);
        input_mobile = findViewById(R.id.input_mobile);
        input_zip = findViewById(R.id.input_zip);
        ac_city = findViewById(R.id.ac_city);
        ac_state = findViewById(R.id.ac_state);
        ac_country = findViewById(R.id.ac_country);
        tv_submit = findViewById(R.id.tv_submit);


        input_address.setText(globalClass.getAddress());
        input_mobile.setText(globalClass.getPhone());
        input_zip.setText(globalClass.getZip());
     /*   ac_city.setText(globalClass.getCityname());
        ac_state.setText(globalClass.getStatename());
        ac_country.setText(globalClass.getCountryname());*/



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        GetCountry();

        ac_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String country_name = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < array_country_name.size(); i++){
                    if (country_name.equals(array_country_name.get(i))){

                        country_idd = array_country_id.get(i);
                        GetState(country_idd);

                    }
                }
            }
        });

        ac_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String state_name = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < array_state_name.size(); i++){
                    if (state_name.equals(array_state_name.get(i))){

                        state_idd = array_state_id.get(i);
                        GetCity(state_idd);


                    }
                }
            }
        });

        ac_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String city_name = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < array_city_name.size(); i++){
                    if (city_name.equals(array_city_name.get(i))){

                        city_idd = array_city_id.get(i);

                    }
                }
            }
        });


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (globalClass.isNetworkAvailable()) {
                    if (!input_address.getText().toString().isEmpty()) {
                        if (!input_mobile.getText().toString().isEmpty()) {
                            if (!input_zip.getText().toString().isEmpty()) {
                                if (!country_idd.isEmpty()) {
                                    if (!state_idd.isEmpty()) {
                                        if (!city_idd.isEmpty()) {

                                            editCustomerShipAddress(input_address.getText().toString(),
                                                    input_zip.getText().toString(), input_mobile.getText().toString());

                                        } else {Toasty.warning(EditAddress.this, "Please enter city name.", Toast.LENGTH_SHORT, true).show();}
                                    } else {Toasty.warning(EditAddress.this, "Please enter state name.", Toast.LENGTH_SHORT, true).show();}
                                } else {Toasty.warning(EditAddress.this, "Please enter country name.", Toast.LENGTH_SHORT, true).show();}
                            } else {Toasty.warning(EditAddress.this, "Please enter zip code.", Toast.LENGTH_SHORT, true).show();}
                        } else {Toasty.warning(EditAddress.this, "Please enter mobile number.", Toast.LENGTH_SHORT, true).show();}
                    } else {Toasty.warning(EditAddress.this, "Please enter address.", Toast.LENGTH_SHORT, true).show();}
                } else {Toasty.info(EditAddress.this, "Check your internet connection", Toast.LENGTH_SHORT, true).show();}


            }
        });



    }

    public void GetCountry(){


        pd.show();

        array_country_id.clear();
        array_country_name.clear();

        String url = WebserviceUrl.getCountryList;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("email", globalClass.getEmail());
        params.put("store_name",WebserviceUrl.Storename);

        //   Log.d(global.TAG , "getCountryList- " + url);
        //   Log.d(global.TAG ,"getCountryList- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onRetry(int retryNo) {
                // Request was retried
                super.onRetry(retryNo);
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ///   Log.d(global.TAG, "getCountryList- " + response.toString());
                if (response != null) {
                    try {


                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");

                        if (status == 0){

                            // Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_LONG).show();


                        }else if (status == 1){

                            JSONArray data = result.getJSONArray("data");

                            int l = data.length();
                            Log.d(TAG, "length > "+l);
                            for (int i = 0; i < data.length(); i++){
                                JSONObject d = data.getJSONObject(i);

                                String id = d.optString("id");
                                String name = d.optString("name");

                                array_country_id.add(id);
                                array_country_name.add(name);

                            }


                            ac_country.setThreshold(1);
                            ac_country.setTextColor(Color.BLACK);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (EditAddress.this,android.R.layout.simple_list_item_1, array_country_name);

                            ac_country.setAdapter(adapter);


                        }

                        pd.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "getCountryList- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(EditAddress.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }

    // get state ..........
    public void GetState(String country_id){


        pd.show();

        array_state_id.clear();
        array_state_name.clear();

        String url = WebserviceUrl.getStateList;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("email", globalClass.getEmail());
        params.put("country_id", country_id);
        params.put("store_name", WebserviceUrl.Storename);

        //  Log.d(global.TAG , "getStateList- " + url);
        //   Log.d(global.TAG ,"getStateList- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //   Log.d(global.TAG, "getStateList- " + response.toString());
                if (response != null) {
                    try {


                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");

                        if (status == 0){

                            // Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_LONG).show();


                        }else if (status == 1){

                            JSONArray data = result.getJSONArray("data");

                            int l = data.length();
                            Log.d(TAG, "length > "+l);
                            for (int i = 0; i < data.length(); i++){
                                JSONObject d = data.getJSONObject(i);

                                String id = d.optString("id");
                                String name = d.optString("name");

                                array_state_id.add(id);
                                array_state_name.add(name);

                            }


                            ac_state.setThreshold(1);
                            ac_state.setTextColor(Color.BLACK);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (EditAddress.this,android.R.layout.simple_list_item_1, array_state_name);

                            ac_state.setAdapter(adapter);


                        }

                        pd.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "getStateList- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(EditAddress.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }

    //// get city .............
    public void GetCity(String state_id){


        pd.show();

        array_city_id.clear();
        array_city_name.clear();

        String url = WebserviceUrl.getCityList;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("store_name", WebserviceUrl.Storename);
        params.put("email", globalClass.getEmail());
        params.put("state_id", state_id);


        //  Log.d(global.TAG , "getCityList- " + url);
        //  Log.d(global.TAG ,"getCityList- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //   Log.d(global.TAG, "getCityList- " + response.toString());
                if (response != null) {
                    try {


                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");

                        if (status == 0){

                            // Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_LONG).show();


                        }else if (status == 1){

                            JSONArray data = result.getJSONArray("data");

                            int l = data.length();
                            Log.d(TAG, "length > "+l);
                            for (int i = 0; i < data.length(); i++){
                                JSONObject d = data.getJSONObject(i);

                                String id = d.optString("id");
                                String name = d.optString("name");

                                array_city_id.add(id);
                                array_city_name.add(name);

                            }


                            ac_city.setThreshold(1);
                            ac_city.setTextColor(Color.BLACK);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (EditAddress.this,android.R.layout.simple_list_item_1, array_city_name);

                            ac_city.setAdapter(adapter);


                        }

                        pd.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "getCityList- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(EditAddress.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }

    public void editCustomerShipAddress(String street, String zipcode, String mobile){


        pd.show();

        array_city_id.clear();
        array_city_name.clear();

        String url = WebserviceUrl.editCustomerShipAddress;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("store_name", WebserviceUrl.Storename);
        params.put("address_id", id);
        params.put("user_id", globalClass.getId());
        params.put("address", street);
        params.put("country_id", country_idd);
        params.put("state_id", state_idd);
        params.put("city_id", city_idd);
        params.put("zip_code", zipcode);
        params.put("phone", mobile);



        Log.d(TAG , "editCustomerShipAddress- " + url);
        Log.d(TAG ,"editCustomerShipAddress- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "editCustomerShipAddress- " + response.toString());
                if (response != null) {
                    try {


                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");
                        String message = result.optString("message");

                        if (status == 0){

                            Toasty.error(EditAddress.this, ""+message, Toast.LENGTH_SHORT, true).show();
                        }else if (status == 1){

                            //Toast.makeText(AddAddress.this, "Added", Toast.LENGTH_LONG).show();
                            Toasty.success(EditAddress.this, ""+message, Toast.LENGTH_SHORT, true).show();
                            finish();
                        }

                        pd.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d("TAG", "editCustomerShipAddress- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(EditAddress.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }







}
