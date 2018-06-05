package com.sketch.developer.gowireless.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.fragments.FragmentProductList;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Developer on 2/15/18.
 */

public class AdapterHomeGrid extends RecyclerView.Adapter<AdapterHomeGrid.MyViewHolder> {


    Context context;
    ArrayList<String> list_keyvalue;
    ArrayList<HashMap<String, String>> List_Header_1;
    ArrayList<HashMap<String, HashMap<String, String>>> Arraylist_DataChild;
    ImageLoader loader;
    FragmentManager manager_;

    public AdapterHomeGrid( Context context_, ArrayList<HashMap<String, String>> List_Header_1,
                            ArrayList<HashMap<String, HashMap<String, String>>> Arraylist_DataChild,
                            ArrayList<String> list_keyvalue,FragmentManager manager_) {
        this.context = context_;
        this.List_Header_1 = List_Header_1;
        this.Arraylist_DataChild = Arraylist_DataChild;
        this.list_keyvalue = list_keyvalue;
        this.manager_ = manager_;


    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_home, parent, false);
        // set the view's size, margins, paddings and layout parameters

// pass the view to View Holder
        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();


        String name = (String) List_Header_1.get(position).get("name");


        Log.d("TAG", "name: "+name);
        Log.d("TAG", "iamge: "+List_Header_1.get(position).get("imagethumb"));

     holder.name.setText(name);

        int type=position;
        //For each row processing respectively
        switch(type){
            case 0:
                holder.itemView.setBackgroundColor(Color.parseColor("#B03A2E"));
                break;
            case 1:
                holder.itemView.setBackgroundColor(Color.parseColor("#717D7E"));
                break;
            case 2:
                holder.itemView.setBackgroundColor(Color.parseColor("#F1C40F"));
                break;
            case 3:
                holder.itemView.setBackgroundColor(Color.parseColor("#27AE60"));
                break;
            case 4:
                holder.itemView.setBackgroundColor(Color.parseColor("#3498DB"));
                break;
            case 5:
                holder.itemView.setBackgroundColor(Color.parseColor("#8E44AD"));
                break;
            case 6:
                holder.itemView.setBackgroundColor(Color.parseColor("#16A085"));
                break;
        }


        loader.displayImage(List_Header_1.get(position).get("imagethumb"), holder.image , defaultOptions);

      //  final HashMap<String, String> hashMap_child = (HashMap<String, String>) getChild(groupPosition, childPosition);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id =  List_Header_1.get(position).get("id");
                Bundle bundle =new Bundle();
                bundle.putString("id",id);
                bundle.putString("from","home");
                FragmentProductList fragment = new FragmentProductList();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = manager_.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return List_Header_1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
