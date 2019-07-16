package com.example.bebo.inventory_app.data;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.example.bebo.inventory_app.data.Contract.inventory;

@SuppressLint("Registered")
public class Provider extends ContentProvider {
    Product product;
    private static final int product_table = 100;
    private static final int product_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.Content,Contract.PATH_products , product_table);
        sUriMatcher.addURI(Contract.Content,Contract.PATH_products +"/#", product_ID);

    }
    @Override
    public boolean onCreate() {
        product = new Product(getContext());
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = product.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match){
            case product_table :
                cursor = db.query(inventory.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                ContentValues contentValues = new ContentValues();
                contentValues.put(inventory.name, "abanoub");
                contentValues.put(inventory.price, 3);
                contentValues.put(inventory.quantity, 4);
                contentValues.put(inventory.supplierName, "man");
                contentValues.put(inventory.supplierPhone, "01275");
                break;
            case product_ID :
                selection = inventory.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(inventory.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("cannot query unknown "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case product_table:
                return inventory.CONTENT_LIST_TYPE;
            case product_ID:
                return inventory.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        Uri resultUri;
        String name = values.getAsString(inventory.name);
        float price = values.getAsFloat(inventory.price);
        Integer quantity = values.getAsInteger(inventory.quantity);
        String sup_name = values.getAsString(inventory.supplierName);
        String sup_phone = values.getAsString(inventory.supplierPhone);
        if (name == null) {
            throw new IllegalArgumentException("product requires a name");
        }
        else if (price < 0){
            throw new IllegalArgumentException("price must be positive");
        }
        else if (quantity < 0 && quantity != null){
            throw new IllegalArgumentException("quantity must be positive");
        }
        else if (sup_name == null){
            throw new IllegalArgumentException("supplier name required");
        }
        else if (sup_phone == null){
            throw new IllegalArgumentException("supplier phone required");
        }
        else {
            switch (match) {
                case product_table:
                    resultUri = insertProduct(uri, values);
                    break;
                default:
                    throw new IllegalArgumentException("Insertion is not supported for " + uri);
            }
        }
        if (resultUri == null)
            return null;
        return resultUri;
    }

    private Uri insertProduct(Uri uri, ContentValues contentValues) {



        SQLiteDatabase sqLiteDatabase = product.getWritableDatabase();
        long id = sqLiteDatabase.insert(inventory.TABLE_NAME, null, contentValues);
        if (id == -1) {
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = product.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int result;
        switch (match) {
            case product_table:
                result = deleteProduct(uri, selection, selectionArgs);
                break;
            case product_ID:
                // Delete a single row given by the ID in the URI
                selection = inventory._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                result = deleteProduct(uri, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        return result;
    }

    private int deleteProduct(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = product.getWritableDatabase();
        int rowsDeleted = sqLiteDatabase.delete(inventory.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int result ;
        switch (match) {
            case product_table:
                result = updateProduct(uri, values, selection, selectionArgs);
                break;
            case product_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = inventory._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                result = updateProduct(uri, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        return result ;
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        SQLiteDatabase sqLiteDatabase = product.getWritableDatabase();
        int rowsUpdated = sqLiteDatabase.update(inventory.TABLE_NAME, values, selection, selectionArgs);
        String name = values.getAsString(inventory.name);
        float price = values.getAsFloat(inventory.price);
        Integer quantity = values.getAsInteger(inventory.quantity);
        String sup_name = values.getAsString(inventory.supplierName);
        String sup_phone = values.getAsString(inventory.supplierPhone);
        if (name == null) {
            throw new IllegalArgumentException("product requires a name");
        }
        else if (price < 0){
            throw new IllegalArgumentException("price must be positive");
        }
        else if (quantity < 0 && quantity != null){
            throw new IllegalArgumentException("quantity must be positive");
        }
        else if (sup_name == null){
            throw new IllegalArgumentException("supplier name required");
        }
        else if (sup_phone == null){
            throw new IllegalArgumentException("supplier phone required");
        }
        else {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    }