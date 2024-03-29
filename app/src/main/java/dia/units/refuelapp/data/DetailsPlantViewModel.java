package dia.units.refuelapp.data;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dia.units.refuelapp.model.PlantWithPrices;

public class DetailsPlantViewModel extends ViewModel {
    final private MutableLiveData<PlantWithPrices> plant;

    public DetailsPlantViewModel() {
        plant = new MutableLiveData<>();
    }

    public MutableLiveData<PlantWithPrices> getPlant() {
        return plant;
    }
}
