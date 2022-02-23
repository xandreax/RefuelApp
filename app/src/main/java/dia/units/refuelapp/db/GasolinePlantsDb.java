package dia.units.refuelapp.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;

@Database(entities = {GasolinePlant.class, GasolinePrice.class}, version = 1)
public abstract class GasolinePlantsDb extends RoomDatabase {

    private static GasolinePlantsDb instance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract GasolinePlantDao gasolinePlantDao();
    public abstract GasolinePricesDao gasolinePricesDao();

    public static synchronized GasolinePlantsDb getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GasolinePlantsDb.class, "gasoline_plants_database")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            databaseWriteExecutor.execute(new DownloadAndSaveCsvThread(context));
                        }
                    })
                    .build();
        }
        return instance;
    }
}
