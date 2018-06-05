package com.sketch.developer.gowireless.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.activity.EditAddress;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.sketch.developer.gowireless.utils.Shared_Prefrence;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Developer on 2/15/18.
 */

public class AdapterAddress extends BaseAdapter {
    Context context;

  //  ArrayList addressr_array_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> ArrayList_ShippingAddress = new ArrayList<>();
    Global_Class globalClass;
    EditText txt_edit;
    int selectedPosition = -1;
    ArrayList<Boolean> booleansarr;
    Shared_Prefrence prefrence;
    String address_save;


    public AdapterAddress(Context context, ArrayList<HashMap<String, String>> ArrayList_ShippingAddress_) {

        this.context = context;
        this.ArrayList_ShippingAddress = ArrayList_ShippingAddress_;
        globalClass = (Global_Class)context.getApplicationContext();
        prefrence = new Shared_Prefrence(context);

        setBooleanValue();

    }

    public void setBooleanValue(){


        booleansarr = new ArrayList<>();
        for(int i = 0 ; i<ArrayList_ShippingAddress.size(); i++ ){
            booleansarr.add(false);
        }

        Log.d("dcdcdc", "ArrayList_ShippingAddress: "+ArrayList_ShippingAddress.size());
        Log.d("dcdcdc", "booleansarr: "+booleansarr.size());



    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v= convertView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.address_list_single_row, null, true);

        final String id = ArrayList_ShippingAddress.get(position).get("id");
        final String address = ArrayList_ShippingAddress.get(position).get("address");
        final String cityname = ArrayList_ShippingAddress.get(position).get("cityname");
        final String statename = ArrayList_ShippingAddress.get(position).get("statename");
        final String countryname = ArrayList_ShippingAddress.get(position).get("countryname");
        final String zip = ArrayList_ShippingAddress.get(position).get("zip");
        final String phone = ArrayList_ShippingAddress.get(position).get("phone");
        final TextView txt_address = (TextView) listViewItem.findViewById(R.id.txt_address);
        TextView txt_name = (TextView) listViewItem.findViewById(R.id.txt_name);
        TextView txt_edit = (TextView) listViewItem.findViewById(R.id.txt_edit);
        final ImageView img_rb = (ImageView) listViewItem.findViewById(R.id.img_rb);




        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedData(position,true);

                address_save=address+", "+cityname+", "+statename+", "+countryname+", "+zip;
                Log.d("add", "onClick: "+address_save);
                globalClass.setSELECTED_SHIPPING_ADDRESS_ID(id);
                globalClass.setSaved_address(address_save);



                prefrence.savePrefrence();


            }
        });


        if(booleansarr.get(position))
        {
            img_rb.setImageResource(R.mipmap.select96);
        }else {
            img_rb.setImageResource(R.mipmap.dselect96);
        }




        txt_name.setText(globalClass.getName());

        txt_address.setText(address+"\n"+cityname+", "+statename+"\n"+countryname+", "+zip+"\n"+"Mobile- "+phone);

        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                globalClass.setAddress(address);
                globalClass.setStatename(statename);
                globalClass.setCityname(cityname);
                globalClass.setCountryname(countryname);
                globalClass.setZip(zip);
                globalClass.setPhone(phone);

                Intent intent = new Intent(context,EditAddress.class);
                intent.putExtra("id",ArrayList_ShippingAddress.get(position).get("id"));

                context.startActivity(intent);
            }
        });


   /*     TextView tv_product_price_before = (TextView) listViewItem.findViewById(R.id.tv_product_price_before);
        // tv_product_price_before.setText("This is strike-thru");
        tv_product_price_before.setPaintFlags(tv_product_price_before.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
*/
        return listViewItem;

    }
    public void setSelectedData(int position, boolean boo){
        booleansarr.clear();
        setBooleanValue();
        booleansarr.set(position, boo);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count =0;
        if (ArrayList_ShippingAddress != null) {
            count=ArrayList_ShippingAddress.size();
        }
        return count;
    }

    @Override
    public Object getItem(int i) {
        return ArrayList_ShippingAddress.size();
    }

    @Override
    public long getItemId(int i) {
        return ArrayList_ShippingAddress.size();
    }
}
