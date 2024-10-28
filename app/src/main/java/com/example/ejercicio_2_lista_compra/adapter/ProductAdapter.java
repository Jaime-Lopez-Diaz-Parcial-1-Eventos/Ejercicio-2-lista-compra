package com.example.ejercicio_2_lista_compra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ejercicio_2_lista_compra.R;
import com.example.ejercicio_2_lista_compra.domain.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private final ProductActionListener listener;

    public interface ProductActionListener {
        void onDeleteProduct(Product product);
    }

    public ProductAdapter(@NonNull Context context, @NonNull List<Product> products, ProductActionListener listener) {
        super(context, 0, products);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item_layout, parent, false);
        }

        Product product = getItem(position);
        if (product != null) {
            TextView tvName = convertView.findViewById(R.id.tv_product_name);
            TextView tvQuantity = convertView.findViewById(R.id.tv_quantity);
            TextView tvPrice = convertView.findViewById(R.id.tv_price);
            Button btnDelete = convertView.findViewById(R.id.btn_delete_product);

            tvName.setText(product.getNombre());
            tvQuantity.setText(getContext().getString(R.string.quantity_text, product.getCantidad()));
            tvPrice.setText(getContext().getString(R.string.price_text, product.getPrecio() == 0 ? "N/A" : String.format("%.2f €", product.getPrecio())));

            btnDelete.setOnClickListener(v -> listener.onDeleteProduct(product));
        }

        return convertView;
    }
}
