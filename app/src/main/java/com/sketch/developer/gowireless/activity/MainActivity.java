package com.sketch.developer.gowireless.activity;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.fragments.FragmentCart;
import com.sketch.developer.gowireless.fragments.FragmentDrawer;
import com.sketch.developer.gowireless.fragments.FragmentHome;
import com.sketch.developer.gowireless.fragments.FragmentOffer;
import com.sketch.developer.gowireless.fragments.FragmentProfile;
import com.sketch.developer.gowireless.fragments.FragmentSearch;
import com.sketch.developer.gowireless.utils.DatabaseHelper;
import com.sketch.developer.gowireless.utils.Global_Class;
import com.splunk.mint.Mint;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    Global_Class global_class;
    Fragment fragment;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new FragmentHome());

        global_class = (Global_Class)getApplicationContext();
        global_class.setCurrency_Symbol("₹");


        Mint.initAndStartSession(this.getApplication(), "0ae6c687");


        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        //databaseHelper.deleteAll();

        fragment = new FragmentHome();
        loadFragment(fragment);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.home:

                               /* FragmentHome home = new FragmentHome();
                                android.support.v4.app.FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction1.replace(R.id.contentContainer, home);
                                fragmentTransaction1.commit();*/

                                fragment = new FragmentHome();
                                loadFragment(fragment);
                                return true;

                            case R.id.offer:

                              /*  FragmentOffer offer = new FragmentOffer();
                                android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction2.replace(R.id.contentContainer, offer);
                                fragmentTransaction2.commit();*/
                                fragment = new FragmentOffer();
                                loadFragment(fragment);
                                return true;

                            case R.id.cart:

//                                FragmentCart cart = new FragmentCart();
//                                android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//                                fragmentTransaction3.replace(R.id.contentContainer, cart);
//                                fragmentTransaction3.commit();
//
                                fragment = new FragmentCart();
                                loadFragment(fragment);
                                return true;

                            case R.id.search:
/*
                                FragmentSearch search = new FragmentSearch();
                                android.support.v4.app.FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction4.replace(R.id.contentContainer, search);
                                fragmentTransaction4.commit();*/

                                fragment = new FragmentSearch();
                                loadFragment(fragment);
                                return true;

                            case R.id.profile:
/*

                                FragmentProfile profile = new FragmentProfile();
                                android.support.v4.app.FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction5.replace(R.id.contentContainer, profile);
                                fragmentTransaction5.commit();
*/

                                fragment = new FragmentProfile();
                                loadFragment(fragment);
                                return true;


                            default:
                                fragment = new FragmentHome();
                                loadFragment(fragment);
                                return true;


                        }

                    }
                });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.home:

                               /* FragmentHome home = new FragmentHome();
                                android.support.v4.app.FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction1.replace(R.id.contentContainer, home);
                                fragmentTransaction1.commit();*/

                        fragment = new FragmentHome();
                        loadFragment(fragment);

                    case R.id.offer:

                              /*  FragmentOffer offer = new FragmentOffer();
                                android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction2.replace(R.id.contentContainer, offer);
                                fragmentTransaction2.commit();*/
                        fragment = new FragmentOffer();
                        loadFragment(fragment);

                    case R.id.cart:

//                                FragmentCart cart = new FragmentCart();
//                                android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//                                fragmentTransaction3.replace(R.id.contentContainer, cart);
//                                fragmentTransaction3.commit();
//
                        fragment = new FragmentCart();
                        loadFragment(fragment);

                    case R.id.search:
/*
                                FragmentSearch search = new FragmentSearch();
                                android.support.v4.app.FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction4.replace(R.id.contentContainer, search);
                                fragmentTransaction4.commit();*/

                        fragment = new FragmentSearch();
                        loadFragment(fragment);

                    case R.id.profile:
/*

                                FragmentProfile profile = new FragmentProfile();
                                android.support.v4.app.FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction5.replace(R.id.contentContainer, profile);
                                fragmentTransaction5.commit();
*/

                        fragment = new FragmentProfile();
                        loadFragment(fragment);

                    default:

                        fragment = new FragmentHome();
                        loadFragment(fragment);


                }


            }
        });









        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_menu96);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(global_class.cartclicked){

            fragment = new FragmentCart();
            loadFragment(fragment);

            global_class.cartclicked=false;
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;


        Toasty.info(MainActivity.this,"Tap back button in order to exit", Toast.LENGTH_SHORT,true).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
