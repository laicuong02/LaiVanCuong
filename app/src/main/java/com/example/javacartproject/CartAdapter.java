package com.example.javacartproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends ArrayAdapter<Product> {
    protected Context context;
    protected ArrayList<Product> prod;
    protected int resource;
    protected TextView totalLabel;

    public CartAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects, TextView totalLabel) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        prod = CartStaticArray.cartProducts;
        prod = (ArrayList<Product>)objects;
        this.totalLabel = totalLabel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product tmpProd = this.prod.get(position);
        int quantity = CartStaticArray.quantities.get(tmpProd);

        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        convertView = layoutInflater.inflate(this.resource,parent,false);

        TextView prod_name = (TextView)convertView.findViewById(R.id.prod_name);
        TextView prod_price = (TextView)convertView.findViewById(R.id.prod_price);
        ImageView prod_img = (ImageView)convertView.findViewById(R.id.prod_img);
        Button del_btn = (Button)convertView.findViewById(R.id.del);
        Button inc_btn = (Button)convertView.findViewById(R.id.increase_btn);
        Button dec_btn = (Button)convertView.findViewById(R.id.decrease_btn);
        TextView quantity_text = (TextView)convertView.findViewById(R.id.quantity_text);


        prod_name.setText(tmpProd.getName());
        prod_img.setImageResource(tmpProd.getImage());
        quantity_text.setText(String.valueOf(quantity));

        double totalPrice = tmpProd.getPrice() * quantity;
        String formattedPrice = String.format("%.2f", totalPrice); // round the price to two decimal places
        prod_price.setText(quantity + " x " + tmpProd.getPrice() + " = " + formattedPrice);

        double total = 0;
        for (Product p : prod) {
            total += p.getPrice() * CartStaticArray.quantities.get(p);
        }
        totalLabel.setText("Total: " + String.format("%.2f", total) + "$"); // set the text of the total price label


        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double price = tmpProd.getPrice() * CartStaticArray.quantities.get(tmpProd);
                CartStaticArray.delete(tmpProd);
                notifyDataSetChanged();
                double total = 0;
                for (Product p : prod) {
                    total += p.getPrice() * CartStaticArray.quantities.get(p);
                }
                totalLabel.setText("Total: " + String.format("%.2f", total)+"$");
            }
        });


        inc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = quantity + 1;
                CartStaticArray.quantities.put(tmpProd, newQuantity);
                quantity_text.setText(String.valueOf(newQuantity));
                notifyDataSetChanged();
            }
        });

        dec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    int newQuantity = quantity - 1;
                    CartStaticArray.quantities.put(tmpProd, newQuantity);
                    quantity_text.setText(String.valueOf(newQuantity));
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Cannot decrease quantity further", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
