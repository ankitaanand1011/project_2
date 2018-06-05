package com.sketch.developer.gowireless.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sketch.developer.gowireless.R;

/**
 * Created by Developer on 2/15/18.
 */

public class FragmentProfile extends Fragment {

    private ViewPager mViewPager;
    TabLayout tabs;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.removeAllViewsInLayout();
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabs = (TabLayout)view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);






        return  view;
    }
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.d("M", "adapter");
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                //mViewPager.setCurrentItem(tabLayout.getPosition(position));

                case 0:
                    Log.d("M", "adapter "+position);
                    FragMyProfile tab1 = new FragMyProfile();
                    return tab1;
                case 1:
                    FragMyOrder tab2 = new FragMyOrder();
                    Log.d("M", "mon");
                    return tab2;
                case 2:
                    FragSavedItems tab3 = new FragSavedItems();
                    Log.d("M", "tue");
                    return tab3;
                case 3:
                    FragSavedAddress tab4 = new FragSavedAddress();
                    Log.d("M", "wed");
                    return tab4;


                default:

                    FragMyProfile tab5 = new FragMyProfile();
                    return tab5;
            }

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.profile);
                case 1:
                    return getString(R.string.myorder);
                case 2:
                    return getString(R.string.saveditems);
                case 3:
                    return getString(R.string.savedaddress);

            }
            return null;
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            final TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(getPageTitle(position));

            return tab;
        }
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    /*private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFrag(new FragMyProfile(), "Profile");
        adapter.addFrag(new FragMyOrder(), "My Order");
        adapter.addFrag(new FragSavedItems(), "Saved Items");
        adapter.addFrag(new FragSavedAddress(), "Saved Address");

        viewPager.setAdapter(adapter);
    }*/
}
