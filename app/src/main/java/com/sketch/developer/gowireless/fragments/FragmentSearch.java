package com.sketch.developer.gowireless.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.adapters.ProductRecyclerViewAdapter;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 2/15/18.
 */

public class FragmentSearch extends Fragment{

    EditText edt_search_product;
    Global_Class global_class;
    RecyclerView rvproducts;
    ProgressDialog pd;
    String TAG = "search";
    ArrayList<HashMap<String, String>> list_product;
    ArrayList<String> data_arraylist;
    ProductRecyclerViewAdapter adapter;
    ArrayList<HashMap<String, String>> ArrayList_Product;
    ArrayList<HashMap<String, String>> ArrayList_Attributes;
    ArrayList<HashMap<String, String>> ArrayList_Available_Combination;
    ArrayList<HashMap<String, String>> ArrayList_Offer;
    ArrayList<HashMap<String, String>> ArrayList_Combination_Offer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        global_class = (Global_Class)getActivity().getApplicationContext();
        pd=new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

       // list_product = new ArrayList<>();
        data_arraylist = new ArrayList<>();
        ArrayList_Product = new ArrayList<>();
        ArrayList_Attributes = new ArrayList<>();
        ArrayList_Available_Combination = new ArrayList<>();
        ArrayList_Offer = new ArrayList<>();
        ArrayList_Combination_Offer = new ArrayList<>();


        edt_search_product = view.findViewById(R.id.edt_search_product);
        rvproducts = (RecyclerView) view.findViewById(R.id.rvproducts);
        int numberOfColumns = 2;
        rvproducts.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        edt_search_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >=1 ){

                    if (global_class.isNetworkAvailable()) {

                        searchProductByTerm_url(s.toString());

                    }else {

                        Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
                    }

                }

                   /* final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);*/

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


        });
        return  view;
    }


    public void searchProductByTerm_url(String keyword){

       // pd.show();

        String url = WebserviceUrl.searchProductByTerm;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("store_name",WebserviceUrl.Storename);
        params.put("search_term", keyword);





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

                        JSONArray data = result.getJSONArray("data");
                        int l1 = data.length();
                        Log.d(TAG, "data length- " + l1);

                        if(l1>0){
                            for (int i = 0; i < data.length(); i++) {

                                JSONObject obj_data = data.getJSONObject(i);

                                String id = obj_data.optString("id");
                                String name = obj_data.optString("name");
                                String catid = obj_data.optString("catid");
                                String stock = obj_data.optString("available_qnty");
                                String price = obj_data.optString("price");
                                String special_price = obj_data.optString("minimum_price");
                                String is_attr_exist = obj_data.optString("is_attr_exist");
                                String imagename = obj_data.optString("imagename");
                                String unit_name = obj_data.optString("unit_name");
                                String imagename_thumb = obj_data.optString("imagename_thumb");

                                String final_price = obj_data.optString("final_price");


                                HashMap<String, String> map_product = new HashMap<String, String>();

                                map_product.put("id", id);
                                map_product.put("name", name);
                                map_product.put("catid", catid);
                                map_product.put("stock", stock);
                                map_product.put("price", price);
                                map_product.put("special_price", special_price);
                                map_product.put("is_attr_exist", is_attr_exist);
                                map_product.put("imagename", imagename);
                                map_product.put("unit_name", unit_name);
                                map_product.put("imagename_thumb", imagename_thumb);

                                map_product.put("final_price", final_price);


                                JSONArray all_discount_dtls_main = obj_data.getJSONArray("all_discount_dtls");
                                if (all_discount_dtls_main.length() <= 0){
                                    map_product.put("is_discount", "N");
                                }else {
                                    map_product.put("is_discount", "Y");
                                }

                                ArrayList_Product.add(map_product);

                                // check attributes is or not........
                                if (is_attr_exist.equals("N")) {

                                    JSONArray all_discount_dtls = obj_data.getJSONArray("all_discount_dtls");
                                    int lp = all_discount_dtls.length();
                                    Log.d(TAG, "P all_discount length- " + lp);
                                    for (int a = 0; a < all_discount_dtls.length(); a++) {
                                        JSONObject obj_offer = all_discount_dtls.getJSONObject(a);

                                        String discount_name = obj_offer.optString("discount_name");
                                        String discount_amount = obj_offer.optString("discount_amount");
                                        String discount_type = obj_offer.optString("discount_type");

                                        HashMap<String, String> map_offer = new HashMap<String, String>();
                                        map_offer.put("discount_name", discount_name);
                                        map_offer.put("discount_amount", discount_amount);
                                        map_offer.put("discount_type", discount_type);
                                        map_offer.put("pid", id);

                                        ArrayList_Offer.add(map_offer);

                                    }

                                }


                                JSONArray attributes_data = obj_data.getJSONArray("attributes_data");
                                int l2 = attributes_data.length();
                                Log.d(TAG, "attributes_data length- " + l2);
                                for (int j = 0; j < attributes_data.length(); j++) {

                                    JSONObject obj_attributes = attributes_data.getJSONObject(j);

                                    String attr_id = obj_attributes.optString("attr_id");
                                    String attr_name = obj_attributes.optString("attr_name");
                                    String attr_child = obj_attributes.optString("attr_child");

                                    HashMap<String, String> map_attr = new HashMap<String, String>();

                                    map_attr.put("pid", id);
                                    map_attr.put("attr_id", attr_id);
                                    map_attr.put("attr_name", attr_name);
                                    map_attr.put("attr_child", attr_child);


                                    ArrayList_Attributes.add(map_attr);


                                }


                                JSONArray available_combination = obj_data.getJSONArray("available_combination");
                                int l3 = available_combination.length();
                                Log.d(TAG, "available_combination length- " + l3);
                                for (int k = 0; k < available_combination.length(); k++) {

                                    JSONObject obj_available_combination = available_combination.getJSONObject(k);

                                    String combination_id = obj_available_combination.optString("id");
                                    String pid = obj_available_combination.optString("pid");
                                    String stockdet = obj_available_combination.optString("stockdet");
                                    String c_price = obj_available_combination.optString("price");
                                    String spcl_price = obj_available_combination.optString("spcl_price");
                                    String available_qnty = obj_available_combination.optString("available_qnty");

                                    String image_combination = obj_available_combination.optString("imagename");
                                    String imagename_thumb_1 = obj_available_combination.optString("imagename_thumb");


                                    HashMap<String, String> map_attr = new HashMap<String, String>();

                                    map_attr.put("id", combination_id);
                                    map_attr.put("pid", pid);
                                    map_attr.put("combination", stockdet);
                                    map_attr.put("price", c_price);
                                    map_attr.put("spcl_price", spcl_price);
                                    map_attr.put("available_qnty", available_qnty);

                                    map_attr.put("image_combination", image_combination);
                                    map_attr.put("imagename_thumb", imagename_thumb_1);


                                    ArrayList_Available_Combination.add(map_attr);


                                    JSONArray all_discount_dtls = obj_available_combination.getJSONArray("all_discount_dtls");
                                    int lp = all_discount_dtls.length();
                                    Log.d(TAG, "all_discount length- " + lp);
                                    for (int a = 0; a < all_discount_dtls.length(); a++) {
                                        JSONObject obj_offer = all_discount_dtls.getJSONObject(a);

                                        String discount_name = obj_offer.optString("discount_name");
                                        String discount_amount = obj_offer.optString("discount_amount");
                                        String discount_type = obj_offer.optString("discount_type");

                                        HashMap<String, String> map_offer = new HashMap<String, String>();
                                        map_offer.put("discount_name", discount_name);
                                        map_offer.put("discount_amount", discount_amount);
                                        map_offer.put("discount_type", discount_type);
                                        map_offer.put("pid", pid);
                                        map_offer.put("combination_id", combination_id);

                                        ArrayList_Combination_Offer.add(map_offer);

                                    }


                                }

                                // list_product.add(hashMap);
                                data_arraylist.add(obj_data.toString());

                            }


                        }else {
                            Toasty.error(getActivity(),"No Product Available.",Toast.LENGTH_LONG,true).show();
                        }


                        adapter = new ProductRecyclerViewAdapter(getActivity(), ArrayList_Product,
                                ArrayList_Attributes, ArrayList_Available_Combination, ArrayList_Offer,
                                ArrayList_Combination_Offer,data_arraylist);

                        rvproducts.setAdapter(adapter);
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
