package com.sketch.developer.gowireless.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sketch.developer.gowireless.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Marketing on 9/22/2016.
 */
public class ProductViewPagerAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<String> sliderImagesId = new ArrayList<>();
    ImageView mImageView;
    ImageLoader loader;
    LayoutInflater mLayoutInflater;
    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;

    public ProductViewPagerAdapter(Context context, ArrayList<String> sliderImagesId_) {
        this.mContext = context;
        this.sliderImagesId = sliderImagesId_;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .showImageOnLoading(R.mipmap.app_icon)

                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        config = new ImageLoaderConfiguration.Builder(mContext.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();


    }

    @Override
    public int getCount() {
        return sliderImagesId.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((LinearLayout) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView mImageView = (ImageView) itemView.findViewById(R.id.imageView);

        loader.displayImage(sliderImagesId.get(i), mImageView , defaultOptions);

        container.addView(itemView, 0);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((LinearLayout) obj);
    }

}