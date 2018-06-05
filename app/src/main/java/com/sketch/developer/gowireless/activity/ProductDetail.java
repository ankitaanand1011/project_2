package com.sketch.developer.gowireless.activity;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.adapters.ProductViewPagerAdapter;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.Shared_Prefrence;
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
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Developer on 2/15/18.
 */

public class ProductDetail extends AppCompatActivity {

    ViewPager viewPager;
    final Handler handler = new Handler();
    int NUM_PAGES = 0, currentPage = 0;
    ArrayList<String> sliderImagesId = new ArrayList<>();
    String data;
    TextView txt_name,txt_discount_price,txt_original_price,tv_product_description_1,cart_badge;
    Spinner spinner,spinner_colors;
    List<String> list;
    Button btn_add_to_cart;
    String[] str_attr_child;
    String attr_child;
    LinearLayout ll_spinner;
    RelativeLayout rl_description;
    Global_Class globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    String TAG = "product_detail";
    String id,name,price,special_price,final_price,stock_id;
    String qty;
    ImageView img;

    ArrayList<HashMap<String, String>> ArrayList_Cart = new ArrayList<>();
    ArrayList<HashMap<String, String>> ArrayList_Cart_Offer = new ArrayList<>();
    ArrayList<HashMap<String, String>> ArrayList_Cart_Tax = new ArrayList<>();

    ArrayList<HashMap<String, String>> List_Filter_Attributes = new ArrayList<>();
    ArrayList<HashMap<String, String>> List_Filter_Available_Combination = new ArrayList<>();
    ArrayList<HashMap<String, String>> List_Filter_Offer = new ArrayList<>();
    ArrayList<HashMap<String, String>> List_Filter_Combination_Offer = new ArrayList<>();


    DisplayImageOptions defaultOptions;
    ImageLoader loader;
    ImageView img_back,cart_img;
    String AVL_STOCK;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetail);

        globalClass = (Global_Class)getApplicationContext();
        prefrence = new Shared_Prefrence(ProductDetail.this);
        pd=new ProgressDialog(ProductDetail.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();



        data = getIntent().getStringExtra("data");
        img_back = findViewById(R.id.img_back);
        cart_img = findViewById(R.id.cart_img);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalClass.cartclicked = true;
                finish();
            }
        });


        viewPager = findViewById(R.id.viewPager);
        txt_name = findViewById(R.id.txt_name);
        img = findViewById(R.id.img);
        txt_discount_price = findViewById(R.id.txt_discount_price);
        txt_original_price = findViewById(R.id.txt_original_price);
        tv_product_description_1 = findViewById(R.id.tv_product_description_1);
        rl_description = findViewById(R.id.rl_description);
        ll_spinner = findViewById(R.id.ll_spinner);
        cart_badge = findViewById(R.id.cart_badge);

        Log.d(TAG, "onCreatecart: "+globalClass.getCartdata());
        if (globalClass.getCartdata().equals("0") || globalClass.getCartdata().isEmpty()){
            cart_badge.setText("0");
        }else {
            cart_badge.setText(globalClass.getCartdata());
        }


        txt_original_price.setPaintFlags(txt_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Add_To_Cart(globalClass.getId());
            }
        });

        list = new ArrayList<>();
        list.add("01");
        list.add("02");
        list.add("03");
        list.add("04");
        list.add("05");
        list.add("06");
        list.add("07");
        list.add("08");
        list.add("09");
        list.add("10");
        list.add("11");
        list.add("12");

        spinner = findViewById(R.id.spinner);
        spinner_colors = findViewById(R.id.spinner_colors);

        ArrayAdapter<String> adp = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list);
        //adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adp);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                String item = spinner.getItemAtPosition(1).toString();
                //Toast.makeText(spinner.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                 qty=spinner.getSelectedItem().toString();
            }


            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });





        spinner_colors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //@Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                String item = spinner_colors.getItemAtPosition(1).toString();
                //Toast.makeText(spinner.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                String ss=spinner_colors.getSelectedItem().toString();
            }


            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        getdata();




    }

    private void initViewpager(ArrayList<String> sliderImagesId) {



        viewPager.setAdapter(new ProductViewPagerAdapter(ProductDetail.this, sliderImagesId));



        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radious

        NUM_PAGES = sliderImagesId.size();

        // Auto start of viewpager

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        handler.removeCallbacks(Update);



       /* swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        handler.postDelayed(Update,10000);*/

        // Pager listener over indicator



    }


    public void getdata(){

        try {

            JSONObject object = new JSONObject(data);
             id = object.getString("id");
             name = object.getString("name");
            String short_description = object.getString("short_description");
            String imagename = object.getString("imagename");
            String imagename_thumb = object.getString("imagename_thumb");
             price = object.getString("price");
             special_price = object.getString("special_price");
             final_price = object.getString("final_price");
            stock_id = object.getString("stock_id");
            String stock = object.getString("stock");

            Log.d("mer", "imagename: "+imagename);

            JSONArray all_discount_dtls = object.getJSONArray("all_discount_dtls");
            for (int k = 0; k < all_discount_dtls.length(); k++) {
                JSONObject obj_discount = all_discount_dtls.getJSONObject(k);

                String discount_name = obj_discount.getString("discount_name");
                String discount_amount = obj_discount.getString("discount_amount");
                String discount_type = obj_discount.getString("discount_type");

            }

            JSONArray attributes_data = object.getJSONArray("attributes_data");
            for (int n = 0; n < attributes_data.length(); n++) {
                JSONObject obj_attributes = attributes_data.getJSONObject(n);

                String attr_id = obj_attributes.getString("attr_id");
                String attr_name = obj_attributes.getString("attr_name");
                attr_child = obj_attributes.getString("attr_child");

                str_attr_child = attr_child.split(",");


                Log.d("attr_child", "attr_child: "+str_attr_child);


            }


            JSONArray available_combination = object.getJSONArray("available_combination");
            for (int n = 0; n < available_combination.length(); n++) {

                JSONObject obj_available_combi = available_combination.getJSONObject(n);
                String combination_id = obj_available_combi.optString("id");
                String pid = obj_available_combi.optString("pid");
                String stockdet = obj_available_combi.optString("stockdet");
                String c_price = obj_available_combi.optString("price");
                String spcl_price = obj_available_combi.optString("spcl_price");
                String available_qnty = obj_available_combi.optString("available_qnty");

                String image_combination = obj_available_combi.optString("imagename");
                String imagename_thumb_1 = obj_available_combi.optString("imagename_thumb");



                HashMap<String, String> map_attr = new HashMap<String, String>();

                map_attr.put("id", combination_id);
                map_attr.put("pid", pid);
                map_attr.put("combination", stockdet);
                map_attr.put("price", c_price);
                map_attr.put("spcl_price", spcl_price);
                map_attr.put("available_qnty", available_qnty);

                map_attr.put("image_combination", image_combination);
                map_attr.put("imagename_thumb", imagename_thumb_1);

                List_Filter_Available_Combination.add(map_attr);


            }


            JSONArray array_all_product_img = object.getJSONArray("all_product_images");

            for (int n = 0; n < array_all_product_img.length(); n++) {
                JSONObject obj_all_product_img = array_all_product_img.getJSONObject(n);

                String name = obj_all_product_img.getString("name");

                String img_name = WebserviceUrl.img_prefix+name;

                sliderImagesId.add(img_name);
            }


            viewPager.setAdapter(new ProductViewPagerAdapter(ProductDetail.this, sliderImagesId));




            txt_name.setText(name);

            if(short_description.isEmpty()){

                rl_description.setVisibility(View.GONE);
            }else {
                rl_description.setVisibility(View.VISIBLE);
                tv_product_description_1.setText(short_description);
            }

            if(!attr_child.isEmpty()) {

                ArrayAdapter<String> adpter = new ArrayAdapter<String>
                        (this, R.layout.spinner_item, str_attr_child);
                //adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_colors.setAdapter(adpter);

            }else {
                ll_spinner.setVisibility(View.GONE);
            }

            spinner_colors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ///    mProgressDialog.show();

                    String s = parent.getItemAtPosition(position).toString();




                    // checking combination............

                    for (int j = 0; j < List_Filter_Available_Combination.size(); j++){

                        if (s.equals(List_Filter_Available_Combination.get(j).get("combination"))){

                           // tv_addtocart.setVisibility(View.VISIBLE);

                            Log.d(TAG, "YES  = "+s);


                            String pid = List_Filter_Available_Combination.get(j).get("pid");
                            //  Log.d(global.TAG, "pid  = "+pid);

                            stock_id = List_Filter_Available_Combination.get(j).get("id");
                            //  Log.d(global.TAG, "stock_id  = "+stock_id);


                            txt_original_price.setText("");
                            txt_discount_price.setText("");

                            //   Log.d(global.TAG, "price  = "+List_Filter_Available_Combination.get(j).get("price"));
                            //   Log.d(global.TAG, "spcl_price  = "+List_Filter_Available_Combination.get(j).get("spcl_price"));

                            String specl_price = List_Filter_Available_Combination.get(j).get("spcl_price");
                            if (specl_price.equals("0.00")){

                                txt_original_price.setText(globalClass.getCurrency_Symbol()+" "+List_Filter_Available_Combination.get(j).get("price"));
                                txt_discount_price.setVisibility(View.GONE);

                            }else {

                                txt_original_price.setText(globalClass.getCurrency_Symbol()+" "+List_Filter_Available_Combination.get(j).get("price"));
                                txt_discount_price.setText(globalClass.getCurrency_Symbol()+" "+specl_price);
                            }


                            AVL_STOCK = List_Filter_Available_Combination.get(j).get("available_qnty");

                            if (AVL_STOCK.equals("0")){

                                btn_add_to_cart.setVisibility(View.INVISIBLE);

                            }else {

                                btn_add_to_cart.setVisibility(View.VISIBLE);

                            }

                       //     tv_combination_stock.setText("Stock: "+List_Filter_Available_Combination.get(j).get("available_qnty"));

                            String img_url = List_Filter_Available_Combination.get(j).get("image_combination");
                            Log.d("img_url", "img_url: "+img_url);


                            loader.displayImage(img_url, img , defaultOptions);






                            // Combination offer check ...........
                            String combination_id = List_Filter_Available_Combination.get(j).get("id");

                            ArrayList<HashMap<String, String>> list_temp_offer = new ArrayList<HashMap<String, String>>();

                            for(int x = 0; x < List_Filter_Combination_Offer.size() ; x++)
                            {
                                if (combination_id.equals(List_Filter_Combination_Offer.get(x).get("combination_id"))){

                                    list_temp_offer.add(List_Filter_Combination_Offer.get(x));
                                }

                            }
                            //  Log.d(global.TAG, "list_temp_offer  = "+list_temp_offer);

                            for (int y = 0; y < list_temp_offer.size(); y++){


                                String discount_name = list_temp_offer.get(y).get("discount_name");
                                String discount_amount = list_temp_offer.get(y).get("discount_amount");
                                String discount_type = list_temp_offer.get(y).get("discount_type");


                                if (discount_type.equals("A")){

                                   // tv_offer.setText("Offer - "+discount_name+" "+globalClass.getCurrency_Symbol()+" "+discount_amount);

                                }else {

                                   // tv_offer.setText("Offer - "+discount_name+"  "+discount_amount+"%");

                                }

                            }






                            break;

                        }

                    }





                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });







           /* if (special_price.equals("0")){
                txt_discount_price.setText(globalClass.getCurrency_Symbol()+" "+price);
                txt_original_price.setVisibility(View.GONE);
            }else{

                txt_discount_price.setText(globalClass.getCurrency_Symbol()+" "+special_price);
                txt_original_price.setVisibility(View.VISIBLE);
                txt_original_price.setText(globalClass.getCurrency_Symbol()+" "+price);
            }
*/
            Log.d(TAG, "price: "+price);
            Log.d(TAG, "special_price: "+special_price);

            if (final_price.equals("0")){
                txt_discount_price.setText(globalClass.getCurrency_Symbol()+" "+price);
                txt_original_price.setVisibility(View.GONE);
            }else{

                txt_discount_price.setText(globalClass.getCurrency_Symbol()+" "+final_price);
                txt_original_price.setVisibility(View.VISIBLE);

                if(price.matches("0.00")){
                    txt_original_price.setVisibility(View.GONE);

                }else{
                    txt_original_price.setText(globalClass.getCurrency_Symbol()+" "+price);
                }

            }



        }catch (Exception e){
                    e.printStackTrace();
        }






    }


    public void Add_To_Cart(String customer_id){


        pd.show();

      /*  global.TAB_Cart_List_Item.clear();
        global.TAB_Cart_Offer.clear();
        global.TAB_Cart_Tax.clear();
*/


        String url = WebserviceUrl.productAddToCart;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        //  params.put("email", global.getEmail());
        params.put("store_name",WebserviceUrl.Storename);

        params.put("product_id", id);
        params.put("product_quantity", qty);
        params.put("stock_id", stock_id);
        params.put("customer_id", customer_id);
        params.put("device_id",globalClass.getDeviceid());


        Log.d(TAG , "productAddToCart- " + url);
        Log.d(TAG , "productAddToCart- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                 Log.d(TAG, "productAddToCart- " + response.toString());

                if (response != null) {
                    try {


                        JSONObject result = response.getJSONObject("result");
                        int status = result.optInt("status");

                        if (status == 0){

                            Toast.makeText(ProductDetail.this, "No Product Available", Toast.LENGTH_LONG).show();

                        }else if (status == 1){

                            ArrayList_Cart.clear();
                            ArrayList_Cart_Offer.clear();
                            globalClass.Cart_List_Item.clear();
                            ArrayList_Cart_Tax.clear();
                         //   _lv_cart.setAdapter(null);




                            JSONObject obj_data = result.getJSONObject("data");

                            JSONArray cart_data = obj_data.getJSONArray("cart_data");
                            int l1 = cart_data.length();

                            globalClass.setCartdata(String.valueOf(l1));

                            globalClass.setCart_size(l1);


                            Log.d(TAG, "cart_data length = "+l1);
                        //    globalClass.setCartdata(String.valueOf(l1));
                            cart_badge.setText(String.valueOf(l1));


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
                                String final_price = obj_cart_data.optString("final_price");
                                String special_price = obj_cart_data.optString("special_price");
                                String product_main_img = obj_cart_data.optString("product_main_img");
                                String is_attr_exist = obj_cart_data.optString("is_attr_exist");
                                String image_path_thumb = obj_cart_data.optString("image_path_thumb");
                                String image_path = obj_cart_data.optString("image_path");
                                String attriblute_image = obj_cart_data.optString("attriblute_image");


                                Log.d("lkio", "onSuccess final_price: "+final_price);
                                Log.d("lkio", "onSuccess price: "+price);
                                Log.d("lkio", "onSuccess card_dtls_id: "+card_dtls_id);


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
                                map_cart.put("product_main_img" , product_main_img);
                                map_cart.put("is_attr_exist" , is_attr_exist);
                                map_cart.put("image_path_thumb" , image_path_thumb);
                                map_cart.put("image_path" , image_path);
                                map_cart.put("attriblute_image" , attriblute_image);
                                map_cart.put("final_price" , final_price);

                                //    map_cart.put("AVL_STOCK", AVL_STOCK);



                                ArrayList_Cart.add(map_cart);
                                globalClass.Cart_List_Item.add(map_cart);




                                globalClass.setCART_ID(cart_id);


                                globalClass.setT_qty_cart(Integer.parseInt(cart_quantity));
                                globalClass.setT_item_cart(cart_data.length()+1);


                                JSONArray all_discount_dtls = obj_cart_data.getJSONArray("all_discount_dtls");
                                int l2 = all_discount_dtls.length();
                                //    Log.d(global.TAG, "all_discount_dtls length = "+l2);
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



                                    ArrayList_Cart_Offer.add(map_offer);

                                    globalClass.setCart_Offer(ArrayList_Cart_Offer);

                                }



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


                                    ArrayList_Cart_Tax.add(map_tax);


                                    globalClass.Cart_Tax.add(map_tax);

                                }


                                double total_cart_amount = Double.parseDouble(obj_data.optString("total_cart_amount"));

                                Log.d(TAG, "total_cart_amount = " + total_cart_amount);

                                globalClass.setCURRENT_TOTAL(total_cart_amount);

                                String total_cart_shipcost = obj_data.optString("total_cart_shipcost");
                                globalClass.setSHIPPINIG_CHARGES(total_cart_shipcost);

                                String cart_grand_total = obj_data.optString("cart_grand_total");
                                globalClass.setGRAND_TOTAL(Double.parseDouble(cart_grand_total));


                                //   Log.d(global.TAG, "total_cart_amount = "+global.getCURRENT_TOTAL());
                                //    Log.d(global.TAG, "ord_no_prefix = "+global.getPREFIX());
                                //  Log.d(global.TAG, "ord_no_suffix = "+global.getSUFFIX());
                                //  Log.d(global.TAG, "global.setCART_ID = "+global.getCART_ID());


                                DecimalFormat df = new DecimalFormat();
                                df.setMaximumFractionDigits(2);
                                df.setMinimumFractionDigits(2);

                                //  Log.d(global.TAG, "getCURRENT_TOTAL = " + global.getCURRENT_TOTAL());
                                //   Log.d(global.TAG, "getCURRENT_TOTAL = " + String.format("%.2f", global.getCURRENT_TOTAL()));
                                //   Log.d(global.TAG, "getCURRENT_TOTAL = " + String.valueOf(df.format(global.getCURRENT_TOTAL())));

                              //  tv_current_total.setText((df.format(globalClass.getCURRENT_TOTAL())));


                                if (!globalClass.Discount_amount.equals("")){

                                    if (globalClass.Discount_Type.equals("A")){


                                        double discount_amount = Float.parseFloat(globalClass.Discount_amount);

                                        Log.d(TAG, "discount amount = " + discount_amount);

                                        double pay_amount = globalClass.getCURRENT_TOTAL() - discount_amount;

                                    //    tv_pay_amount.setText(String.valueOf(df.format(pay_amount)));

                                        globalClass.setGRAND_TOTAL(pay_amount);


                                    }else if (globalClass.Discount_Type.equals("P")){


                                        double discount = Float.parseFloat(globalClass.Discount_amount);

                                        double discount_amount = globalClass.getCURRENT_TOTAL() * (discount / 100);

                                        Log.d(TAG, "discount amount = " + discount_amount);

                                        double pay_amount = globalClass.getCURRENT_TOTAL() - discount_amount;

                                   //     tv_pay_amount.setText(String.valueOf(df.format(pay_amount)));


                                        globalClass.setGRAND_TOTAL(pay_amount);

                                    }



                                }else {

                                 //   tv_pay_amount.setText(String.valueOf(df.format(globalClass.getCURRENT_TOTAL())));


                                    globalClass.setGRAND_TOTAL(globalClass.getCURRENT_TOTAL());

                                }


                            }


                            Toast.makeText(ProductDetail.this,"Product added to cart",Toast.LENGTH_SHORT).show();

                            //Log.d(global.TAG, "ArrayList_Cart_Offer = "+ArrayList_Cart_Offer);
/*
                            cartListViewAdapter = new T_Cart_Adapter(_context, global.Cart_List_Item,
                                    ArrayList_Cart_Offer,ArrayList_Cart_Tax,  tv_current_total, tv_pay_amount);
                            cartListViewAdapter.notifyDataSetChanged();
                            _lv_cart.setAdapter(cartListViewAdapter);*/

                        }

                        pd.dismiss();

                    //    Validate_Authentication();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "productAddToCart- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(ProductDetail.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }

}