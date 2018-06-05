package com.sketch.developer.gowireless.utils;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Developer on 2/22/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {




    private static final String DATABASE_NAME = "Go-Wireless.db";
    private static final int    DATABASE_VERSION = 1;

    private static final String FAV_TABLE = "fav_table";
    private static final String P_ID = "p_id";

    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_STOCK_ID = "product_stock_id";
    private static final String PRODUCT_DATA = "product_data";




    private static final String STRING_PRODUCT = "CREATE TABLE " + FAV_TABLE + "("
            + P_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PRODUCT_ID+" TEXT,"
            + PRODUCT_STOCK_ID+" TEXT,"
            + PRODUCT_DATA+" TEXT)";






    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(STRING_PRODUCT);
        Log.d("my_db", "PRODUCT table created. ");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ FAV_TABLE);
        Log.d("my_db", "PRODUCT_TABLE  upgrade. ");


    }


    public  void insertData(DataModel dataModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PRODUCT_ID, dataModel.getPRODUCT_ID());
        values.put(PRODUCT_STOCK_ID, dataModel.getPRODUCT_STOCK_ID());
        values.put(PRODUCT_DATA, dataModel.getPRODUCT_DATA());


        db.insert(FAV_TABLE, null, values);
        Log.d("DB", "insert in FAV_TABLE");
        db.close();
    }


    public ArrayList<DataModel> getAllData(){


        ArrayList<DataModel> modelArrayList = new ArrayList<>();
        DataModel dataModel;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FAV_TABLE, null, null, null,null,null,null);
        Log.d("DB", "PRODUCT_TABLE Size = "+cursor.getCount());

        while(cursor.moveToNext()){
            dataModel = new DataModel();

            int id = cursor.getInt(cursor.getColumnIndex(P_ID));
            String p_id = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
            String p_s_id = cursor.getString(cursor.getColumnIndex(PRODUCT_STOCK_ID));
            String p_data = cursor.getString(cursor.getColumnIndex(PRODUCT_DATA));

            dataModel.setP_ID(id);
            dataModel.setPRODUCT_ID(p_id);
            dataModel.setPRODUCT_STOCK_ID(p_s_id);
            dataModel.setPRODUCT_DATA(p_data);


            modelArrayList.add(dataModel);
        }


        cursor.close();
        db.close();

        return modelArrayList;

    }


    public boolean isExistingData(String pdroduct_id) {

        String[] columns = {PRODUCT_ID};
        String selection = PRODUCT_ID + " =?";
        String[] selectionArgs = {pdroduct_id};
        String limit = "1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FAV_TABLE, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);

        db.close();
        cursor.close();

        Log.d("DB", "DB l = "+cursor.getCount());

        return exists;
    }


    public int getRowId(String product_id) {

        Log.d("DB", "ccccc = "+product_id);

        int id = 0;
       // String[] columns = {PRODUCT_ID};
        String selection = PRODUCT_ID + " =?";
        String[] selectionArgs = {product_id};
        String limit = "1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FAV_TABLE, null, selection, selectionArgs, null, null, null, limit);

        Log.d("DB", "ccccc = "+cursor.getCount());

        if(cursor != null) {
            if (cursor.getCount() > 0){
                if(cursor.moveToFirst()){
                    id = cursor.getInt(cursor.getColumnIndex(P_ID));
                }
            }
        }

        cursor.close();
        db.close();

        return id;
    }


    public void removeFromFAV(String rowId){

        SQLiteDatabase db = this.getWritableDatabase();

        String where = P_ID +" =?";
        String[] whereArg = {rowId};

        db.delete(FAV_TABLE, where, whereArg);
        db.close();

        Log.d("DB", "PRODUCT_TABLE update successful");
    }





    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAV_TABLE,null,null);
        db.close();
    }



}
