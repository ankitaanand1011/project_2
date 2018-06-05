package com.sketch.developer.gowireless.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.activity.ProductDetail;
import com.sketch.developer.gowireless.utils.DataModel;
import com.sketch.developer.gowireless.utils.DatabaseHelper;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Developer on 3/13/18.
 */

public class ProductRecyclerViewAdapter extends
        RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

   // private String[] mData = new String[0];
    Global_Class global_class;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    ArrayList<HashMap<String, String>> list_product, cart_arraylist;
    ArrayList<HashMap<String, String>> ArrayList_Attributes;
    ArrayList<HashMap<String, String>> ArrayList_Available_Combination;
    ArrayList<HashMap<String, String>> ArrayList_Offer;
    ArrayList<HashMap<String, String>> ArrayList_Combination_Offer;

    ImageLoader loader;
    Context context;
    ArrayList<String> data_arraylist;

    DatabaseHelper databaseHelper;
    DataModel dataModel;

    // data is passed into the constructor
    public ProductRecyclerViewAdapter(Context context, ArrayList<HashMap<String, String>> list_product,
                                      ArrayList<HashMap<String, String>> ArrayList_Attributes,
                                      ArrayList<HashMap<String, String>> ArrayList_Available_Combination,
                                      ArrayList<HashMap<String, String>> ArrayList_Offer,
                                      ArrayList<HashMap<String, String>> ArrayList_Combination_Offer,
                                      ArrayList<String> data_arraylist) {
        this.mInflater = LayoutInflater.from(context);
        this.list_product = list_product;
        this.context = context;
        this.data_arraylist = data_arraylist;
        this.ArrayList_Attributes = ArrayList_Attributes;
        this.ArrayList_Available_Combination = ArrayList_Available_Combination;
        this.ArrayList_Offer = ArrayList_Offer;
        this.ArrayList_Combination_Offer = ArrayList_Combination_Offer;

        global_class = (Global_Class)context.getApplicationContext();
        databaseHelper = new DatabaseHelper(context);


    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String name = list_product.get(position).get("name");
        String price = list_product.get(position).get("price");
        String imagename = list_product.get(position).get("imagename");
        String short_description = list_product.get(position).get("short_description");
        String id = list_product.get(position).get("id");
        String final_price = list_product.get(position).get("final_price");

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


        holder.name_tv.setText(name);
        holder.price_tv.setText(global_class.getCurrency_Symbol()+" "+final_price);
        loader.displayImage(list_product.get(position).get("imagename_thumb"), holder.img_product , defaultOptions);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductDetail.class);
                intent.putExtra("data",data_arraylist.get(position));
                context.startActivity(intent);
            }
        });


        if (databaseHelper.isExistingData(list_product.get(position).get("id"))){
            // set golden color
            holder.img_favourite.setImageResource(R.mipmap.fav1_96);
        }else {
            // set normal
            holder.img_favourite.setImageResource(R.mipmap.fav96);
        }


        holder.img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (databaseHelper.isExistingData(list_product.get(position).get("id"))){
                    // remove
                 //   databaseHelper.removeFromFAV();

                    int rowid = databaseHelper.getRowId(list_product.get(position).get("id"));
                    if (rowid != 0){
                        databaseHelper.removeFromFAV(String.valueOf(rowid));
                    }


                }else {
                    // insert
                    dataModel = new DataModel();
                    dataModel.setPRODUCT_ID(list_product.get(position).get("id"));
                    dataModel.setPRODUCT_STOCK_ID(list_product.get(position).get("stock_id"));
                    dataModel.setPRODUCT_DATA(data_arraylist.get(position));
                    databaseHelper.insertData(dataModel);
                }

                notifyDataSetChanged();
            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return list_product.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_tv,price_tv;
        ImageView img_favourite,img_product;

        ViewHolder(View itemView) {
            super(itemView);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
            img_favourite =  itemView.findViewById(R.id.img_favourite);
            img_product =  itemView.findViewById(R.id.img_product);



        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());


            }
        }
    }


    /*void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }*/

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}