package com.sketch.developer.gowireless.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.HashMap;


public class Global_Class extends Application {

   public Boolean login_status = false;

   String id, name, email, phone_number, fcm_reg_token, deviceid, profil_pic;

   public String notification;
   public String FCM;
   String username;

   String address,cityname,statename,countryname,zip,phone;



   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getCityname() {
      return cityname;
   }

   public void setCityname(String cityname) {
      this.cityname = cityname;
   }

   public String getStatename() {
      return statename;
   }

   public void setStatename(String statename) {
      this.statename = statename;
   }

   public String getCountryname() {
      return countryname;
   }

   public void setCountryname(String countryname) {
      this.countryname = countryname;
   }

   public String getZip() {
      return zip;
   }

   public void setZip(String zip) {
      this.zip = zip;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public Boolean getLogin_status() {
      return login_status;
   }

   public void setLogin_status(Boolean login_status) {
      this.login_status = login_status;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPhone_number() {
      return phone_number;
   }

   public void setPhone_number(String phone_number) {
      this.phone_number = phone_number;
   }

   public String getFcm_reg_token() {
      return fcm_reg_token;
   }

   public void setFcm_reg_token(String fcm_reg_token) {
      this.fcm_reg_token = fcm_reg_token;
   }

   public String getDeviceid() {
      return deviceid;
   }

   public void setDeviceid(String deviceid) {
      this.deviceid = deviceid;
   }

   public String getProfil_pic() {
      return profil_pic;
   }

   public void setProfil_pic(String profil_pic) {
      this.profil_pic = profil_pic;
   }

   public String getNotification() {
      return notification;
   }

   public String getFCM() {
      return FCM;
   }

   public void setFCM(String FCM) {
      this.FCM = FCM;
   }


   public void setNotification(String notification) {
      this.notification = notification;
   }

   public boolean isNetworkAvailable() {
      ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = manager.getActiveNetworkInfo();

      boolean isAvailable = false;
      if (networkInfo != null && networkInfo.isConnected()) {
         isAvailable = true;
      }
      return isAvailable;
   }

   public ArrayList<HashMap<String,String>> TAB_Cart_List_Item = new ArrayList<>();

   public ArrayList<HashMap<String,String>> TAB_Cart_Offer = new ArrayList<>();

   public ArrayList<HashMap<String,String>> TAB_Cart_Tax = new ArrayList<>();

   public ArrayList<HashMap<String,String>> Cart_List_Item = new ArrayList<>();



   public ArrayList<HashMap<String,String>> Cart_Offer = new ArrayList<>();

   public ArrayList<HashMap<String,String>> Cart_Tax = new ArrayList<>();

   public int cart_size ;

   public ArrayList<HashMap<String, String>> getCart_Offer() {
      return Cart_Offer;
   }


   public void setCart_Offer(ArrayList<HashMap<String, String>> cart_Offer) {
      Cart_Offer = cart_Offer;}

   public int getCart_size() {
      return cart_size;
   }

   public void setCart_size(int cart_size) {
      this.cart_size = cart_size;
   }


//////////////////////

   public double CURRENT_TOTAL = 0;
   public double GRAND_TOTAL = 0;
   public int t_qty_cart = 0;
   public int t_item_cart = 0;

   public double getCURRENT_TOTAL() {
      return CURRENT_TOTAL;
   }

   public void setCURRENT_TOTAL(double CURRENT_TOTAL) {
      this.CURRENT_TOTAL = CURRENT_TOTAL;
   }

   public double getGRAND_TOTAL() {
      return GRAND_TOTAL;
   }

   public void setGRAND_TOTAL(double GRAND_TOTAL) {
      this.GRAND_TOTAL = GRAND_TOTAL;
   }

   public int getT_qty_cart() {
      return t_qty_cart;
   }

   public void setT_qty_cart(int t_qty_cart) {
      this.t_qty_cart = t_qty_cart;
   }

   public int getT_item_cart() {
      return t_item_cart;
   }

   public void setT_item_cart(int t_item_cart) {
      this.t_item_cart = t_item_cart;
   }


   ////////////////

   public String CART_ID = "";
   public String SHIPPINIG_CHARGES = "";

   public String getCART_ID() {
      return CART_ID;
   }

   public void setCART_ID(String CART_ID) {
      this.CART_ID = CART_ID;
   }

   public String getSHIPPINIG_CHARGES() {
      return SHIPPINIG_CHARGES;
   }

   public void setSHIPPINIG_CHARGES(String SHIPPINIG_CHARGES) {
      this.SHIPPINIG_CHARGES = SHIPPINIG_CHARGES;
   }

   ////////////////////////////////

   public String Discount_Value = "";
   public String Discount_amount = "";
   public String Discount_Type = "";

   public String getDiscount_Value() {
      return Discount_Value;
   }

   public void setDiscount_Value(String discount_Value) {
      Discount_Value = discount_Value;
   }

   public String getDiscount_amount() {
      return Discount_amount;
   }

   public void setDiscount_amount(String discount_amount) {
      Discount_amount = discount_amount;
   }

   public String getDiscount_Type() {
      return Discount_Type;
   }

   public void setDiscount_Type(String discount_Type) {
      Discount_Type = discount_Type;
   }

   ////////////

   public String Currency_Symbol = "";

   public String getCurrency_Symbol() {
      return Currency_Symbol;
   }

   public void setCurrency_Symbol(String currency_Symbol) {
      Currency_Symbol = currency_Symbol;
   }

   ///////////////////////////////////

   public String cartdata = "0";

   public String getCartdata() {
      return cartdata;
   }

   public void setCartdata(String cartdata) {
      this.cartdata = cartdata;
   }

   public String ORDER_ID;

   public String getORDER_ID() {
      return ORDER_ID;
   }

   public void setORDER_ID(String ORDER_ID) {
      this.ORDER_ID = ORDER_ID;
   }

   public  String SELECTED_SHIPPING_ADDRESS_ID ="";

   public String getSELECTED_SHIPPING_ADDRESS_ID() {
      return SELECTED_SHIPPING_ADDRESS_ID;
   }

   public void setSELECTED_SHIPPING_ADDRESS_ID(String SELECTED_SHIPPING_ADDRESS_ID) {
      this.SELECTED_SHIPPING_ADDRESS_ID = SELECTED_SHIPPING_ADDRESS_ID;
   }
   public  String saved_address ="";

   public String getSaved_address() {
      return saved_address;
   }

   public void setSaved_address(String saved_address) {
      this.saved_address = saved_address;
   }

   public Boolean cartclicked = false;
}
