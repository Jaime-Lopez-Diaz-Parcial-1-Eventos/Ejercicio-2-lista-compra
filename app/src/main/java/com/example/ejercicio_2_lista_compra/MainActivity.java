package com.example.ejercicio_2_lista_compra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ejercicio_2_lista_compra.activity.SettingsActivity;
import com.example.ejercicio_2_lista_compra.adapter.ProductAdapter;
import com.example.ejercicio_2_lista_compra.dataBase.SQLiteHelper;
import com.example.ejercicio_2_lista_compra.dataBase.dao.ProductDao;
import com.example.ejercicio_2_lista_compra.domain.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private SQLiteHelper sqliteHelper;
    private ProductDao productDao;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private ExecutorService executorService;

    private FirebaseFirestore firebaseDB;

    private EditText etProductName, etQuantity, etPrice;
    private TextView tvTotalPrice, tvProductCount;
    private ListView productListView;
    private Button btnAddProduct;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executorService = Executors.newFixedThreadPool(4);

        // Initialize SQLite and Firebase Firestore
        sqliteHelper = SQLiteHelper.getInstance(this);
        productDao = sqliteHelper.productDao();
        firebaseDB = FirebaseFirestore.getInstance();

        etProductName = findViewById(R.id.et_product_name);
        etQuantity = findViewById(R.id.et_quantity);
        etPrice = findViewById(R.id.et_price);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvProductCount = findViewById(R.id.tv_product_count);
        productListView = findViewById(R.id.product_list_view);
        btnAddProduct = findViewById(R.id.btn_add_product);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_main);
        navView = findViewById(R.id.nav_view);

        // Configure toolbar and drawer toggle
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Settings button listener for redirection
        navView.findViewById(R.id.nav_settings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList, this::deleteProduct);
        productListView.setAdapter(productAdapter);

        btnAddProduct.setOnClickListener(v -> addNewProduct());

        loadProducts();
    }

    private void addNewProduct() {
        String name = etProductName.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "El nombre del producto es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = quantityStr.isEmpty() ? 1 : Integer.parseInt(quantityStr);
        double price = priceStr.isEmpty() ? 0 : Double.parseDouble(priceStr);

        Product product = new Product(UUID.randomUUID().toString(), name, quantity, price);

        firebaseDB.collection("products").document(product.getId()).set(product)
                .addOnSuccessListener(aVoid -> executorService.execute(() -> {
                    productDao.addProduct(product);
                    runOnUiThread(() -> {
                        etProductName.setText("");
                        etQuantity.setText("");
                        etPrice.setText("");
                        loadProducts();
                    });
                }))
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error al agregar producto en Firebase", Toast.LENGTH_SHORT).show());
    }

    private void loadProducts() {
        executorService.execute(() -> {
            List<Product> productsFromDB = productDao.getAllProducts();
            if (!productsFromDB.isEmpty()) {
                updateProductList(productsFromDB);
            } else {
                firebaseDB.collection("products")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                productList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Product product = document.toObject(Product.class);

                                    if (product.getId() == null || product.getId().isEmpty()) {
                                        product.setId(UUID.randomUUID().toString());
                                    }

                                    productList.add(product);
                                    executorService.execute(() -> productDao.addProduct(product));
                                }
                                updateProductList(productList);
                            } else {
                                Toast.makeText(MainActivity.this, "Error al cargar productos desde Firebase", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateProductList(List<Product> products) {
        double totalPrice = 0;
        int totalProducts = products.size();

        for (Product product : products) {
            if (product.getPrecio() > 0) {
                totalPrice += product.getPrecio() * product.getCantidad();
            }
        }

        double finalTotalPrice = totalPrice;
        runOnUiThread(() -> {
            productList.clear();
            productList.addAll(products);
            productAdapter.notifyDataSetChanged();
            tvProductCount.setText(String.valueOf(totalProducts));
            tvTotalPrice.setText(String.format("Total: %.2f €", finalTotalPrice));
        });
    }

    private void deleteProduct(Product product) {
        firebaseDB.collection("products").document(product.getId()).delete()
                .addOnSuccessListener(aVoid -> executorService.execute(() -> {
                    productDao.deleteProduct(product);
                    runOnUiThread(this::loadProducts);
                }))
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error al eliminar producto en Firebase", Toast.LENGTH_SHORT).show());
    }
}

