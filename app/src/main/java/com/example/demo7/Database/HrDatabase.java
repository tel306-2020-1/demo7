package com.example.demo7.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.demo7.Dao.TrabajoDao;
import com.example.demo7.Dto.Trabajo;

@Database(entities = {Trabajo.class},version = 1)
public abstract class HrDatabase extends RoomDatabase {

    public abstract TrabajoDao trabajoDao();

    private static HrDatabase INSTANCIA;

    public static HrDatabase getDatabase(final Context context){
        if(INSTANCIA == null){
            synchronized (HrDatabase.class){
                INSTANCIA = Room.databaseBuilder(context.getApplicationContext(),
                        HrDatabase.class, "hr").build();
            }
        }
        return INSTANCIA;
    }
}
