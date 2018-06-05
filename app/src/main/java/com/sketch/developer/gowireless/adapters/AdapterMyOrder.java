package com.sketch.developer.gowireless.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sketch.developer.gowireless.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Developer on 2/15/18.
 */

public class AdapterMyOrder extends BaseAdapter {
    Context context;


    ArrayList<HashMap<String, String>> Arrayliat_Order;


    public AdapterMyOrder(Context context, ArrayList<HashMap<String, String>> Arrayliat_Order) {

        this.context = context;
        this.Arrayliat_Order = Arrayliat_Order;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v= convertView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.my_order_list_single_row, null, true);


        //String name = (String) Arrayliat_Order.get(position).get("order_date");
        TextView txt_order_id = (TextView) listViewItem.findViewById(R.id.txt_order_id);
        TextView txt_date = (TextView) listViewItem.findViewById(R.id.txt_date);
        TextView txt_status = (TextView) listViewItem.findViewById(R.id.txt_status);
        txt_order_id.setText("#"+Arrayliat_Order.get(position).get("order_number"));



        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String inputDateStr=Arrayliat_Order.get(position).get("order_date");
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);


        txt_date.setText(outputDateStr);

        String stat = Arrayliat_Order.get(position).get("delivery_status");

        switch (stat) {
            case "0":
                txt_status.setText("In Progress");
                txt_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case "1":
                txt_status.setText("Shipped");
                txt_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case "2":
                txt_status.setText("Dispatched");
                txt_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case "3":
                txt_status.setText("Delivered");
                txt_status.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "4":
                txt_status.setText("Cancelled");
                txt_status.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                break;
            case "5":
                txt_status.setText("Complete");
                txt_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
        }



   /*     TextView tv_product_price_before = (TextView) listViewItem.findViewById(R.id.tv_product_price_before);
        // tv_product_price_before.setText("This is strike-thru");
        tv_product_price_before.setPaintFlags(tv_product_price_before.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
*/
        return listViewItem;

    }

    @Override
    public int getCount() {
        int count =0;
        if (Arrayliat_Order != null) {
            count=Arrayliat_Order.size();
        }
        return count;
    }

    @Override
    public Object getItem(int i) {
        return Arrayliat_Order.size();
    }

    @Override
    public long getItemId(int i) {
        return Arrayliat_Order.size();
    }
}
