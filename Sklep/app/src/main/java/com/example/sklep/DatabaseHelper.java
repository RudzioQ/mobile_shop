package com.example.sklep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shop.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ORDERS = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "computer TEXT, " +
                "mouse TEXT, " +
                "keyboard TEXT, " +
                "webcam TEXT, " +
                "name TEXT, " +
                "email TEXT, " +
                "phone TEXT, " +
                "quantity INTEGER, " +
                "order_datetime TEXT, " +
                "totalPrice INTEGER)";

        db.execSQL(CREATE_TABLE_ORDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);
    }

    public void insertOrder(String computer, String mouse, String keyboard, String webcam, String name, String email, String phone, int quantity, String orderDatetime, int totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("computer", computer);
        values.put("mouse", mouse);
        values.put("keyboard", keyboard);
        values.put("webcam", webcam);
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("quantity", quantity);
        values.put("order_datetime", orderDatetime);
        values.put("totalPrice", totalPrice);

        db.insert("orders", null, values);
        db.close();
    }

    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM orders", null);
    }
}
