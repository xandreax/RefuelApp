package dia.units.refuelapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;

@Dao
public interface GasolinePricesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Iterable<GasolinePrice> gasolinePrices);

    @Query("SELECT COUNT(idPlant) FROM " + GasolinePrice.TABLE_NAME + " ORDER BY idPlant LIMIT 1")
    ListenableFuture<Integer> isDbEmpty();

    @Query("SELECT DISTINCT fuelType FROM "+GasolinePrice.TABLE_NAME)
    LiveData<List<String>> getFuelTypes();

    @Query("SELECT AVG(price) FROM "+GasolinePrice.TABLE_NAME+" WHERE fuelType = :fuel AND isSelf = :self")
    ListenableFuture<Double> getAvgPriceFuel(String fuel, int self);
}
