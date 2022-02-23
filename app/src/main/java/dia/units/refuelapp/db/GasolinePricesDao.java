package dia.units.refuelapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dia.units.refuelapp.model.GasolinePrice;

@Dao
public interface GasolinePricesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (GasolinePrice gasolinePrice);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GasolinePrice> gasolinePrices);

    @Update
    void update (GasolinePrice gasolinePrice);

    @Update
    void updateAll(List<GasolinePrice> prices);

    @Query("SELECT COUNT(*) FROM " + GasolinePrice.TABLE_NAME)
    int count();
}
