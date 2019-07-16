package com.example.bebo.inventory_app;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.bebo.inventory_app.data.Contract.inventory;


public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    ListView newsListView;
    private SetAdapter adapter;
    Cursor cursor;
    Button fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new SetAdapter(this , null);
        newsListView = findViewById(R.id.listview);
        newsListView.setEmptyView(findViewById(R.id.empty_view));
        newsListView.setAdapter(adapter);
        int LOADER_VERSION = 100;
        getLoaderManager().initLoader(LOADER_VERSION, null ,  this);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                //intent to go editor activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                //upload the url in intent
                intent.setData(ContentUris.withAppendedId(inventory.CONTENT_URI, id));

                //start activity
                startActivity(intent);
            }
        });
        fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to go editor activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                //start activity
                startActivity(intent);
            }
        });

    }



    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                inventory.ID,
                inventory.name,
                inventory.price,
                inventory.quantity,
                inventory.supplierName,
                inventory.supplierPhone
        };

        return new ProductLoader(this ,
                inventory.CONTENT_URI ,
                projection ,
                null ,
                null ,
                null);


    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
