package dia.units.refuelapp.db;

import androidx.paging.DataSource;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;
import dia.units.refuelapp.model.PlantWithPrices;

@Dao
public interface GasolinePlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GasolinePlant> plants);

    @Transaction
    @Query("SELECT * FROM " + GasolinePlant.TABLE_NAME + " WHERE idPlant IN (SELECT DISTINCT(idPlant) FROM " + GasolinePrice.TABLE_NAME + ") ORDER BY ABS(latitude - :latitude) + ABS(longitude - :longitude)")
    PagingSource<Integer, PlantWithPrices> getAllPlantsWithPricesByDistance(double latitude, double longitude);

    @Transaction
    @Query("SELECT * FROM "+ GasolinePlant.TABLE_NAME + " WHERE idPlant IN (SELECT DISTINCT(idPlant) FROM " + GasolinePrice.TABLE_NAME + ")")
    PagingSource<Integer,PlantWithPrices> getAllPlantsWithPrices();
}