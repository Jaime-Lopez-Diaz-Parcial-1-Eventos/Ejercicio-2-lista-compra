package com.example.ejercicio_2_lista_compra.dataBase.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ejercicio_2_lista_compra.domain.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void addProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();
}
