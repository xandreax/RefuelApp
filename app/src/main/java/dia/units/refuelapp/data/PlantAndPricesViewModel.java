package dia.units.refuelapp.data;

import android.app.Application;
import android.graphics.Color;
import android.location.Location;
import android.util.Pair;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;
import java.util.Locale;

import dia.units.refuelapp.model.PlantWithPrices;
import kotlinx.coroutines.CoroutineScope;

public class PlantAndPricesViewModel extends AndroidViewModel {
    private RepositoryPlantsAndPrices repositoryPlantsAndPrices;
    private MutableLiveData<Location> position;
    private MutableLiveData<String> fuelType;
    public LiveData<PagingData<PlantWithPrices>> allPlantsWithPrices;

    public PlantAndPricesViewModel(Application application) {
        super(application);
        repositoryPlantsAndPrices = new RepositoryPlantsAndPrices(application);
        position = new MutableLiveData<>();
        fuelType = new MutableLiveData<>();
        MediatorPositionFuelType mediator = new MediatorPositionFuelType(position, fuelType);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        allPlantsWithPrices = Transformations.switchMap(mediator, input -> PagingLiveData.cachedIn(repositoryPlantsAndPrices.getAllPlantsWithPricesByLocation(input.first, input.second), viewModelScope));
    }

    public void setPosition(Location location) {
        position.postValue(location);
    }

    public LiveData<PagingData<PlantWithPrices>> getAllPlantsWithPrices() {
        return allPlantsWithPrices;
    }

    public LiveData<List<PlantWithPrices>> getFavorites() {
        return repositoryPlantsAndPrices.getFavorites(position.getValue(), fuelType.getValue());
    }

    public LiveData<List<String>> getFuelTypes() {
        return repositoryPlantsAndPrices.getFuelTypes();
    }

    public String getFuelType() {
        return fuelType.getValue();
    }

    public void setFuelType(String fuelType) {
        this.fuelType.postValue(fuelType);
    }

    public Location getPosition() {
        return position.getValue();
    }

    public void isDbEmpty(RepositoryCallback<Boolean> emptyCallback) {
        repositoryPlantsAndPrices.isDbEmpty(emptyCallback);
    }

    private class MediatorPositionFuelType extends MediatorLiveData<Pair<Location, String>> {
        public MediatorPositionFuelType(MutableLiveData<Location> location, MutableLiveData<String> fuelType) {
            addSource(location, location1 -> setValue(Pair.create(location1, fuelType.getValue())));
            addSource(fuelType, fuelType1 -> setValue(Pair.create(location.getValue(), fuelType1)));
        }
    }

    public void checkIfPlantIsFavorite(int idPlant, CheckBox isFavoriteCheckBox) {
        repositoryPlantsAndPrices.checkIfPlantIsFavorite(idPlant, new RepositoryCallback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                isFavoriteCheckBox.setChecked(result);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void insertFavorite(int idPlant) {
        repositoryPlantsAndPrices.insertFavorite(idPlant);
    }

    public void removeFavorite(int idPlant) {
        repositoryPlantsAndPrices.removeFavorite(idPlant);
    }

    public void getAvgPriceFuel(String fuel, int self, TextView textView) {
        repositoryPlantsAndPrices.getAvgPriceFuel(fuel, self, new RepositoryCallback<Double>() {
            @Override
            public void onComplete(Double result) {
                String strDouble = String.format("%.3f", result);
                textView.setText(strDouble);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getAvgPriceFuels(List<String> fuels, int self, BarChart chart) {
        repositoryPlantsAndPrices.getAvgPriceFuels(fuels, self, new RepositoryCallback<List<BarEntry>>() {
            @Override
            public void onComplete(List<BarEntry> entryList) {
                BarDataSet barDataSet = new BarDataSet(entryList, "prezzo medio");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextSize(16f);
                barDataSet.setValueTextColor(Color.rgb(0,121,107));
                chart.getDescription().setEnabled(false);
                chart.getAxisRight().setEnabled(false);
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.getXAxis().setDrawGridLines(false);
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(fuels){
                    @Override
                    public String getFormattedValue(float value) {
                        int index = Math.round(value);
                        if (index <= 0 || index >= fuels.size()+1 || index != (int)value)
                            return "";
                        return fuels.get(index-1);
                    }
                });
                chart.getLegend().setEnabled(false);
                BarData data = new BarData(barDataSet);
                chart.setData(data);
                chart.animateXY(2000, 2000);
                chart.invalidate();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<List<String>> getPlantsNames(){
        return repositoryPlantsAndPrices.getPlantsNames();
    }
}
