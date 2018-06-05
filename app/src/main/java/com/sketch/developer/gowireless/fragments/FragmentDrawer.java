package com.sketch.developer.gowireless.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.adapters.Expandable_list_adapter;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.NavDrawerItem;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ANDRIOD on 9/13/2016.
 */
public class FragmentDrawer extends Fragment {

    private static String TAG = "mercy";
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private TextView name;
    List<NavDrawerItem> data = new ArrayList<>();
    NavDrawerItem navItem;

    Global_Class globalClass;
    Shared_Prefrence prefrence;

    ArrayList<HashMap<String, String>> List_Header_1;
    ArrayList<HashMap<String, String>> List_Child_1;

    HashMap<String, HashMap<String, String>> MAP_ListDataChild;
    ArrayList<HashMap<String, HashMap<String, String>>> Arraylist_DataChild;
    ArrayList<String> list_keyvalue;

    Expandable_list_adapter expandable_list_adapter;
    ExpandableListView expand_lv;

    ImageView imageView2;
    ImageLoader loader;
    DisplayImageOptions defaultOptions;


    public FragmentDrawer() {

    }
    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        globalClass = (Global_Class)getActivity().getApplicationContext();
        prefrence = new Shared_Prefrence(getActivity());
        prefrence.loadPrefrence();


         defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext().getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();

        List_Header_1 = new ArrayList<>();
        List_Child_1 = new ArrayList<>();
        MAP_ListDataChild = new HashMap<>();
        Arraylist_DataChild = new ArrayList<>();
        list_keyvalue = new ArrayList<>();


        expand_lv = (ExpandableListView) layout.findViewById(R.id.expand_lv);
        imageView2 = layout.findViewById(R.id.imageView2);
        name = (TextView)layout.findViewById(R.id.name);

        name.setText(globalClass.getName());

        Log.d(TAG, "globalClass.getProfil_pic(): "+globalClass.getProfil_pic());

        if(globalClass.getProfil_pic().isEmpty()){

           /* Picasso.with(getActivity()).load(globalClass.getProfil_pic()).fit().centerCrop()
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.error64)
                    .into(imageView2);*/
            imageView2.setImageResource(R.mipmap.no_image);

        }else {
            loader.displayImage(globalClass.getProfil_pic(), imageView2 , defaultOptions);


        }


        ///////////////////////  set user name and email   ////////////////////////

      //  fragment = new FragmentProductList();
        fragmentManager = getActivity().getSupportFragmentManager();
       /* fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

        category_url();



        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(globalClass.getProfil_pic().isEmpty()){


            imageView2.setImageResource(R.mipmap.no_image);

        }else {
            loader.displayImage(globalClass.getProfil_pic(), imageView2 , defaultOptions);


        }

    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                prefrence.loadPrefrence();
                name.setText(globalClass.getName());
                if(globalClass.getProfil_pic().isEmpty()){
                    imageView2.setImageResource(R.mipmap.no_image);
                }else {
                    loader.displayImage(globalClass.getProfil_pic(), imageView2 , defaultOptions);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }





    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);








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
                            for(int i = 0 ; i<data_1.length(); i++){

                                JSONObject main_cat_obj = data_1.getJSONObject(i);
                                String id = main_cat_obj.getString("id");
                                String parentid = main_cat_obj.getString("id");
                                String name = main_cat_obj.getString("name");


                                HashMap<String,String> hashMap = new HashMap<String, String>();
                                hashMap.put("id", id);
                                hashMap.put("name", name);

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


                            expandable_list_adapter = new Expandable_list_adapter(getActivity(), List_Header_1, Arraylist_DataChild,
                                    list_keyvalue,fragmentManager,mDrawerLayout);
                            expand_lv.setAdapter(expandable_list_adapter);



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







}
