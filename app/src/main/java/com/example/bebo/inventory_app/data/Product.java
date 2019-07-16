package com.example.bebo.inventory_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bebo.inventory_app.data.Contract.inventory;

public class Product extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="products.db";
    private String SQL_CREATE_TABLES = "CREATE TABLE "+ inventory.TABLE_NAME+"(" +
            inventory.ID+" INTEGER ," +
            inventory.name+" TEXT ," +
            inventory.quantity+" INTEGER ," +
            inventory.price+" INTEGER ," +
            inventory.supplierName+" TEXT ," +
            inventory.supplierPhone+" TEXT " +
            ");";
    public static final String SQL_DELETE_TABLE_PRODUCT_QUERY = "DROP TABLE " + inventory.TABLE_NAME + ";";

    public Product(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLES);
        Log.i(this.toString(), "onCreate: is active");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(this.toString(), "onUpgrade: is active");
        db.execSQL(SQL_DELETE_TABLE_PRODUCT_QUERY);
        db.execSQL(SQL_CREATE_TABLES);
    }
}
