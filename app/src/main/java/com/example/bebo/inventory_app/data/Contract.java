package com.example.bebo.inventory_app.data;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
public final class Contract {

    public static final String Content = "com.example.bebo.inventory_app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + Content);
    public static final String PATH_products = "produc";

    public static final class inventory implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_products);

        public final static String TABLE_NAME = "products";

        public final static String ID = BaseColumns._ID;

        public final static String name = "name";

        public final static String price = "price";

        public final static String quantity = "quantity";

        public final static String supplierName = "supplier_name";

        public final static String supplierPhone = "supplier_phone";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Content + "/" + PATH_products;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Content + "/" + PATH_products;
    }
}
