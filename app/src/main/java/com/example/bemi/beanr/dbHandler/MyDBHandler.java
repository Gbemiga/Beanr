package com.example.bemi.beanr.dbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bemi.beanr.entites.Business;
import com.example.bemi.beanr.entites.Customer;
import com.example.bemi.beanr.entites.FavouriteShop;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "customer.db";
    public static final String TABLE_NAME = "customer";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_GENDER = "gender";
    public static final String TABLE_NAME2 = "favouriteShop";
    public static final String COLUMN_BUSINESS_NAME = "businessName";

    //We need to pass database information along to superclass
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_GENDER + " TEXT " +
                ");";
        String query2 = "CREATE TABLE " + TABLE_NAME2 + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_BUSINESS_NAME + " TEXT "+
                ");";
        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add a new row to the database
    public void addCustomer(Customer customer){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, customer.getUsername());
        values.put(COLUMN_EMAIL, customer.getEmail());
        values.put(COLUMN_GENDER, customer.getGender());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addFavouriteShop(Customer customer, Business business){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, customer.getUsername());
        values.put(COLUMN_BUSINESS_NAME, business.getName());
        System.out.println(business.getName()+"IN MY HANDLER");
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME2, null, values);
        db.close();
    }



    //Delete a user from the database
    public void deleteCustomer(String username) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=\"" + username + "\";");
    }

    public void deleteAllFavourite() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME2 + ";");
    }

    public void deleteAllCustomer() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }


    public Customer getCustomer(){

        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        // String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";
        String query = "SELECT * FROM " + TABLE_NAME;
        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
        Customer customer = new Customer();
        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("username")) != null) {
                dbString = c.getString(c.getColumnIndex("username"));
                customer.setUsername(dbString);
            }
            if (c.getString(c.getColumnIndex("email")) != null) {
                dbString = c.getString(c.getColumnIndex("email"));
                customer.setEmail(dbString);
            }
            if (c.getString(c.getColumnIndex("gender")) != null) {
                dbString = c.getString(c.getColumnIndex("gender"));
                customer.setGender(dbString);
            }
            c.moveToNext();
            System.out.println(c.getCount() + "CURSORRR");
        }
        db.close();
        return customer;
    }

    public ArrayList<FavouriteShop> getFavouriteShops(){
        ArrayList<FavouriteShop>  favouriteShops = new ArrayList<>();
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        // String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";
        String query = "SELECT * FROM " + TABLE_NAME2;
        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
//        FavouriteShop favouriteShop = new FavouriteShop();
        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            FavouriteShop favouriteShop = new FavouriteShop();
            if (c.getString(c.getColumnIndex("username")) != null) {
                dbString = c.getString(c.getColumnIndex("username"));

                favouriteShop.setCustomerName(dbString);
            }
            if (c.getString(c.getColumnIndex("businessName")) != null) {
                dbString = c.getString(c.getColumnIndex("businessName"));
                System.out.println(dbString+"Trying to Gettttttt");
                favouriteShop.setBusinessName(dbString);
            }
            favouriteShops.add(favouriteShop);
            c.moveToNext();
            System.out.println(c.getCount() + "CURSORRR");
        }
        db.close();
        System.out.println(favouriteShops.toString()+ "END ");
        return favouriteShops;
    }

}