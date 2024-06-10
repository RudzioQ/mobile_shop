package com.example.sklep;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    private ListView ordersListView;
    private OrderAdapter orderAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        setSupportActionBar(findViewById(R.id.toolbar));

        ordersListView = findViewById(R.id.orders_list_view);
        dbHelper = new DatabaseHelper(this);

        ArrayList<Order> orders = fetchOrdersFromDatabase();
        orderAdapter = new OrderAdapter(this, orders);
        ordersListView.setAdapter(orderAdapter);
    }

    private ArrayList<Order> fetchOrdersFromDatabase() {
        ArrayList<Order> orders = new ArrayList<>();
        Cursor cursor = dbHelper.getAllOrders();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String computerName = cursor.getString(cursor.getColumnIndexOrThrow("computer"));
                String mouseName = cursor.getString(cursor.getColumnIndexOrThrow("mouse"));
                String keyboardName = cursor.getString(cursor.getColumnIndexOrThrow("keyboard"));
                String webcamName = cursor.getString(cursor.getColumnIndexOrThrow("webcam"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                int totalPrice = cursor.getInt(cursor.getColumnIndexOrThrow("totalPrice"));
                String dateTime = cursor.getString(cursor.getColumnIndexOrThrow("order_datetime"));

                orders.add(new Order(name, email, phone, computerName, mouseName, keyboardName, webcamName, quantity, totalPrice, dateTime));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return orders;
    }
}
