package dia.units.refuelapp.db;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import dia.units.refuelapp.model.FavoritePlant;
import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;
import dia.units.refuelapp.model.PlantWithPrices;

@Dao
public interface GasolinePlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Iterable<GasolinePlant> plants);

    @Transaction
    @Query("SELECT * FROM " + GasolinePlant.TABLE_NAME + " WHERE idPlant IN " +
            "(SELECT DISTINCT(idPlant) FROM " + GasolinePrice.TABLE_NAME + " WHERE fuelType = :fuelType) " +
            "ORDER BY ABS(latitude - :latitude) + ABS(longitude - :longitude)")
    PagingSource<Integer, PlantWithPrices> getAllPlantsWithPricesByDistanceByFuel(double latitude, double longitude, String fuelType);

    @Transaction
    @Query("SELECT * FROM " + GasolinePlant.TABLE_NAME + " INNER JOIN " + FavoritePlant.TABLE_NAME + " ON "
            + FavoritePlant.TABLE_NAME + ".idPlant = " + GasolinePlant.TABLE_NAME + ".idPlant WHERE " + GasolinePlant.TABLE_NAME + ".idPlant IN " +
            "(SELECT DISTINCT(" + GasolinePrice.TABLE_NAME + ".idPlant) FROM " + GasolinePrice.TABLE_NAME + " WHERE fuelType = :fuelType) " +
            "ORDER BY ABS(latitude - :latitude) + ABS(longitude - :longitude)")
    LiveData<List<PlantWithPrices>> getFavoritePlants(String fuelType, double latitude, double longitude);

    @Query("SELECT COUNT(idPlant) FROM " + GasolinePlant.TABLE_NAME + " ORDER BY idPlant LIMIT 1")
    ListenableFuture<Integer> isDbEmpty();

    @Query("SELECT EXISTS(SELECT * FROM " + FavoritePlant.TABLE_NAME + " WHERE idPlant = :idPlant)")
    ListenableFuture<Boolean> isPlantFavorite(int idPlant);

    @Insert(entity = FavoritePlant.class)
    ListenableFuture<Void> insertFavorite(FavoritePlant favoritePlant);

    @Query("DELETE FROM " + FavoritePlant.TABLE_NAME + " WHERE idPlant = :idPlant")
    ListenableFuture<Void> removeFavorite(int idPlant);

    @Query("SELECT name FROM "+ GasolinePlant.TABLE_NAME)
    LiveData<List<String>> getPlantsNames();
}