package com.example.bebo.inventory_app;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bebo.inventory_app.data.Contract.inventory;

public class EditActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    EditText name ;
    EditText price ;
    EditText quantity ;
    EditText sup_name ;
    EditText sup_phone ;
    Button add ;
    Button update ;
    Button delete ;
    int PRODUCT_LOADER_VERSION = 101;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        name  = findViewById(R.id.name);
        price  = findViewById(R.id.price);
        quantity  = findViewById(R.id.quantity);
        sup_name  = findViewById(R.id.sup_name);
        sup_phone  = findViewById(R.id.sup_phone);

        add = findViewById(R.id.add);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);

        final Intent intent = getIntent();
        mUri = intent.getData();

        if ((mUri != null)) {
            //if it will update will go here
            setTitle(getString(R.string.edit_label));
            //get data from database and set in edits text
            getLoaderManager().initLoader(PRODUCT_LOADER_VERSION, null, this);

        } else {
            //else it will insert will go here
            //hidden contact , delete and save buttons when show insert layout
            delete.setVisibility(Button.GONE);
            update.setVisibility(Button.GONE);
            setTitle(getString(R.string.insert_label));

        }
        }
    private void insertProduct() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = name.getText().toString();
        String priceString = price.getText().toString();
        String quantityString = quantity.getText().toString();
        String sup_nameString = sup_name.getText().toString();
        String sup_phoneString = sup_phone.getText().toString();

        int priceInt = Integer.parseInt(priceString);
        int quantityInt = Integer.parseInt(quantityString);

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(inventory.name, nameString);
        values.put(inventory.price, priceInt);
        values.put(inventory.quantity, quantityInt);
        values.put(inventory.supplierName, sup_nameString);
        values.put(inventory.supplierPhone, sup_phoneString);

        // Insert a new pet into the provider, returning the content URI for the new pet.
        int resultUpdate = getContentResolver().update(mUri, values, null, null);

        //inform user state of process update
        if (resultUpdate == 0) {
            //if no row update inform by toast it
            Toast.makeText(EditActivity.this, R.string.toast_attention_null_values, Toast.LENGTH_SHORT).show();
        } else {
            //if it update done inform in toast
            Toast.makeText(EditActivity.this, R.string.toast_update_done, Toast.LENGTH_SHORT).show();

            //after it update go to home
            finish();
        }

    }

    public void add(View view) {
        insertProduct();
    }

    public void update(View view) {

    }

    public void delete(View view) {
        createAlertDialogDelete();
    }

    public void createAlertDialogDelete() {
        //create alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set message that show in alert Dialog
        builder.setMessage(R.string.message_delete_alert);

        //create positive button and set name and click listener
        builder.setPositiveButton(R.string.delete_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do delete the product
                int rowDelete = getContentResolver().delete(mUri, null, null);

                //Toast show depending in delect successful or not
                if (rowDelete == 0) {
                    //if row deleted is 0 then the delete have with problem
                    Toast.makeText(getBaseContext(), R.string.toast_message_delete_faile, Toast.LENGTH_SHORT).show();
                } else {
                    //otherwise , the delete was successful and we can display the toast
                    Toast.makeText(getBaseContext(), R.string.toast_message_delete_done, Toast.LENGTH_SHORT).show();
                }

                //go to home
                finish();
            }
        });

        //create negative button with name and click listener
        builder.setNegativeButton(R.string.delete_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (dialogInterface != null) {
                    //if user click negative button then dismiss the alert dialog
                    dialogInterface.dismiss();
                }
            }
        });
        //create alert dialog by builder
        AlertDialog alertDialog = builder.create();

        //show alert dialog
        alertDialog.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //check if muri is null or not
        if (mUri != null) {
            //if muri is not null then we in update layout
            //should hidden item of insert in database
            //get item of insert in database
            MenuItem menuItem = menu.findItem(R.id.action_save_insert);

            //hidden it item
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get menu inflater and inflate the menu editor
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get it of item
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save_insert:
                //associate xml by java
                //get string from edit text for name and price and amount and supplier name and supplier phone
                String nameS = name.getText().toString().trim();
                String priceS = price.getText().toString().trim();
                String quantityS = quantity.getText().toString().trim();
                String supplierNameS = sup_name.getText().toString().trim();
                String supplierPhoneS = sup_phone.getText().toString().trim();

                //create content values
                ContentValues contentValues = new ContentValues();

                //set key and value in content value
                contentValues.put(inventory.name, nameS);
                contentValues.put(inventory.price, priceS);
                contentValues.put(inventory.quantity, quantityS);
                contentValues.put(inventory.supplierName, supplierNameS);
                contentValues.put(inventory.supplierPhone, supplierPhoneS);

                //insert values in database
                Uri uriInsertResult = getContentResolver().insert(inventory.CONTENT_URI, contentValues);

                if (uriInsertResult == null) {
                    //insert failed
                    Toast.makeText(this, R.string.toast_attention_null_values, Toast.LENGTH_SHORT).show();
                } else {
                    //insert done
                    Toast.makeText(this, R.string.toast_insert_done, Toast.LENGTH_SHORT).show();
                    //go to home
                    finish();
                }


            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String projection[] = {inventory.name,
                inventory.price,
                inventory.quantity,
                inventory.supplierName,
                inventory.supplierPhone
        };

        //create the loader and send context and my uri and projection to work in background
        return new ProductLoader(this, mUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {

            //get index of name , price , amount ,supplier name and phone name
            int nameIndex = cursor.getColumnIndex(inventory.name);
            int priceIndex = cursor.getColumnIndex(inventory.price);
            int amountIndex = cursor.getColumnIndex(inventory.quantity);
            int supplierNameIndex = cursor.getColumnIndex(inventory.supplierName);
            int supplierPhoneIndex = cursor.getColumnIndex(inventory.supplierPhone);

            //get text from cursor for index name , price , amount ,supplier name and phone name  and save in string
            String nameS = cursor.getString(nameIndex);
            String priceS = cursor.getString(priceIndex);
            String quantityS = cursor.getString(amountIndex);
            String supplierNameS = cursor.getString(supplierNameIndex);
            String supplierPhoneS = cursor.getString(supplierPhoneIndex);

            //set text in edit text special
            name.setText(nameS);
            price.setText(priceS);
            quantity.setText(quantityS);
            sup_name.setText(supplierNameS);
            sup_phone.setText(supplierPhoneS);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        name.setText("");
        price.setText("");
        quantity.setText("");
        sup_name.setText("");
        sup_phone.setText("");
    }




}

