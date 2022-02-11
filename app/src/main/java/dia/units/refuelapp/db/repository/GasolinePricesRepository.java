package dia.units.refuelapp.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import dia.units.refuelapp.db.GasolinePlantsDb;
import dia.units.refuelapp.db.dao.GasolinePricesDao;
import dia.units.refuelapp.db.entities.GasolinePrice;

public class GasolinePricesRepository {
    private GasolinePricesDao gasolinePricesDao;

    public GasolinePricesRepository(Application application) {
        GasolinePlantsDb db = GasolinePlantsDb.getInstance(application);
        gasolinePricesDao = db.gasolinePricesDao();
    }

    public void insert (GasolinePrice gasolinePrice){
        GasolinePlantsDb.databaseWriteExecutor.execute(()->{
            gasolinePricesDao.insert(gasolinePrice);
        });
    }

    public void insertAll (List<GasolinePrice> prices){
        GasolinePlantsDb.databaseWriteExecutor.execute(()->{
            gasolinePricesDao.insertAll(prices);
        });
    }

    public void count(){
        GasolinePlantsDb.databaseWriteExecutor.execute(()->{
            gasolinePricesDao.count();
        });
    }
}
