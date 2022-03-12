package dia.units.refuelapp.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import dia.units.refuelapp.db.GasolinePlantsDb;
import dia.units.refuelapp.db.RefuelItemDao;
import dia.units.refuelapp.model.RefuelItem;
import dia.units.refuelapp.model.RefuelItemWithPlantInfo;

public class RepositoryRefuelItems {

    private RefuelItemDao refuelItemDao;

    public RepositoryRefuelItems(Context context) {
        GasolinePlantsDb db = GasolinePlantsDb.getInstance(context);
        this.refuelItemDao = db.refuelItemDao();
    }

    public LiveData<List<RefuelItemWithPlantInfo>> getAllRefuelItems(){
        return refuelItemDao.getAllRefuelItems();
    }

    public void insertRefuelItem(RefuelItem item){
        refuelItemDao.insertRefuelItem(item);
    }

    public void deleteRefuelItem(RefuelItemWithPlantInfo item){
        refuelItemDao.deleteRefuelItem(item.getId());
    }
}
