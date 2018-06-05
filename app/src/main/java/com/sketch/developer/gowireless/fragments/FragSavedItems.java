package com.sketch.developer.gowireless.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.adapters.SvedItemsRecyclerViewAdapter;
import com.sketch.developer.gowireless.utils.DataModel;
import com.sketch.developer.gowireless.utils.DatabaseHelper;
import com.sketch.developer.gowireless.utils.Global_Class;

import java.util.ArrayList;

/**
 * Created by Developer on 2/15/18.
 */

public class FragSavedItems extends Fragment {

    Global_Class globalClass;
    ProgressDialog pd;
    String id;
    // ArrayList<HashMap<String, String>> list_product;
    RecyclerView rvNumbers;
    ArrayList<String> data_arraylist;

    SvedItemsRecyclerViewAdapter adapter;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.frag_saved_items, container, false);


        globalClass = (Global_Class)getActivity().getApplicationContext();

        pd=new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");


        data_arraylist = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getActivity());

        rvNumbers = (RecyclerView) view.findViewById(R.id.rvNumbers);
        int numberOfColumns = 2;
        rvNumbers.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        ArrayList<DataModel> modelArrayList = databaseHelper.getAllData();

        Log.d("sa", "modelArrayList: "+modelArrayList.size());



        adapter = new SvedItemsRecyclerViewAdapter(getActivity(), modelArrayList);
        rvNumbers.setAdapter(adapter);



        return  view;
    }

}
