package com.sketch.developer.gowireless.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.adapters.AdapterCartList;
import com.sketch.developer.gowireless.utils.Global_Class;

import java.util.ArrayList;


/**
 * Created by Developer on 2/15/18.
 */

public class FragmentCart extends Fragment{
    ListView listview_cart;
  //  ArrayList<HashMap<String, String>> List_cart = new ArrayList<>();
    ArrayList List_cart;
    AdapterCartList adapterCartList;
    Global_Class global;
    ProgressDialog mProgressDialog;
    TextView tv_order_place;
    ImageView empty_cart_iv;
    RelativeLayout rl_1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.fragment_cart, container, false);



        global = (Global_Class)getActivity().getApplicationContext();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Please wait...");

        tv_order_place = view.findViewById(R.id.tv_order_place);
        empty_cart_iv = view.findViewById(R.id.empty_cart_iv);
        rl_1 = view.findViewById(R.id.rl_1);


        if(global.Cart_List_Item.isEmpty()){

            empty_cart_iv.setVisibility(View.VISIBLE);
            rl_1.setVisibility(View.GONE);

        }else{
            rl_1.setVisibility(View.VISIBLE);

            empty_cart_iv.setVisibility(View.GONE);
        }



        listview_cart = view.findViewById(R.id.list_cart);

        adapterCartList = new AdapterCartList(getActivity(), global.Cart_List_Item,global.getCart_Offer(),global.Cart_Tax,
                tv_order_place,mProgressDialog,rl_1,empty_cart_iv);
        listview_cart.setAdapter(adapterCartList);

        return  view;
    }
}
