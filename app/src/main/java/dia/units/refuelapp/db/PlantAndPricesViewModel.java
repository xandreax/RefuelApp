package dia.units.refuelapp.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.entities.GasolinePrice;
import dia.units.refuelapp.db.entities.PlantWithPrices;
import dia.units.refuelapp.db.repository.GasolinePlantRepository;
import dia.units.refuelapp.db.repository.GasolinePricesRepository;

public class PlantAndPricesViewModel extends AndroidViewModel {
    private GasolinePlantRepository gasolinePlantRepository;
    private GasolinePricesRepository gasolinePricesRepository;
    private LiveData<List<PlantWithPrices>> allPlantsWithPrices;

    public PlantAndPricesViewModel(Application application) {
        super(application);
        gasolinePlantRepository = new GasolinePlantRepository(application);
        gasolinePricesRepository = new GasolinePricesRepository(application);
        allPlantsWithPrices = gasolinePlantRepository.getAllPlantsWithPrices();
    }

    public void insertGasolinePlant (GasolinePlant gasolinePlant){
        gasolinePlantRepository.insert(gasolinePlant);
    }

    public void insertGasolinePrice(GasolinePrice gasolinePrice){
        gasolinePricesRepository.insert(gasolinePrice);
    }

    public LiveData<List<PlantWithPrices>> getAllPlantsWithPrices(){
        return allPlantsWithPrices;
    }
}
