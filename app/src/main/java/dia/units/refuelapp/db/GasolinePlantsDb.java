package dia.units.refuelapp.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dia.units.refuelapp.db.dao.GasolinePlantDao;
import dia.units.refuelapp.db.dao.GasolinePricesDao;
import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.entities.GasolinePrice;

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
                    //.addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    /*
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private GasolinePlantDao gasolinePlantDao;
        private GasolinePricesDao gasolinePricesDao;

        public PopulateDbAsyncTask(GasolinePlantsDb db) {
            gasolinePlantDao = db.gasolinePlantDao();
            gasolinePricesDao = db.gasolinePricesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            gasolinePlantDao.insert(new GasolinePlant(9737, "RIVA STEFANO", "Esso", "Altro","Stazione di servizio ESSO di Riva Stefano", "PIAZZALE VALMAURA 4 34148","TRIESTE","TS",45.625284722136534, 13.795276130424782));
            gasolinePlantDao.insert(new GasolinePlant(47617,"ZOCCO GIOVANNI","Api-Ip","Altro","STAZIONE DI SERVIZIO IP","PASSEGGIO SANT'ANDREA 10 34123","TRIESTE","TS",45.639361582637804,13.760304350205974));
            gasolinePlantDao.insert(new GasolinePlant(38629,"INTERAUTO DI PRUTEANU DANIELA","Q8","Altro","INTERAUTO","strada della rosandra 62 34147","TRIESTE","TS",45.61287773636554, 13.825717940926552));
            gasolinePlantDao.insert(new GasolinePlant(38618, "ZOL FRANCO - CARBURANTI", "Agip Eni", "Strada Statale", "eni4044", "Statale 14 della Venezia Giulia, Km. 1, dir. tieste 34121","TRIESTE","TS",45.66479868882435 ,13.767396658658981));

            gasolinePricesDao.insert(new GasolinePrice(9737, "Benzina", 1.759,0,"26/01/2022 07:22:15"));
            gasolinePricesDao.insert(new GasolinePrice(9737,"Benzina",1.759,1,"26/01/2022 07:22:15"));
            gasolinePricesDao.insert(new GasolinePrice(9737,"Gasolio",1.679,1,"26/01/2022 07:22:15"));
            gasolinePricesDao.insert(new GasolinePrice(9737,"Gasolio",1.679,0,"26/01/2022 07:22:15"));
            gasolinePricesDao.insert(new GasolinePrice(47617,"Benzina",1.779,0,"27/01/2022 09:17:22"));
            gasolinePricesDao.insert(new GasolinePrice(47617,"Gasolio",1.679,0,"27/01/2022 09:17:22"));
            gasolinePricesDao.insert(new GasolinePrice(38629, "Benzina", 1.789,1,"28/01/2022 07:01:57"));
            gasolinePricesDao.insert(new GasolinePrice(38629, "Benzina", 1.939,0,"28/01/2022 07:01:57"));
            gasolinePricesDao.insert(new GasolinePrice(38629, "Gasolio", 1.849,0,"28/01/2022 07:01:57"));
            gasolinePricesDao.insert(new GasolinePrice(38629, "Gasolio", 1.699,1,"28/01/2022 07:01:57"));
            gasolinePricesDao.insert(new GasolinePrice(38618, "Benzina", 1.999, 0,"22/01/2022 09:46:49"));
            gasolinePricesDao.insert(new GasolinePrice(38618, "Benzina", 1.779, 1,"22/01/2022 09:46:49"));
            gasolinePricesDao.insert(new GasolinePrice(38618, "Blue Diesel",1.999, 0,"22/01/2022 09:46:49"));
            gasolinePricesDao.insert(new GasolinePrice(38618, "Blue Diesel", 1.779, 1,"22/01/2022 09:46:49"));
            ///insert the plants and the prices here
            return null;
        }
    }

     */
}
