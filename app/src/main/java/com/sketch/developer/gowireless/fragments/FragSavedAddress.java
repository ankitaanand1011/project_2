package com.sketch.developer.gowireless.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.activity.AddAddress;
import com.sketch.developer.gowireless.adapters.AdapterAddress;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.Shared_Prefrence;
import com.sketch.developer.gowireless.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Developer on 2/15/18.
 */

public class FragSavedAddress extends Fragment {

    Global_Class globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    String TAG = "my_address";

    ListView list_address;
    AdapterAddress adapterAddress;
    RelativeLayout rl_add_address;
    ArrayList<HashMap<String, String>> ArrayList_ShippingAddress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.frag_saved_address, container, false);


        globalClass = (Global_Class)getActivity().getApplicationContext();
        prefrence = new Shared_Prefrence(getActivity());
        prefrence.loadPrefrence();
        pd=new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");



        list_address = view.findViewById(R.id.list_address);
        rl_add_address = view.findViewById(R.id.rl_add_address);
        ArrayList_ShippingAddress = new ArrayList<>();

        rl_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddAddress.class);
                getContext().startActivity(intent);
            }
        });
        Get_Shipping_Address();

        return  view;
    }


    public void Get_Shipping_Address(){


        pd.show();


        String url = WebserviceUrl.getCustomerShipAddress;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("store_name",WebserviceUrl.Storename);
        params.put("email", globalClass.getEmail());
        params.put("customer_id", globalClass.getId());

          Log.d(TAG , "getCustomerShipAddress- " + url);
          Log.d(TAG ,"getCustomerShipAddress- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                   Log.d(TAG, "getCustomerShipAddress- " + response.toString());
                if (response != null) {
                    try {


                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");

                        if (status == 0){

                            // Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_LONG).show();


                        }else if (status == 1){

                            ArrayList_ShippingAddress.clear();

                            JSONArray data = result.getJSONArray("data");

                            int l = data.length();
                            Log.d(TAG, "length > "+l);
                            for (int i = 0; i < data.length(); i++){
                                JSONObject d = data.getJSONObject(i);

                                String id = d.optString("id");
                                String address = d.optString("address");
                                String country = d.optString("country");
                                String state = d.optString("state");
                                String city = d.optString("city");
                                String zip = d.optString("zip");
                                String phone = d.optString("phone");
                                String countryname = d.optString("countryname");
                                String statename = d.optString("statename");
                                String cityname = d.optString("cityname");


                                HashMap<String, String> map_address = new HashMap<String, String>();

                                map_address.put("id", id);
                                map_address.put("address", address);
                                map_address.put("cityname", cityname);
                                map_address.put("statename", statename);
                                map_address.put("countryname", countryname);
                                map_address.put("zip", zip);
                                map_address.put("phone", phone);


                                ArrayList_ShippingAddress.add(map_address);

                            }


                            adapterAddress = new AdapterAddress(getActivity(), ArrayList_ShippingAddress);
                            list_address.setAdapter(adapterAddress);
                            adapterAddress.notifyDataSetChanged();


                        }

                        pd.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "getCustomerShipAddress- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(getActivity()).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });




    }


    @Override
    public void onResume() {
        super.onResume();

        Get_Shipping_Address();
    }
}
