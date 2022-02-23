package dia.units.refuelapp.ui;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagedList;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import dia.units.refuelapp.model.PlantWithPrices;
import dia.units.refuelapp.data.Repository;
import kotlinx.coroutines.CoroutineScope;

public class PlantAndPricesViewModel extends AndroidViewModel {
    private Repository repository;
    private MutableLiveData<Location> position;
    public LiveData<PagingData<PlantWithPrices>> allPlantsWithPrices;

    public PlantAndPricesViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        position = new MutableLiveData<>();
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        allPlantsWithPrices = Transformations.switchMap(position, input -> PagingLiveData.cachedIn(repository.getAllPlantsWithPricesByLocation(input), viewModelScope));
    }

    public void setPosition (Location location){
        position.postValue(location);
    }

    public LiveData<PagingData<PlantWithPrices>> getAllPlantsWithPrices(){
        return allPlantsWithPrices;
    }
}
