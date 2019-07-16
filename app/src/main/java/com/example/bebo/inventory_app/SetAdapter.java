package com.example.bebo.inventory_app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.bebo.inventory_app.data.Contract.inventory;


class SetAdapter extends CursorAdapter {
    private Context mContext;

   SetAdapter(Context context, Cursor cursor) {
       super(context, cursor );
       mContext=context;
   }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.list_item ,parent , false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView Name = view.findViewById(R.id.name);
        TextView price = view.findViewById(R.id.price);
        TextView quantity = view.findViewById(R.id.quantity);
//        TextView sup_name = view.findViewById(R.id.webPublicationDate);
//        TextView sup_phone = view.findViewById(R.id.webPublicationDate);

        Name.setText("section name : " + cursor.getString(cursor.getColumnIndex(inventory.name)));
        price.setText("description : " + cursor.getString(cursor.getColumnIndex(inventory.price)));
        quantity.setText("author name : " + cursor.getString(cursor.getColumnIndex(inventory.quantity)));
//        sup_name.setText("date : " + item.getWebPublicationDate());
//        sup_phone.setText("date : " + item.getWebPublicationDate());

    }
}
