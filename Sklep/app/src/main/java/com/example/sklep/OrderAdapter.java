package com.example.sklep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Order> orders;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_list_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.name_text_view);
        TextView detailsTextView = convertView.findViewById(R.id.details_text_view);

        Order order = (Order) getItem(position);
        nameTextView.setText(order.getName());
        String details = "Komputer: " + order.getComputerName() + "\n" +
                "Mysz: " + (order.getMouseName() != null ? order.getMouseName() : "brak") + "\n" +
                "Klawiatura: " + (order.getKeyboardName() != null ? order.getKeyboardName() : "brak") + "\n" +
                "Kamera: " + (order.getWebcamName() != null ? order.getWebcamName() : "brak") + "\n" +
                "Ilość: " + order.getQuantity() + "\n" +
                "Suma: " + order.getTotalPrice() + "zł\n" +
                "Data i czas: " + order.getDateTime();
        detailsTextView.setText(details);

        return convertView;
    }
}
