package com.sketch.developer.gowireless.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Developer on 7/19/17.
 */

public class Shared_Prefrence {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;

    SharedPreferences sharedPreferences3;
    SharedPreferences.Editor editor3;

    Global_Class global_class;
    private boolean pref_logInStatus;
    private String pref_name;
    private String pref_id;
    private String pref_email;
    private String pref_phone_number;
    private String pref_user_type;
    private String pref_profile_img;
    private String pref_shipping_address_id;
    private String pref_shipping_address;

    private String fcm;


    private static final String PREFS_NAME = "preferences";

    private static final String PREF_logInStatus = "logInStatus";
    private static final String PREF_name = "name";
    private static final String PREF_email = "email";
    private static final String PREF_phone_number = "phone_number";
    private static final String PREF_user_type = "user_type";
    private static final String PREF_id = "id";
    private static final String PREF_profile_img = "profile_img";

    private static final String is_first = "is_first";
    private static final String f_cm = "fcm";
    private static final String shipping_address = "shipping_address";
    private static final String shipping_address_id = "shipping_address_id";



    public Shared_Prefrence(Context context) {
        this.context = context;

        this.global_class = (Global_Class) context.getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();





    }

    public void savePrefrence() {
        if (global_class.getLogin_status()) {

            pref_logInStatus = global_class.getLogin_status();
            editor.putBoolean(PREF_logInStatus, pref_logInStatus);

            pref_name = global_class.getName();
            editor.putString(PREF_name, pref_name);

            pref_id= global_class.getId();
            editor.putString(PREF_id,pref_id);

            pref_email= global_class.getEmail();
            editor.putString(PREF_email,pref_email);

            pref_phone_number= global_class.getPhone_number();
            editor.putString(PREF_phone_number,pref_phone_number);


            pref_shipping_address_id= global_class.getSELECTED_SHIPPING_ADDRESS_ID();
            editor.putString(shipping_address_id,pref_shipping_address_id);

            pref_shipping_address= global_class.getSaved_address();
            editor.putString(shipping_address,pref_shipping_address);





            fcm = global_class.getFCM();
            editor.putString(f_cm, fcm);

            pref_profile_img = global_class.getProfil_pic();
            editor.putString(PREF_profile_img, pref_profile_img);






            editor.commit();

        }else{
            // dont save anything, if user is logged out
            pref_logInStatus = global_class.getLogin_status();
            editor.putBoolean(PREF_logInStatus, pref_logInStatus);
            editor.commit();
        }

    }

    public void loadPrefrence() {
        pref_logInStatus = sharedPreferences.getBoolean(PREF_logInStatus, false);
        global_class.setLogin_status(pref_logInStatus);

        Log.d("TV", global_class.getLogin_status() + "");
        if (global_class.getLogin_status()) {

            pref_name = sharedPreferences.getString(PREF_name, "");
            global_class.setName(pref_name);

            pref_id= sharedPreferences.getString(PREF_id,"");
            global_class.setId(pref_id);

            pref_phone_number=sharedPreferences.getString(PREF_phone_number,"");
            global_class.setPhone_number(pref_phone_number);

            pref_email=sharedPreferences.getString(PREF_email,"");
            global_class.setEmail(pref_email);


            fcm=sharedPreferences.getString(f_cm,"");
            global_class.setFCM(fcm);

            pref_profile_img=sharedPreferences.getString(PREF_profile_img,"");
            global_class.setProfil_pic(pref_profile_img);

            pref_shipping_address_id=sharedPreferences.getString(shipping_address_id,"");
            global_class.setSELECTED_SHIPPING_ADDRESS_ID(pref_shipping_address_id);

            pref_shipping_address=sharedPreferences.getString(shipping_address,"");
            global_class.setSaved_address(pref_shipping_address);


        }
    }


    public void setFirstlogin(boolean b){
        editor2.putBoolean(is_first,b);
        editor2.commit();
    }
    public boolean IS_First_Time(){
        return sharedPreferences2.getBoolean(is_first, false);

    }

    public  void clear_notification(){
        editor3.clear();
        editor3.commit();

    }

    public  void  save_noti(){

        editor3.commit();

    }

    public  void load_noti(){


    }



    public void clearPrefrence(){

        editor.clear();
        editor.commit();


    }













}
