package dia.units.refuelapp.data;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import java.util.List;

import dia.units.refuelapp.db.GasolinePlantDao;
import dia.units.refuelapp.db.GasolinePlantsDb;
import dia.units.refuelapp.db.GasolinePricesDao;
import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;
import dia.units.refuelapp.model.PlantWithPrices;

public class Repository {
    private final GasolinePlantDao gasolinePlantDao;
    private final GasolinePricesDao gasolinePricesDao;
    private static final int DATABASE_PAGE_SIZE = 15;
    private static final int PREFETCH_SIZE = 15;
    private static final int INITIAL_SIZE = 20;

    public Repository(Context context) {
        GasolinePlantsDb db = GasolinePlantsDb.getInstance(context);
        gasolinePlantDao = db.gasolinePlantDao();
        gasolinePricesDao = db.gasolinePricesDao();
    }

    public LiveData<PagingData<PlantWithPrices>> getAllPlantsWithPricesByLocation(Location location) {
        Pager<Integer, PlantWithPrices> pager = new Pager<>(
                new PagingConfig(
                        DATABASE_PAGE_SIZE,
                        PREFETCH_SIZE,
                        false,
                        INITIAL_SIZE), () -> gasolinePlantDao.getAllPlantsWithPricesByDistance(location.getLatitude(), location.getLongitude())
        );
        return PagingLiveData.getLiveData(pager);
    }

    public void insertAllPlants(List<GasolinePlant> plants) {
        GasolinePlantsDb.databaseWriteExecutor.execute(() -> {
            gasolinePlantDao.insertAll(plants);
        });
    }

    public void insertAllPrices(List<GasolinePrice> prices) {
        GasolinePlantsDb.databaseWriteExecutor.execute(() -> {
            gasolinePricesDao.insertAll(prices);
        });
    }
}
