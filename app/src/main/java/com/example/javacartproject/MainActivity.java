package com.example.javacartproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ProductTableDataGateway dataGateway;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dataGateway = new ProductTableDataGateway(this);
        db = dataGateway.getWritableDatabase();


        db.delete(ProductTableDataGateway.PRODUCT_TABLE_NAME, null, null);
        SeedDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate main_menu.xml to add [Home] & [Cart] items
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        Cursor cursor = db.query(true, ProductTableDataGateway.PRODUCT_TABLE_NAME, new String[]{ProductTableDataGateway.PRODUCT_COLUMN_CATEGORY},
                null, null, null, null, null, null);


        int index = 0;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(ProductTableDataGateway.PRODUCT_COLUMN_CATEGORY));
            menu.add(Menu.NONE, Menu.FIRST + index, index, category);
            index++;
        }


        cursor.close();
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {

            case R.id.home_button:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;


            case R.id.cart_button:
                intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;


            default:
                intent = new Intent(this, ProductsListActivity.class);
                intent.putExtra("category", item.getTitle());
                intent.putExtra("databaseName", dataGateway.getDatabaseName());
                startActivity(intent);
                break;
        }
        return true;
    }


    public void SeedDatabase() {

        Product[] products = {
                new Product("trai cay", "tao", 1, R.drawable.apple),
                new Product("trai cay", "chuoi", 2, R.drawable.banana),
                new Product("trai cay", "Ca rot", 3, R.drawable.carrot),
                new Product("trai cay", "no", 4, R.drawable.lettuce),
                new Product("thuc pham", "sua", 1, R.drawable.milk),
                new Product("thuc pham", "kem", 2, R.drawable.cheese),
                new Product("thuc pham", "kem", 3, R.drawable.creamer),
                new Product("thit", "thit bo", 1, R.drawable.beef),
                new Product("thit", "thit", 2, R.drawable.pork),
                new Product("thit", "ga", 3, R.drawable.chicken),
                new Product("thit", "ca", 4, R.drawable.fish),

        };


        for (Product product : products) {
            ContentValues values = product.getContentValues();
            db.insert(ProductTableDataGateway.PRODUCT_TABLE_NAME, null, values);
        }
    }


    public void to_cart(View view) {
        Intent intent;
        intent = new Intent (this, CartActivity.class);
        startActivity(intent);
    }
}