package com.sketch.developer.gowireless.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.adapters.AdapterHomeGrid;
import com.sketch.developer.gowireless.adapters.AndroidBannerAdapter;
import com.sketch.developer.gowireless.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by Developer on 2/15/18.
 */

public class FragmentHome extends Fragment {

    private static final String TAG = "TAG";
    ViewPager mViewPager;
    CircleIndicator indicator;
    final Handler handler = new Handler();
    Timer swipeTimer = new Timer();

    int NUM_PAGES = 0, currentPage = 0;

    ArrayList<String> ImagesArray = new ArrayList<>();
    ArrayList<String> sliderImagesId = new ArrayList<>();

    ArrayList<HashMap<String, String>> List_Header_1;
    ArrayList<HashMap<String, String>> List_Child_1;

    HashMap<String, HashMap<String, String>> MAP_ListDataChild;
    ArrayList<HashMap<String, HashMap<String, String>>> Arraylist_DataChild;


    BannerSlider bannerSlider;
    List<Banner> banners;
    ArrayList<String> list_keyvalue;
    RecyclerView recyclerView;
    AdapterHomeGrid adapterHomeGrid;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPageAndroid);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        bannerSlider = (BannerSlider) view.findViewById(R.id.banner_slider1);
        banners = new ArrayList<>();
        //add banner using image url
        fragmentManager = getActivity().getSupportFragmentManager();
        banner_url();
        category_url();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);


        List_Header_1 = new ArrayList<>();
        List_Child_1 = new ArrayList<>();
        MAP_ListDataChild = new HashMap<>();
        Arraylist_DataChild = new ArrayList<>();

        list_keyvalue = new ArrayList<>();



        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));



       /* StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);*/




       // initViewpager();
        return  view;
    }



    public void banner_url(){

        String url = WebserviceUrl.getSliderImage;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("store_name",WebserviceUrl.Storename);
        params.put("no_of_image","3");


        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    if (response != null) {
                         Log.d("TAG", "frag_home- " + response.toString());
                        try {

                            JSONObject result = response.getJSONObject("result");

                            int status = result.getInt("status");

                                if (status == 1) {
                                    sliderImagesId.clear();

                                    JSONArray data = result.getJSONArray("data");
                                    for(int i = 0 ; i<data.length(); i++){

                                        String url = data.getString(i);
                                        Log.d(TAG, "url: "+url);

                                        banners.add(new RemoteBanner(url));
                                        banners.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
                                       // Log.d(TAG, "RemoteBanner(url): "+RemoteBanner(url));
                                        sliderImagesId.add(url);


                                    }
                                    initViewpager(sliderImagesId);
                                    bannerSlider.setBanners(banners);

                                }

                            } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                }
            });









    }


    public void category_url(){

        String url = WebserviceUrl.getCatSubcatTreeView;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("store_name",WebserviceUrl.Storename);


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

                            if(data_1.length() < 6){


                            for(int i = 0 ; i<data_1.length(); i++){

                                JSONObject main_cat_obj = data_1.getJSONObject(i);
                                String id = main_cat_obj.getString("id");
                                String parentid = main_cat_obj.getString("id");
                                String name = main_cat_obj.getString("name");
                                String imagenormal = main_cat_obj.optString("imagenormal");
                                String imagethumb = main_cat_obj.optString("imagethumb");

                                Log.d("TAG", "imagethumb: "+imagethumb);


                                HashMap<String,String> hashMap = new HashMap<String, String>();
                                hashMap.put("id", id);
                                hashMap.put("name", name);
                                hashMap.put("imagenormal", imagenormal);
                                hashMap.put("imagethumb", imagethumb);

                                List_Header_1.add(hashMap);

                                JSONArray child_1= main_cat_obj.getJSONArray("children");

                                for (int j = 0; j < child_1.length(); j++){

                                    JSONObject obj_child_1 = child_1.getJSONObject(j);

                                    String child_id_1 = obj_child_1.optString("id");
                                    String child_name_1 = obj_child_1.optString("name");

                                    JSONArray child_2 = obj_child_1.getJSONArray("children");


                                    HashMap<String,String> child_1_hashMap = new HashMap<String, String>();

                                    child_1_hashMap.put("child_id", child_id_1);
                                    child_1_hashMap.put("child_parentid", parentid);
                                    child_1_hashMap.put("child_name", child_name_1);

                                    List_Child_1.add(child_1_hashMap);


                                    ///////////////////////
                                    // Node  3  ................
                                    for (int k = 0; k < child_2.length(); k++){

                                        JSONObject obj_child_2 = child_2.getJSONObject(k);

                                        String child_id_2 = obj_child_2.optString("id");
                                        String child_name_2 = obj_child_2.optString("name");

                                        JSONArray child_3 = obj_child_2.getJSONArray("children");


                                        HashMap<String,String> child_2_hashMap = new HashMap<String, String>();

                                        child_2_hashMap.put("child_id", child_id_2);
                                        child_2_hashMap.put("child_parentid", parentid);
                                        child_2_hashMap.put("child_name", child_name_2);


                                        List_Child_1.add(child_2_hashMap);


                                        ///////////////////////////////

                                        // Node 4  .................
                                        // Log.d(global.TAG, "length child_3 = "+l3);
                                        for (int m = 0; m < child_3.length(); m++){

                                            JSONObject obj_child_3 = child_3.getJSONObject(m);

                                            String child_id_3 = obj_child_3.optString("id");
                                            String child_name_3 = obj_child_3.optString("name");

                                            JSONArray child_4 = obj_child_3.getJSONArray("children");


                                            HashMap<String,String> child_3_hashMap = new HashMap<String, String>();

                                            child_3_hashMap.put("child_id", child_id_3);
                                            child_3_hashMap.put("child_parentid", parentid);
                                            child_3_hashMap.put("child_name", child_name_3);

                                            List_Child_1.add(child_3_hashMap);

                                            // Node 5  .................
                                            for (int p = 0; p < child_4.length(); p++){
                                                JSONObject obj_child_4 = child_4.getJSONObject(p);

                                                String child_id_4 = obj_child_4.optString("id");
                                                String child_name_4 = obj_child_4.optString("name");

                                                JSONArray child_5 = obj_child_4.getJSONArray("children");

                                                HashMap<String,String> child_4_hashMap = new HashMap<String, String>();

                                                child_4_hashMap.put("child_id", child_id_4);
                                                child_4_hashMap.put("child_parentid", parentid);
                                                child_4_hashMap.put("child_name", child_name_4);

                                                List_Child_1.add(child_4_hashMap);

                                            }


                                        }


                                    }


                                }


                            }

                            for (int i = 0; i < List_Header_1.size(); i++) {
                                int size = 0;
                                for (int j = 0; j < List_Child_1.size(); j++) {

                                    if (List_Header_1.get(i).get("id")
                                            .equals(List_Child_1.get(j).get("child_parentid"))) {

                                        MAP_ListDataChild.put(List_Header_1.get(i).get("id") + size, List_Child_1.get(j));

                                        Arraylist_DataChild.add(MAP_ListDataChild);

                                        list_keyvalue.add(List_Header_1.get(i).get("id") + size);

                                        size++;

                                    }

                                }
                            }

                            }else{


                                for(int i = 0 ; i<=6; i++){

                                    JSONObject main_cat_obj = data_1.getJSONObject(i);
                                    String id = main_cat_obj.getString("id");
                                    String parentid = main_cat_obj.getString("id");
                                    String name = main_cat_obj.getString("name");
                                    String imagenormal = main_cat_obj.optString("imagenormal");
                                    String imagethumb = main_cat_obj.optString("imagethumb");

                                    Log.d("TAG", "imagethumb: "+imagethumb);

                                    HashMap<String,String> hashMap = new HashMap<String, String>();
                                    hashMap.put("id", id);
                                    hashMap.put("name", name);
                                    hashMap.put("imagenormal", imagenormal);
                                    hashMap.put("imagethumb", imagethumb);

                                    List_Header_1.add(hashMap);

                                    JSONArray child_1= main_cat_obj.getJSONArray("children");

                                    for (int j = 0; j < child_1.length(); j++){

                                        JSONObject obj_child_1 = child_1.getJSONObject(j);

                                        String child_id_1 = obj_child_1.optString("id");
                                        String child_name_1 = obj_child_1.optString("name");

                                        JSONArray child_2 = obj_child_1.getJSONArray("children");


                                        HashMap<String,String> child_1_hashMap = new HashMap<String, String>();

                                        child_1_hashMap.put("child_id", child_id_1);
                                        child_1_hashMap.put("child_parentid", parentid);
                                        child_1_hashMap.put("child_name", child_name_1);

                                        List_Child_1.add(child_1_hashMap);


                                        ///////////////////////
                                        // Node  3  ................
                                        for (int k = 0; k < child_2.length(); k++){

                                            JSONObject obj_child_2 = child_2.getJSONObject(k);

                                            String child_id_2 = obj_child_2.optString("id");
                                            String child_name_2 = obj_child_2.optString("name");

                                            JSONArray child_3 = obj_child_2.getJSONArray("children");


                                            HashMap<String,String> child_2_hashMap = new HashMap<String, String>();

                                            child_2_hashMap.put("child_id", child_id_2);
                                            child_2_hashMap.put("child_parentid", parentid);
                                            child_2_hashMap.put("child_name", child_name_2);


                                            List_Child_1.add(child_2_hashMap);


                                            ///////////////////////////////

                                            // Node 4  .................
                                            // Log.d(global.TAG, "length child_3 = "+l3);
                                            for (int m = 0; m < child_3.length(); m++){

                                                JSONObject obj_child_3 = child_3.getJSONObject(m);

                                                String child_id_3 = obj_child_3.optString("id");
                                                String child_name_3 = obj_child_3.optString("name");

                                                JSONArray child_4 = obj_child_3.getJSONArray("children");


                                                HashMap<String,String> child_3_hashMap = new HashMap<String, String>();

                                                child_3_hashMap.put("child_id", child_id_3);
                                                child_3_hashMap.put("child_parentid", parentid);
                                                child_3_hashMap.put("child_name", child_name_3);

                                                List_Child_1.add(child_3_hashMap);

                                                // Node 5  .................
                                                for (int p = 0; p < child_4.length(); p++){
                                                    JSONObject obj_child_4 = child_4.getJSONObject(p);

                                                    String child_id_4 = obj_child_4.optString("id");
                                                    String child_name_4 = obj_child_4.optString("name");

                                                    JSONArray child_5 = obj_child_4.getJSONArray("children");

                                                    HashMap<String,String> child_4_hashMap = new HashMap<String, String>();

                                                    child_4_hashMap.put("child_id", child_id_4);
                                                    child_4_hashMap.put("child_parentid", parentid);
                                                    child_4_hashMap.put("child_name", child_name_4);

                                                    List_Child_1.add(child_4_hashMap);

                                                }


                                            }


                                        }


                                    }


                                }

                                for (int i = 0; i < List_Header_1.size(); i++) {
                                    int size = 0;
                                    for (int j = 0; j < List_Child_1.size(); j++) {

                                        if (List_Header_1.get(i).get("id")
                                                .equals(List_Child_1.get(j).get("child_parentid"))) {

                                            MAP_ListDataChild.put(List_Header_1.get(i).get("id") + size, List_Child_1.get(j));

                                            Arraylist_DataChild.add(MAP_ListDataChild);

                                            list_keyvalue.add(List_Header_1.get(i).get("id") + size);

                                            size++;

                                        }

                                    }
                                }


                            }

                            adapterHomeGrid = new AdapterHomeGrid(getActivity(), List_Header_1, Arraylist_DataChild,list_keyvalue,
                                    fragmentManager);
                            recyclerView.setAdapter(adapterHomeGrid);


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


    private void initViewpager(ArrayList<String> sliderImagesId) {



        mViewPager.setAdapter(new AndroidBannerAdapter(getActivity(), sliderImagesId));
        indicator.setViewPager(mViewPager);


        indicator.setViewPager(mViewPager);

//        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radious

        NUM_PAGES = sliderImagesId.size();

        // Auto start of viewpager

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        handler.removeCallbacks(Update);



        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 2000);

        handler.postDelayed(Update,6000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });



    }
}
