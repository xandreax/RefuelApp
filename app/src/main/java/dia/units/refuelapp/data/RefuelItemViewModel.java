package dia.units.refuelapp.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dia.units.refuelapp.model.RefuelItem;
import dia.units.refuelapp.model.RefuelItemWithPlantInfo;

public class RefuelItemViewModel extends AndroidViewModel {
    private RepositoryRefuelItems repositoryRefuelItems;
    public LiveData<List<RefuelItemWithPlantInfo>> refuelItems;

    public RefuelItemViewModel(@NonNull Application application) {
        super(application);
        repositoryRefuelItems = new RepositoryRefuelItems(application);
        refuelItems = repositoryRefuelItems.getAllRefuelItems();
    }

    public LiveData<List<RefuelItemWithPlantInfo>> getRefuelItems() {
        return refuelItems;
    }

    public void insertRefuelItem(RefuelItem item){
        repositoryRefuelItems.insertRefuelItem(item);
    }

    public void deleteRefuelItem(RefuelItemWithPlantInfo item){
        repositoryRefuelItems.deleteRefuelItem(item);
    }
}
