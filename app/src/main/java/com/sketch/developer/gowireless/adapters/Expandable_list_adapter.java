package com.sketch.developer.gowireless.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.fragments.FragmentProductList;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Developer on 7/25/16.
 */
public class Expandable_list_adapter extends BaseExpandableListAdapter {


    private Context _context;
    private ArrayList<HashMap<String, String>> _listDataHeader; // header titles
    ArrayList<HashMap<String, HashMap<String, String>>> _listDataChild;
    ArrayList<String> list_keyvalue;

    FragmentManager manager;
    ProgressDialog mProgressDialog;
    DrawerLayout mDrawerLayout;

    private int selectedIndex;

    public Expandable_list_adapter(Context context, ArrayList<HashMap<String, String>> listDataHeader,
                                   ArrayList<HashMap<String, HashMap<String, String>>> listChildData ,
                                   ArrayList<String> list_keyvalue,FragmentManager manager_,
                                   DrawerLayout mDrawerLayout_) {

        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.list_keyvalue = list_keyvalue;
        this.manager = manager_;
        this.mDrawerLayout = mDrawerLayout_;


        mProgressDialog = new ProgressDialog(_context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Please wait...");



    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        String key_child = _listDataHeader.get(groupPosition).get("id")+childPosititon;

        return this._listDataChild.get(childPosititon).get(key_child);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final HashMap<String, String> hashMap_child = (HashMap<String, String>) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_child_list, null);

        }

        final TextView tv_child =  convertView.findViewById(R.id.title);


        String child_parentid =  hashMap_child.get("child_parentid");
        final String child_name =  hashMap_child.get("child_name");

        tv_child.setText(child_name);

        tv_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDrawerLayout.closeDrawer(GravityCompat.START);



                String child_id =  hashMap_child.get("child_id");
                Bundle bundle =new Bundle();
                bundle.putString("id",child_id);
                bundle.putString("child_name",child_name);
                FragmentProductList  fragment = new FragmentProductList();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment);
               // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });




        return convertView;
    }



    @Override
    public int getChildrenCount(int groupPosition) {

        String categoryId_ = _listDataHeader.get(groupPosition).get("id");
       // Log.d(global.TAG, "categoryId_   " +categoryId_ );

        int size = 0;

        for (int i = 0; i < _listDataChild.size() ; i++){

            for ( int j = 0; j < _listDataChild.size(); j++){

                if ((categoryId_+i).equals(list_keyvalue.get(j)) ){

                    size++;
                }

            }

        }

       // Log.d(global.TAG, "size   " +size );

        return size;
    }


    public void setSelectedIndex(int ind) {
        selectedIndex = ind;
        notifyDataSetChanged();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
       // String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_parent_list, null);
        }

        TextView lblListHeader =  convertView.findViewById(R.id.tv_parent);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(_listDataHeader.get(groupPosition).get("name"));


        ImageView iv_indicator = convertView.findViewById(R.id.iv_indicator);

        if ( getChildrenCount( groupPosition ) == 0 ) {
            iv_indicator.setVisibility( View.INVISIBLE );
        }
        else {
            iv_indicator.setVisibility( View.VISIBLE );

            if (isTablet(_context)){

                iv_indicator.setImageResource( isExpanded ? R.mipmap.minus96 : R.mipmap.plus96 );

            }else {

                iv_indicator.setImageResource( isExpanded ? R.mipmap.minus96 : R.mipmap.plus96 );
            }

        }

        if (groupPosition == selectedIndex) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.pressed_color));

            iv_indicator.setImageResource( isExpanded ? R.mipmap.minus96 : R.mipmap.plus96 );
        }
        else {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.colorAccent));

            iv_indicator.setImageResource( isExpanded ? R.mipmap.minus96 : R.mipmap.plus96 );
        }



        lblListHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedIndex(groupPosition);

                mDrawerLayout.closeDrawer(GravityCompat.START);

                String id=  _listDataHeader.get(groupPosition).get("id");
                String name = _listDataHeader.get(groupPosition).get("name");

             //   String child_id =  hashMap_child.get("child_id");
                Bundle bundle =new Bundle();
                bundle.putString("id",id);
                bundle.putString("name",name);
                FragmentProductList  fragment = new FragmentProductList();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment);
              //  fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        setSelectedIndex(groupPosition);
        String cat_id = _listDataHeader.get(groupPosition).get("id");
        super.onGroupExpanded(groupPosition);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }






}


