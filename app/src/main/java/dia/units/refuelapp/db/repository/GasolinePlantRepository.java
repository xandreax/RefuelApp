package dia.units.refuelapp.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import dia.units.refuelapp.db.GasolinePlantsDb;
import dia.units.refuelapp.db.dao.GasolinePlantDao;
import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.entities.PlantWithPrices;

public class GasolinePlantRepository {
    private GasolinePlantDao gasolinePlantDao;
    private LiveData<List<PlantWithPrices>> allPlantsWithPrices;

    public GasolinePlantRepository(Application application) {
        GasolinePlantsDb db = GasolinePlantsDb.getInstance(application);
        gasolinePlantDao = db.gasolinePlantDao();
        allPlantsWithPrices = gasolinePlantDao.getAllPlantsWithPrices();
    }

    public LiveData<List<PlantWithPrices>> getAllPlantsWithPrices() {
        return allPlantsWithPrices;
    }

    public void insert(GasolinePlant gasolinePlant) {
        GasolinePlantsDb.databaseWriteExecutor.execute(() -> {
            gasolinePlantDao.insert(gasolinePlant);
        });
    }

    public void insertAll(List<GasolinePlant> plants){
        GasolinePlantsDb.databaseWriteExecutor.execute(()->{
            gasolinePlantDao.insertAll(plants);
        });
    }

    public void count(){
        GasolinePlantsDb.databaseWriteExecutor.execute(() -> {
            gasolinePlantDao.count();
        });
    }
}
