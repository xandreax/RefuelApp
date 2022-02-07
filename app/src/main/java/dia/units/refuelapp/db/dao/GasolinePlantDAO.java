package dia.units.refuelapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dia.units.refuelapp.db.entities.GasolinePlant;

@Dao
public interface GasolinePlantDAO {

    @Insert
    void insert (GasolinePlant plant);

    @Update
    void update (GasolinePlant plant);

    @Delete
    void delete(GasolinePlant plant);

    @Query("SELECT * FROM plants_table")
    LiveData<List<GasolinePlant>> getAllPlants();
}
