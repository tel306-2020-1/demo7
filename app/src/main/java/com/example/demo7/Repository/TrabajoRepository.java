package com.example.demo7.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo7.Dao.TrabajoDao;
import com.example.demo7.Database.HrDatabase;
import com.example.demo7.Dto.Trabajo;

import java.util.List;

public class TrabajoRepository extends ViewModel {

    private TrabajoDao trabajoDao;

    private MutableLiveData<List<Trabajo>> listaTrabajos = new MutableLiveData<>();

    public MutableLiveData<List<Trabajo>> getListaTrabajos() {
        return listaTrabajos;
    }

    public void setListaTrabajos(MutableLiveData<List<Trabajo>> listaTrabajos) {
        this.listaTrabajos = listaTrabajos;
    }

    public TrabajoRepository(Application application) {
        HrDatabase hrDatabase = HrDatabase.getDatabase(application);
        trabajoDao = hrDatabase.trabajoDao();
    }

    public void guardarTrabajo(final Trabajo trabajo) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                    trabajoDao.insert(trabajo);
            }
        });
        t.start();
    }

    public void listarTrabajos(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Trabajo> trabajos = trabajoDao.listaTrabajos();
                listaTrabajos.postValue(trabajos);
            }
        });
        t.start();
    }
}
