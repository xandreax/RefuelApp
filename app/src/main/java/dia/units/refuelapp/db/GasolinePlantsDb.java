package dia.units.refuelapp.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import dia.units.refuelapp.db.dao.GasolinePlantDAO;
import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.entities.GasolinePrices;

@Database(entities = {GasolinePlant.class, GasolinePrices.class}, version = 1)
public abstract class GasolinePlantsDb extends RoomDatabase {

    private static GasolinePlantsDb instance;
    public abstract GasolinePlantDAO gasolinePlantDAO();
    public static synchronized GasolinePlantsDb getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GasolinePlantsDb.class, "gasoline_plants_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private GasolinePlantDAO gasolinePlantDAO;

        public PopulateDbAsyncTask(GasolinePlantsDb db) {
            gasolinePlantDAO = db.gasolinePlantDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ///insert the plants and the prices here
            return null;
        }
    }
}
