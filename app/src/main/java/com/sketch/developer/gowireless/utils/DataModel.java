package com.sketch.developer.gowireless.utils;

/**
 * Created by Developer on 3/16/18.
 */

public class DataModel {

    private int P_ID;
    private String PRODUCT_ID;
    private String PRODUCT_STOCK_ID;
    private String PRODUCT_DATA;

    public int getP_ID() {
        return P_ID;
    }

    public void setP_ID(int p_ID) {
        P_ID = p_ID;
    }

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(String PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public String getPRODUCT_STOCK_ID() {
        return PRODUCT_STOCK_ID;
    }

    public void setPRODUCT_STOCK_ID(String PRODUCT_STOCK_ID) {
        this.PRODUCT_STOCK_ID = PRODUCT_STOCK_ID;
    }

    public String getPRODUCT_DATA() {
        return PRODUCT_DATA;
    }

    public void setPRODUCT_DATA(String PRODUCT_DATA) {
        this.PRODUCT_DATA = PRODUCT_DATA;
    }
}
