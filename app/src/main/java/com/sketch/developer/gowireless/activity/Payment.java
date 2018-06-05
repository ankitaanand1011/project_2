package com.sketch.developer.gowireless.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketch.developer.gowireless.R;
import com.sketch.developer.gowireless.utils.Global_Class;

/**
 * Created by Developer on 2/15/18.
 */

public class Payment extends AppCompatActivity {
    RecyclerView rcv_colors, rcv_size;
    AppCompatButton btn_add_to_cart;
    TextView tv_delivery_val,tv_done;
    Global_Class global_class;
    ImageView img_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payments);

        global_class = (Global_Class)getApplicationContext();

        tv_delivery_val = findViewById(R.id.tv_delivery_val);
        img_back = findViewById(R.id.img_back);
        tv_done = findViewById(R.id.tv_done);

        global_class.Cart_List_Item.clear();

        global_class.setCURRENT_TOTAL(0);
        global_class.setGRAND_TOTAL(0);

        tv_delivery_val.setText(global_class.getSaved_address());

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Payment.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() { }
}
