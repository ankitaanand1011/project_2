package com.sketch.developer.gowireless.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Developer on 3/13/18.
 */

public class SvedItemsRecyclerViewAdapter extends RecyclerView.Adapter<SvedItemsRecyclerViewAdapter.ViewHolder> {

    Global_Class global_class;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    ArrayList<DataModel> modelArrayList ;

    ImageLoader loader;
    Context context;
    DatabaseHelper databaseHelper;


    String name,price,imagename;

    // data is passed into the constructor
    public SvedItemsRecyclerViewAdapter(Context context, ArrayList<DataModel> modelArrayList) {
        this.mInflater = LayoutInflater.from(context);

        this.context = context;
        this.modelArrayList = modelArrayList;

        databaseHelper = new DatabaseHelper(context);
        global_class = (Global_Class)context.getApplicationContext();
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.savedrecyclerview_item, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String pro_data = modelArrayList.get(position).getPRODUCT_DATA();


        try {


            JSONObject object = new JSONObject(pro_data);

             name = object.getString("name");
             price = object.getString("price");
             imagename = object.getString("imagename_thumb");
            String short_description =object.getString("short_description");
            String id = object.getString("id");

        }catch (Exception e){
            e.printStackTrace();
        }


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


        Log.d("saveditem_adapter", "name: "+name);
        Log.d("saveditem_adapter", "price: "+price);
        Log.d("saveditem_adapter", "imagename: "+imagename);


        holder.name_tv.setText(name);
        holder.price_tv.setText(global_class.getCurrency_Symbol()+" "+price);
        loader.displayImage(imagename, holder.img_product , defaultOptions);

       // holder.img_favourite.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductDetail.class);
                intent.putExtra("data", modelArrayList.get(position).getPRODUCT_DATA());
                context.startActivity(intent);
            }
        });


        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseHelper.removeFromFAV(String.valueOf(modelArrayList.get(position).getP_ID()));

                modelArrayList.remove(position);

                notifyDataSetChanged();

            }
        });


    }

    // total number of cells
    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_tv,price_tv;
        ImageView img_delete,img_product;

        ViewHolder(View itemView) {
            super(itemView);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
            img_delete =  itemView.findViewById(R.id.img_delete);
            img_product =  itemView.findViewById(R.id.img_product);



        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());


            }
        }
    }

    // convenience method for getting data at click position
    /*String getItem(int id) {
        return list_product.size();
    }
*/
    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}