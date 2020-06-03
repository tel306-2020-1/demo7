package com.example.demo7.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.demo7.Dto.Trabajo;

import java.util.List;

@Dao
public interface TrabajoDao {

    @Insert
    public void insert(Trabajo trabajo);

    @Query("select * from job")
    public List<Trabajo> listaTrabajos();
}
