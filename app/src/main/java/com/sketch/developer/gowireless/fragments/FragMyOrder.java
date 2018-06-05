package com.sketch.developer.gowireless.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.adapters.AdapterMyOrder;
import com.sketch.developer.gowireless.utils.Global_Class;
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

public class FragMyOrder extends Fragment {

    ListView my_order_list;
    AdapterMyOrder adapterMyOrder;
    ArrayList my_order_array_list;
    Global_Class globalClass;
    ProgressDialog pd;
    String TAG = "order_history";
    ArrayList<HashMap<String, String>> Arrayliat_Order;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.frag_my_order, container, false);

        globalClass = (Global_Class)getActivity().getApplicationContext();

        pd=new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");


        my_order_list = view.findViewById(R.id.my_order_list);
        Arrayliat_Order = new ArrayList();

        Get_Order();


        return  view;
    }

    public void Get_Order(){


        pd.show();


        String url = WebserviceUrl.getOrderList;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("store_name",WebserviceUrl.Storename);



        //   params.put("email", global.getEmail());



        ///   Log.d(global.TAG , "getOrderList- " + url);
        //   Log.d(global.TAG ,"getOrderList- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //     Log.d(global.TAG, "getOrderList- " + response.toString());
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
                                String users_id = d.optString("users_id");
                                String order_number = d.optString("order_number");
                                String fname = d.optString("fname");
                                String lname = d.optString("lname");
                                String grand_total = d.optString("grand_total");
                                String order_date = d.optString("order_date");
                                String payment_method = d.optString("payment_method");
                                String delivery_status = d.optString("delivery_status");


                                HashMap<String, String> map_address = new HashMap<String, String>();


                                map_address.put("id", id);
                                map_address.put("order_number", order_number);
                                map_address.put("fname", fname);
                                map_address.put("lname", lname);
                                map_address.put("grand_total", grand_total);
                                map_address.put("order_date", order_date);
                                map_address.put("payment_method", payment_method);
                                map_address.put("delivery_status", delivery_status);

                                if (users_id.equals(globalClass.getId())) {
                                    Arrayliat_Order.add(map_address);
                                }

                            }

                            adapterMyOrder = new AdapterMyOrder(getActivity(), Arrayliat_Order);
                            my_order_list.setAdapter(adapterMyOrder);


                          //  filter = frag_order_listAdapter.getFilter();

                        }

                        pd.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "getOrderList- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(getActivity()).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }
}
