package dia.units.refuelapp.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.repository.GasolinePlantRepository;

public class PlantViewModel extends AndroidViewModel {
    private GasolinePlantRepository repository;
    private LiveData<List<GasolinePlant>> allPlants;

    public PlantViewModel(@NonNull Application application) {
        super(application);
        repository = new GasolinePlantRepository(application);
        allPlants = repository.getAllGasolinePlants();
    }

    public void insert (GasolinePlant plant){
        repository.insert(plant);
    }

    public LiveData<List<GasolinePlant>> getAllPlants(){
        return allPlants;
    }
}
