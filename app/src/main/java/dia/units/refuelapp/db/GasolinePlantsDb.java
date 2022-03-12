package dia.units.refuelapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dia.units.refuelapp.model.FavoritePlant;
import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;
import dia.units.refuelapp.model.RefuelItem;

@Database(entities = {GasolinePlant.class, GasolinePrice.class, FavoritePlant.class, RefuelItem.class}, version = 5)
public abstract class GasolinePlantsDb extends RoomDatabase {

    private static GasolinePlantsDb instance;

    public abstract GasolinePlantDao gasolinePlantDao();
    public abstract GasolinePricesDao gasolinePricesDao();
    public abstract RefuelItemDao refuelItemDao();

    public static synchronized GasolinePlantsDb getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GasolinePlantsDb.class, "gasoline_plants_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
