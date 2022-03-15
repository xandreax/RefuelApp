package dia.units.refuelapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.RefuelItem;
import dia.units.refuelapp.model.RefuelItemWithPlantInfo;

@Dao
public interface RefuelItemDao {

    @Insert
    ListenableFuture<Void> insertRefuelItem(RefuelItem item);

    @Query("DELETE FROM " + RefuelItem.TABLE_NAME + " WHERE id = :id")
    ListenableFuture<Void> deleteRefuelItem(int id);

    @Query("SELECT name, flagCompany, id, money, liters, price_by_liter, kilometers, updateDate FROM " + GasolinePlant.TABLE_NAME + " INNER JOIN " + RefuelItem.TABLE_NAME + " ON "
            + RefuelItem.TABLE_NAME + ".namePlant = " + GasolinePlant.TABLE_NAME + ".name")
    LiveData<List<RefuelItemWithPlantInfo>> getAllRefuelItems();
}
