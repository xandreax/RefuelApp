package dia.units.refuelapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.entities.GasolinePrice;
import dia.units.refuelapp.db.entities.PlantWithPrices;

@Dao
public interface GasolinePlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (GasolinePlant plant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll (List<GasolinePlant> plants);

    @Update
    void update (GasolinePlant plant);

    @Update
    void updateAll (List<GasolinePlant> plants);

    @Query("SELECT COUNT(*) FROM " + GasolinePlant.TABLE_NAME)
    int count();

    @Transaction
    @Query("SELECT * FROM "+ GasolinePlant.TABLE_NAME + " WHERE idPlant IN (SELECT DISTINCT(idPlant) FROM " + GasolinePrice.TABLE_NAME + ")")
    LiveData<List<PlantWithPrices>> getAllPlantsWithPrices();
}
