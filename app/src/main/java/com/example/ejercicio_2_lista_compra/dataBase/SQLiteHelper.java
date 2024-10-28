package com.example.ejercicio_2_lista_compra.dataBase;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.ejercicio_2_lista_compra.dataBase.dao.ProductDao;
import com.example.ejercicio_2_lista_compra.domain.Product;

@Database(entities = {Product.class}, version = 1)
public abstract class SQLiteHelper extends RoomDatabase {

    private static SQLiteHelper instance;

    public abstract ProductDao productDao();

    public static synchronized SQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            SQLiteHelper.class, "productsDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
