package dia.units.refuelapp.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import dia.units.refuelapp.db.GasolinePlantsDb;
import dia.units.refuelapp.db.dao.GasolinePlantDAO;
import dia.units.refuelapp.db.entities.GasolinePlant;

public class GasolinePlantRepository {
    private GasolinePlantDAO gasolinePlantDAO;
    private LiveData<List<GasolinePlant>> allGasolinePlants;

    public GasolinePlantRepository(Application application) {
        GasolinePlantsDb db = GasolinePlantsDb.getInstance(application);
        gasolinePlantDAO = db.gasolinePlantDAO();
        allGasolinePlants = gasolinePlantDAO.getAllPlants();
    }

    public LiveData<List<GasolinePlant>> getAllGasolinePlants(){
        return allGasolinePlants;
    }

    public void insert (GasolinePlant gasolinePlant){
        new InsertGasolinePlantAsyncTask(gasolinePlantDAO).execute(gasolinePlant);
    }

    private static class InsertGasolinePlantAsyncTask extends AsyncTask<GasolinePlant, Void, Void>{
        private GasolinePlantDAO gasolinePlantDAO;

        private InsertGasolinePlantAsyncTask(GasolinePlantDAO gasolinePlantDAO){
            this.gasolinePlantDAO = gasolinePlantDAO;
        }

        @Override
        protected Void doInBackground(GasolinePlant... gasolinePlants) {
            gasolinePlantDAO.insert(gasolinePlants[0]);
            return null;
        }
    }
}
