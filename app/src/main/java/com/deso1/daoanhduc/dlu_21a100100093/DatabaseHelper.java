package com.deso1.daoanhduc.dlu_21a100100093;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deso1.daoanhduc.dlu_21a100100093.model.Food;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "product_db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_UNIT = "unit";  // Đơn vị tính
    private static final String COLUMN_IMAGE = "imagePath"; // Đường dẫn ảnh

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_UNIT + " TEXT, "
                + COLUMN_IMAGE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_UNIT + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_IMAGE + " TEXT");
        }
    }

    public void addProduct(String name, double price, String unit, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_IMAGE, imagePath);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Food> getAllProducts() {
        List<Food> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                String unit = cursor.getString(3);
                String imagePath = cursor.getString(4);
                productList.add(new Food(id, name, price, unit, imagePath));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public Food getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PRICE, COLUMN_UNIT, COLUMN_IMAGE},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Food product = new Food(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            cursor.close();
            return product;
        }
        return null;
    }

    // Cập nhật sản phẩm
    public void updateProduct(int id, String name, double price, String unit, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_IMAGE, imagePath);

        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

}

