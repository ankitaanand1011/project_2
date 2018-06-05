package com.sketch.developer.gowireless.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.activity.Payment;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class AdapterCartList extends BaseAdapter{
     Context context;

    ArrayList<HashMap<String, String>> cart_list_array = new ArrayList<>();
    ArrayList<HashMap<String, String>> cart_offer_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> cart_tax_list = new ArrayList<>();
    TextView tv_order_place;
    TextView tv_product_price;

    Global_Class global;
    ProgressDialog mProgressDialog;
    ImageLoader loader;
    String img_path;
    Boolean is_delete = false;
    TextView integer_number;
    int minteger = 0;

    String product_quantity_;
    String product_id_;
    String stock_id_;


    int p_qty = 0;
    double p_price = 0.00;
    double total_discount_P = 0.00;
    double total_discount_A = 0.00;
    double total_tax = 0.00;

    String TAG = "cart_adapter";

    ImageView empty_cart_iv;
    RelativeLayout rl_1;

    public AdapterCartList(Context context,ArrayList<HashMap<String, String>> cart_list_array,
                           ArrayList<HashMap<String, String>> cart_offer_list,
                           ArrayList<HashMap<String, String>> cart_tax_list ,
                           TextView tv_order_place, ProgressDialog mProgressDialog, RelativeLayout rl_1,
                           ImageView empty_cart_iv) {

        this.context = context;
        this.cart_list_array = cart_list_array;
        this.cart_offer_list = cart_offer_list;
        this.cart_tax_list = cart_tax_list;
        this.tv_order_place = tv_order_place;
        this.mProgressDialog = mProgressDialog;
        this.rl_1 = rl_1;
        this.empty_cart_iv = empty_cart_iv;

        global = (Global_Class)context.getApplicationContext();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v= convertView;
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.cart_lv_single_row, null, true);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();

        String name =  cart_list_array.get(position).get("product_name");
        String final_price =  cart_list_array.get(position).get("final_price");
        String price = cart_list_array.get(position).get("price");

        Log.d("lkio", "getView:final_price>  "+final_price);
        Log.d("lkio", "getView:price>  "+price);

        ImageView img = (ImageView) listViewItem.findViewById(R.id.img);
        TextView tv_product_name = (TextView) listViewItem.findViewById(R.id.tv_product_name);
        integer_number = (TextView) listViewItem.findViewById(R.id.integer_number);
         tv_product_price = (TextView) listViewItem.findViewById(R.id.tv_product_price);
        TextView tv_all_tax = (TextView) listViewItem.findViewById(R.id.tv_all_tax);





       // tv_product_price_before.setPaintFlags(tv_product_price_before.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv_product_name.setText(name);
        integer_number.setText(cart_list_array.get(position).get("cart_quantity"));



        p_qty = Integer.parseInt(cart_list_array.get(position).get("cart_quantity"));

        String card_dtls_id = cart_list_array.get(position).get("card_dtls_id");

        ArrayList<HashMap<String, String>> filter_offer_list = new ArrayList<>();

        for (int i = 0; i < cart_offer_list.size(); i++){
            if (card_dtls_id.equals(cart_offer_list.get(i).get("card_dtls_id"))){

                filter_offer_list.add(cart_offer_list.get(i));
            }
        }


        if (filter_offer_list.size() != 0){

            tv_product_price.setText(global.getCurrency_Symbol()+" "+cart_list_array.get(position).get("price")+"");

            p_price = Float.parseFloat(cart_list_array.get(position).get("price"));

        }else {

            if (cart_list_array.get(position).get("special_price").equals("0.00")){

                tv_product_price.setText(global.getCurrency_Symbol()+" "+cart_list_array.get(position).get("price"));


                p_price = Float.parseFloat(cart_list_array.get(position).get("price"));

            }else {


                tv_product_price.setText(global.getCurrency_Symbol()+" "+cart_list_array.get(position).get("special_price"));

                p_price = Float.parseFloat(cart_list_array.get(position).get("special_price"));
            }


        }
        total_discount_A = 0.00;
        total_discount_P = 0.00;



        for(int x = 0; x < filter_offer_list.size() ; x++)
        {

            String discount_name = filter_offer_list.get(x).get("discount_name");
            String discount_amount = filter_offer_list.get(x).get("discount_amount");
            String discount_type = filter_offer_list.get(x).get("discount_type");

            if (discount_type.equals("A")){

               // tv_offer.setText(discount_name+" "+global.getCurrency_Symbol()+" "+discount_amount);

                total_discount_A = total_discount_A + Float.parseFloat(discount_amount);

            }else {

                total_discount_P = total_discount_P + Float.parseFloat(discount_amount);

               // tv_offer.setText(discount_name+"  "+discount_amount+"%");

            }

        }

        total_tax = 0.00;

        ArrayList<HashMap<String, String>> filter_tax_list = new ArrayList<>();

        for (int i = 0; i < cart_tax_list.size(); i++){
            if (cart_list_array.get(position).get("card_dtls_id").equals(cart_tax_list.get(i).get("card_dtls_id"))){

                filter_tax_list.add(cart_tax_list.get(i));
            }
        }

        for(int x = 0; x < filter_tax_list.size() ; x++)
        {


            String tax_name = filter_tax_list.get(x).get("tax_name");
            String tax_rate = filter_tax_list.get(x).get("tax_rate");


           // tv_tax.setText(tax_name+"  "+tax_rate+"%");

            total_tax = total_tax + Float.parseFloat(tax_rate);
        }









        /*if (price.isEmpty()){
                tv_product_price.setText(global.getCurrency_Symbol()+" "+
                        cart_list_array.get(position).get("special_price"));

            }else{

                tv_product_price.setText(global.getCurrency_Symbol()+" "+
                        cart_list_array.get(position).get("price"));

            }*/
        if(cart_list_array.get(position).get("is_attr_exist").equals("N")){
            img_path = cart_list_array.get(position).get("image_path_thumb")+ "/"+
                    cart_list_array.get(position).get("product_main_img");



        }else{

             img_path = cart_list_array.get(position).get("image_path_thumb")+ "/"+
                    cart_list_array.get(position).get("attriblute_image");

        }


        loader.displayImage(img_path, img , defaultOptions);















        //tv_ptv_order_placeroduct_price.setText(cart_list_array.get(position).get("price"));
        tv_order_place.setVisibility(View.VISIBLE);
        tv_order_place.setText("Place this Order : "+global.getCurrency_Symbol()+" "+String.format("%.2f",global.getGRAND_TOTAL()));
        tv_order_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(global.getSELECTED_SHIPPING_ADDRESS_ID().isEmpty()){
                    dialogforaddress();
                }else{
                    Place_Order();
                }

            }
        });


        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //is_delete = true;
                final String card_dtls_id = cart_list_array.get(position).get("card_dtls_id");

                alet_dia(card_dtls_id);

            }

        });


        integer_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                // dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                View dialogView = inflater.inflate(R.layout.m_cart_quantity_change_dialog, null);
                dialogBuilder.setCancelable(false);
                dialogBuilder.setView(dialogView);

                final AlertDialog dialog = dialogBuilder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                // dialog.getWindow().setLayout(600, 400);
                dialog.show();


                ImageView cart_minus_img = dialogView.findViewById(R.id.cart_minus_img);
                ImageView cart_plus_img = dialogView.findViewById(R.id.cart_plus_img);
                final TextView cart_product_quantity_tv = dialogView.findViewById(R.id.cart_product_quantity_tv);

                product_id_ = cart_list_array.get(position).get("product_id");
                stock_id_ = cart_list_array.get(position).get("stock_id");


                cart_product_quantity_tv.setText(cart_list_array.get(position).get("cart_quantity"));
                product_quantity_ = cart_product_quantity_tv.getText().toString();

                cart_minus_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int count = Integer.parseInt(cart_product_quantity_tv.getText().toString());
                        if (count >= 2){
                            count--;
                            cart_product_quantity_tv.setText(String.valueOf(count));

                            Log.d(TAG, "count  = "+count);

                            product_quantity_ = String.valueOf(count);

                        }

                    }
                });

                cart_plus_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int count = Integer.parseInt(cart_product_quantity_tv.getText().toString());

                        //  if (count < Integer.parseInt(cart_list_array.get(position).get("AVL_STOCK"))) {

                        count++;
                        cart_product_quantity_tv.setText(String.valueOf(count));

                        Log.d(TAG, "count  = " + count);

                        product_quantity_ = String.valueOf(count);

                        //  }

                    }
                });

                final TextView tv_cancel_dialog = dialogView.findViewById(R.id.tv_cancel_dialog);
                final TextView tv_ok = dialogView.findViewById(R.id.tv_ok);


                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //   Log.d(global.TAG, "product_id_  = "+product_id_);
                        //   Log.d(global.TAG, "stock_id_  = "+stock_id_);
                        //  Log.d(global.TAG, "product_quantity_  = "+product_quantity_);

                        if (!global.getId().equals("")){


                            Update_Cart_Item(global.getId(), product_id_, stock_id_ , product_quantity_);

                        }

                        dialog.dismiss();

                    }
                });


                tv_cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

            }
        });


        Calculation_Cart();


        return listViewItem;

    }

    private void display(int number) {

        integer_number.setText("" + number);
    }

    @Override
    public int getCount() {
        int count =0;
        if (cart_list_array != null) {
            count=cart_list_array.size();
        }
        return count;
    }

    @Override
    public Object getItem(int i) {
        return cart_list_array.size();
    }

    @Override
    public long getItemId(int i) {
        return cart_list_array.size();
    }


    public void Calculation_Cart(){

        double temp_total = p_price * p_qty;

        double dis_p_amm = temp_total * (total_discount_P/100);

        temp_total = temp_total - (dis_p_amm + total_discount_A);


        double tax_amm = temp_total * (total_tax/100);


        temp_total = temp_total + tax_amm;

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        tv_product_price.setText(global.getCurrency_Symbol()+" "+df.format(temp_total));

    }



    public void Place_Order( ){


        mProgressDialog.show();


        String url = WebserviceUrl.addOrder;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("store_name", WebserviceUrl.Storename);
        params.put("ship_id", global.getSELECTED_SHIPPING_ADDRESS_ID());
        params.put("subtotal", global.getCURRENT_TOTAL());
        params.put("grandtotal", (float) Math.round(global.getGRAND_TOTAL()));
        params.put("notes", "frgdgfhg");
        params.put("is_paid", "1");
        params.put("added_shipping_amount", "0");
        params.put("spl_disc_type", "");
        params.put("spl_discount_amount", "");
        params.put("spl_discount_val", "");
        params.put("device_id", global.getDeviceid());
        params.put("payment_method", "COD");
        params.put("txn_id", "");
        params.put("pos_user_id", "");
        params.put("pos_device_auto_id", "");
        params.put("transaction_info", "");
        params.put("customer_id", global.getId());



        Log.d("placed" ,"addOrder- " + url);
        Log.d("placed" ,"addOrder- " + params.toString());
        Log.d("placed" ,"global.getDeviceid()- " +global.getDeviceid());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                   Log.d("placed", "placed- " + response.toString());
                if (response != null) {
                    try {


                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");
                        String message = result.optString("message");

                        if (status == 0){

                            // Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_LONG).show();
                            Log.d("placed", "onSuccess: "+message);

                        }else if (status == 1){

                            global.Cart_List_Item.clear();

                            String data = result.optString("data");

                            global.setORDER_ID(data);



                            Intent intent = new Intent(context, Payment.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();




                        }

                        mProgressDialog.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                //  Log.d(global.TAG, "addOrder- " + res);

                mProgressDialog.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }


    public void dialogforaddress(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Please add the shipping address in profile first");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void Delete_Cart_Item(String card_dtls_id){

        mProgressDialog.show();

        String url = WebserviceUrl.productDeleteFromCart;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("store_name", WebserviceUrl.Storename);
        params.put("email", global.getEmail());
        params.put("card_dtls_id", card_dtls_id);


          Log.d(TAG , "productDeleteFromCart- " + url);
          Log.d(TAG , "productDeleteFromCart- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);

        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                   Log.d(TAG, "productDeleteFromCart- " + response.toString());
                if (response != null) {
                    try {

                        ArrayList<HashMap<String, String>> temp_list_cart = new ArrayList<HashMap<String, String>>();
                        ArrayList<HashMap<String, String>> temp_list_offer = new ArrayList<HashMap<String, String>>();
                        ArrayList<HashMap<String, String>> temp_list_tax = new ArrayList<HashMap<String, String>>();

                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");

                        if (status == 0){
                            empty_cart_iv.setVisibility(View.VISIBLE);
                            rl_1.setVisibility(View.GONE);

                            android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                            alert.setMessage("Some Error Occurred");
                            alert.show();

                        }else if (status == 1){

                            global.Cart_List_Item.clear();
                           // global.Cart_Offer.clear();

                            JSONObject obj_data = result.getJSONObject("data");

                            JSONArray cart_data = obj_data.getJSONArray("cart_data");
                            int l1 = cart_data.length();
                            global.setCart_size(l1);

                            if(cart_data.length()==0){

                                empty_cart_iv.setVisibility(View.VISIBLE);
                                rl_1.setVisibility(View.GONE);

                            }else{
                                rl_1.setVisibility(View.VISIBLE);

                                empty_cart_iv.setVisibility(View.GONE);
                            }



                            Log.d("kjhg", "cart_data length = "+l1);
                            for (int i = 0; i < cart_data.length(); i++){
                                JSONObject obj_cart_data = cart_data.getJSONObject(i);

                                String id = obj_cart_data.optString("id");
                                String card_dtls_id = obj_cart_data.optString("card_dtls_id");
                                String cart_id = obj_cart_data.optString("cart_id");
                                String product_id = obj_cart_data.optString("product_id");
                                String stock_id = obj_cart_data.optString("stock_id");
                                String cart_quantity = obj_cart_data.optString("cart_quantity");
                                String product_name = obj_cart_data.optString("product_name");
                                String stockdet = obj_cart_data.optString("stockdet");
                                String price = obj_cart_data.optString("price");
                                String special_price = obj_cart_data.optString("special_price");
                                String product_main_img = obj_cart_data.optString("product_main_img");
                                String image_path_thumb = obj_cart_data.optString("image_path_thumb");
                                String image_path = obj_cart_data.optString("image_path");
                                String attriblute_image = obj_cart_data.optString("attriblute_image");

                                String is_attr_exist = obj_cart_data.optString("is_attr_exist");

                                HashMap<String, String> map_cart = new HashMap<String, String>();

                                map_cart.put("id" , id);
                                map_cart.put("card_dtls_id" , card_dtls_id);
                                map_cart.put("cart_id" , cart_id);
                                map_cart.put("product_id" , product_id);
                                map_cart.put("stock_id" , stock_id);
                                map_cart.put("cart_quantity" , cart_quantity);
                                map_cart.put("product_name" , product_name);
                                map_cart.put("stockdet" , stockdet);
                                map_cart.put("special_price" , special_price);
                                map_cart.put("price" , price);
                                map_cart.put("is_attr_exist" , is_attr_exist);
                                map_cart.put("image_path_thumb" , image_path_thumb);
                                map_cart.put("image_path" , image_path);
                                map_cart.put("attriblute_image" , attriblute_image);
                                map_cart.put("product_main_img" , product_main_img);


                                temp_list_cart.add(map_cart);



                                JSONArray all_discount_dtls = obj_cart_data.getJSONArray("all_discount_dtls");
                                int l2 = all_discount_dtls.length();
                                Log.d("kjhg", "all_discount_dtls length = "+l2);
                                for (int j = 0; j < all_discount_dtls.length(); j++) {
                                    JSONObject obj_discount = all_discount_dtls.getJSONObject(j);

                                    String discount_name = obj_discount.optString("discount_name");
                                    String discount_amount = obj_discount.optString("discount_amount");
                                    String discount_type = obj_discount.optString("discount_type");


                                    HashMap<String, String> map_offer = new HashMap<String, String>();

                                    map_offer.put("discount_name" , discount_name);
                                    map_offer.put("discount_amount" , discount_amount);
                                    map_offer.put("discount_type" , discount_type);
                                    map_offer.put("card_dtls_id" , card_dtls_id);


                                    temp_list_offer.add(map_offer);

                                }


                                global.setCART_ID(cart_id);



                                JSONObject tax_details_data = obj_cart_data.getJSONObject("tax_details_data");

                                JSONArray tax_Details = tax_details_data.getJSONArray("tax_Details");
                                int l3 = tax_Details.length();
                                Log.d("kjhg", "tax_Details length = "+l3);
                                for (int j = 0; j < tax_Details.length(); j++) {
                                    JSONObject obj_tax_Details = tax_Details.getJSONObject(j);

                                    String tax_rate = obj_tax_Details.optString("tax_rate");
                                    String tax_name = obj_tax_Details.optString("tax_name");

                                    HashMap<String, String> map_tax = new HashMap<String, String>();

                                    map_tax.put("tax_rate" , tax_rate);
                                    map_tax.put("tax_name" , tax_name);
                                    map_tax.put("card_dtls_id" , card_dtls_id);


                                    temp_list_tax.add(map_tax);


                                }

                            }


                            double total_cart_amount = Double.parseDouble(obj_data.optString("total_cart_amount"));

                            Log.d("kjhg", "total_cart_amount = " + total_cart_amount);

                            global.setCURRENT_TOTAL(total_cart_amount);



                            String total_cart_shipcost = obj_data.optString("total_cart_shipcost");
                            global.setSHIPPINIG_CHARGES(total_cart_shipcost);

                            String cart_grand_total = obj_data.optString("cart_grand_total");
                            global.setGRAND_TOTAL(Double.parseDouble(cart_grand_total));


                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);
                            df.setMinimumFractionDigits(2);

                            tv_order_place.setText(global.getCurrency_Symbol()+" "
                                    +String.valueOf(String.format("%.2f",global.getCURRENT_TOTAL())));


                            if (!global.Discount_amount.equals("")){


                                if (global.Discount_Type.equals("A")){


                                    double discount_amount = Float.parseFloat(global.Discount_amount);

                                    Log.d("kjhg", "discount amount = " + discount_amount);

                                    double pay_amount = global.getCURRENT_TOTAL() - discount_amount;

                                    tv_order_place.setText(global.getCurrency_Symbol()+" "
                                            +String.valueOf(String.format("%.2f",pay_amount)));


                                }else if (global.Discount_Type.equals("P")){


                                    double discount = Float.parseFloat(global.Discount_amount);

                                    double discount_amount = global.getCURRENT_TOTAL() * (discount / 100);

                                    Log.d("kjhg", "discount amount = " + discount_amount);

                                    double pay_amount = global.getCURRENT_TOTAL() - discount_amount;

                                    tv_order_place.setText(global.getCurrency_Symbol()+" "+String.valueOf(df.format(pay_amount)));

                                }

                            }else {

                                tv_order_place.setText(global.getCurrency_Symbol()+" "+String.valueOf(String.format("%.2f",global.getCURRENT_TOTAL())));

                            }






                            // Log.d(global.TAG, "cart_list_array = "+cart_list_array);

                            cart_list_array.clear();

                            cart_list_array = temp_list_cart;


                            global.Cart_List_Item = temp_list_cart;


                          //  Log.d(global.TAG, "temp_list_cart length = "+temp_list_cart.size());

                           // tv_cart_number.setText(String.valueOf(global.getCart_size()));

                        }

                        mProgressDialog.dismiss();

/*
                        if(cart_list_array.get(position).get("is_attr_exist").equals("N")){
                            img_path = cart_list_array.get(position).get("image_path_thumb")+ "/"+
                                    cart_list_array.get(position).get("product_main_img");


                        }else{

                            img_path = cart_list_array.get(position).get("image_path_thumb")+ "/"+
                                    cart_list_array.get(position).get("attriblute_image");

                        }


                        loader.displayImage(img_path, img , defaultOptions);*/
                      //  is_delete =false;
                        notifyDataSetChanged();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d("kjhg", "productDeleteFromCart- " + res);

                mProgressDialog.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });





    }

    public  void  alet_dia(final String card_dtls_id){


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(R.string.app_name)
                // .setMessage("Are you sure you want to delete this order?")
                .setTitle("Are you sure you want to delete this order?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        Delete_Cart_Item(card_dtls_id);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(R.mipmap.delete96x96)
                .show();

    }

    public void Update_Cart_Item(String customer_id_ , String p_id, String stock_id, String qty ){


        mProgressDialog.show();


        String url = WebserviceUrl.productAddToCart;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("store_name", WebserviceUrl.Storename);
        params.put("email", global.getEmail());
        params.put("product_id", p_id);
        params.put("product_quantity", qty);
        params.put("stock_id", stock_id);
        params.put("customer_id", customer_id_);
        params.put("device_id", global.getDeviceid());

        //   Log.d(global.TAG , "productAddToCart- " + url);
        //   Log.d(global.TAG , "productAddToCart- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);

        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //   Log.d(global.TAG, "productAddToCart- " + response.toString());
                if (response != null) {
                    try {

                        ArrayList<HashMap<String, String>> temp_list_cart = new ArrayList<HashMap<String, String>>();
                        ArrayList<HashMap<String, String>> temp_list_offer = new ArrayList<HashMap<String, String>>();
                        ArrayList<HashMap<String, String>> temp_list_tax = new ArrayList<HashMap<String, String>>();

                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");

                        if (status == 0){


                            android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                            alert.setMessage("Some Error Occurred");
                            alert.show();

                        }else if (status == 1){

                            global.Cart_List_Item.clear();
                         //   global.Cart_Offer.clear();

                            JSONObject obj_data = result.getJSONObject("data");

                            JSONArray cart_data = obj_data.getJSONArray("cart_data");
                            int l1 = cart_data.length();
                          //  global.setCart_size(l1);



                            Log.d(TAG, "cart_data length = "+l1);
                            for (int i = 0; i < cart_data.length(); i++){
                                JSONObject obj_cart_data = cart_data.getJSONObject(i);

                                String id = obj_cart_data.optString("id");
                                String card_dtls_id = obj_cart_data.optString("card_dtls_id");
                                String cart_id = obj_cart_data.optString("cart_id");
                                String product_id = obj_cart_data.optString("product_id");
                                String stock_id = obj_cart_data.optString("stock_id");
                                String cart_quantity = obj_cart_data.optString("cart_quantity");
                                String product_name = obj_cart_data.optString("product_name");
                                String stockdet = obj_cart_data.optString("stockdet");
                                String price = obj_cart_data.optString("price");
                                String special_price = obj_cart_data.optString("special_price");
                                String product_main_img = obj_cart_data.optString("product_main_img");
                                String image_path_thumb = obj_cart_data.optString("image_path_thumb");
                                String image_path = obj_cart_data.optString("image_path");
                                String attriblute_image = obj_cart_data.optString("attriblute_image");

                                String is_attr_exist = obj_cart_data.optString("is_attr_exist");




                                HashMap<String, String> map_cart = new HashMap<String, String>();

                                map_cart.put("id" , id);
                                map_cart.put("card_dtls_id" , card_dtls_id);
                                map_cart.put("cart_id" , cart_id);
                                map_cart.put("product_id" , product_id);
                                map_cart.put("stock_id" , stock_id);
                                map_cart.put("cart_quantity" , cart_quantity);
                                map_cart.put("product_name" , product_name);
                                map_cart.put("stockdet" , stockdet);
                                map_cart.put("special_price" , special_price);
                                map_cart.put("price" , price);
                                map_cart.put("is_attr_exist" , is_attr_exist);
                                map_cart.put("image_path_thumb" , image_path_thumb);
                                map_cart.put("image_path" , image_path);
                                map_cart.put("attriblute_image" , attriblute_image);
                                map_cart.put("product_main_img" , product_main_img);

                                temp_list_cart.add(map_cart);



                                JSONArray all_discount_dtls = obj_cart_data.getJSONArray("all_discount_dtls");
                                int l2 = all_discount_dtls.length();
                                Log.d(TAG, "all_discount_dtls length = "+l2);
                                for (int j = 0; j < all_discount_dtls.length(); j++) {
                                    JSONObject obj_discount = all_discount_dtls.getJSONObject(j);

                                    String discount_name = obj_discount.optString("discount_name");
                                    String discount_amount = obj_discount.optString("discount_amount");
                                    String discount_type = obj_discount.optString("discount_type");


                                    HashMap<String, String> map_offer = new HashMap<String, String>();

                                    map_offer.put("discount_name" , discount_name);
                                    map_offer.put("discount_amount" , discount_amount);
                                    map_offer.put("discount_type" , discount_type);
                                    map_offer.put("card_dtls_id" , card_dtls_id);


                                    temp_list_offer.add(map_offer);

                                }

                                global.setCART_ID(cart_id);





                                JSONObject tax_details_data = obj_cart_data.getJSONObject("tax_details_data");

                                JSONArray tax_Details = tax_details_data.getJSONArray("tax_Details");
                                int l3 = tax_Details.length();
                                Log.d(TAG, "tax_Details length = "+l3);
                                for (int j = 0; j < tax_Details.length(); j++) {
                                    JSONObject obj_tax_Details = tax_Details.getJSONObject(j);

                                    String tax_rate = obj_tax_Details.optString("tax_rate");
                                    String tax_name = obj_tax_Details.optString("tax_name");

                                    HashMap<String, String> map_tax = new HashMap<String, String>();

                                    map_tax.put("tax_rate" , tax_rate);
                                    map_tax.put("tax_name" , tax_name);
                                    map_tax.put("card_dtls_id" , card_dtls_id);


                                    temp_list_tax.add(map_tax);


                                }








                            }



                            double total_cart_amount = Double.parseDouble(obj_data.optString("total_cart_amount"));

                            //  Log.d(global.TAG, "total_cart_amount = " + total_cart_amount);

                            global.setCURRENT_TOTAL(total_cart_amount);



                            String total_cart_shipcost = obj_data.optString("total_cart_shipcost");
                            global.setSHIPPINIG_CHARGES(total_cart_shipcost);

                            String cart_grand_total = obj_data.optString("cart_grand_total");
                            global.setGRAND_TOTAL(Double.parseDouble(cart_grand_total));


                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);
                            df.setMinimumFractionDigits(2);

                            tv_order_place.setText("Place this order : "+global.getCurrency_Symbol()+" "+String.valueOf(df.format(global.getCURRENT_TOTAL())));


                            if (!global.Discount_amount.equals("")){


                                if (global.Discount_Type.equals("A")){


                                    double discount_amount = Float.parseFloat(global.Discount_amount);

                                    Log.d(TAG, "discount amount = " + discount_amount);

                                    double pay_amount = global.getCURRENT_TOTAL() - discount_amount;

                                    tv_order_place.setText("Place this order : "+global.getCurrency_Symbol()+" "+String.valueOf(df.format(pay_amount)));


                                }else if (global.Discount_Type.equals("P")){


                                    double discount = Float.parseFloat(global.Discount_amount);

                                    double discount_amount = global.getCURRENT_TOTAL() * (discount / 100);

                                    Log.d(TAG, "discount amount = " + discount_amount);

                                    double pay_amount = global.getCURRENT_TOTAL() - discount_amount;

                                    tv_order_place.setText("Place this order : "+global.getCurrency_Symbol()+" "+String.valueOf(df.format(pay_amount)));

                                }

                            }else {

                                tv_order_place.setText("Place this order : "+global.getCurrency_Symbol()+" "+String.valueOf(df.format(global.getCURRENT_TOTAL())));

                                global.setGRAND_TOTAL(global.getCURRENT_TOTAL());
                            }


                            //    Log.d(global.TAG, "total_cart_amount = "+total_cart_amount);
                            //    Log.d(global.TAG, "ord_no_prefix = "+ord_no_prefix);
                            //    Log.d(global.TAG, "ord_no_suffix = "+ord_no_suffix);
                            //    Log.d(global.TAG, "global.setCART_ID = "+global.getCART_ID());




                            // Log.d(global.TAG, "cart_list_array = "+cart_list_array);

                            cart_list_array.clear();


                            cart_list_array = temp_list_cart;

                            global.Cart_List_Item = temp_list_cart;


                            //   Log.d(global.TAG, "temp_list_cart length = "+temp_list_cart.size());


                           // tv_cart_number.setText(String.valueOf(global.getCart_size()));

                        }

                        mProgressDialog.dismiss();


                        notifyDataSetChanged();



                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "productAddToCart- " + res);

                mProgressDialog.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });



    }

}
