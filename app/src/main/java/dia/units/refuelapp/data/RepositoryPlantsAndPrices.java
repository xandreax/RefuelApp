package dia.units.refuelapp.data;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.BarEntry;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import dia.units.refuelapp.db.GasolinePlantDao;
import dia.units.refuelapp.db.GasolinePlantsDb;
import dia.units.refuelapp.db.GasolinePricesDao;
import dia.units.refuelapp.model.FavoritePlant;
import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;
import dia.units.refuelapp.model.PlantWithPrices;
import dia.units.refuelapp.volley.InputStreamVolleyRequest;
import dia.units.refuelapp.volley.VolleyCallBack;

public class RepositoryPlantsAndPrices {
    private final GasolinePlantDao gasolinePlantDao;
    private final GasolinePricesDao gasolinePricesDao;
    private static final int DATABASE_PAGE_SIZE = 15;
    private static final int PREFETCH_SIZE = 15;
    private static final int INITIAL_SIZE = 20;
    private static final String URL_PREZZI = "https://www.mise.gov.it/images/exportCSV/prezzo_alle_8.csv";
    private static final String URL_IMPIANTI = "https://www.mise.gov.it/images/exportCSV/anagrafica_impianti_attivi.csv";
    private final RequestQueue requestQueue;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private final Handler mainThreadHandler;

    public RepositoryPlantsAndPrices(Context context) {
        requestQueue = Volley.newRequestQueue(context, new HurlStack());
        GasolinePlantsDb db = GasolinePlantsDb.getInstance(context);
        gasolinePlantDao = db.gasolinePlantDao();
        gasolinePricesDao = db.gasolinePricesDao();
        this.mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    }

    public LiveData<PagingData<PlantWithPrices>> getAllPlantsWithPricesByLocation(Location location, String fuelType) {
        Log.i("lte", "fuel type: " + fuelType);
        Pager<Integer, PlantWithPrices> pager = new Pager<>(
                new PagingConfig(
                        DATABASE_PAGE_SIZE,
                        PREFETCH_SIZE,
                        false,
                        INITIAL_SIZE), () -> gasolinePlantDao.getAllPlantsWithPricesByDistanceByFuel(
                location != null ? location.getLatitude() : 0,
                location != null ? location.getLongitude() : 0,
                fuelType != null ? fuelType : "Benzina")
        );
        return PagingLiveData.getLiveData(pager);
    }

    public void insertAllPlants(List<GasolinePlant> plants) {
        databaseWriteExecutor.submit(() -> {
            gasolinePlantDao.insertAll(plants);
        });
    }

    public void insertAllPrices(List<GasolinePrice> prices) {
        databaseWriteExecutor.submit(() -> gasolinePricesDao.insertAll(prices));
    }

    public void isDbEmpty(RepositoryCallback<Boolean> repositoryCallback) {
        ListenableFuture<Integer> plantEmpty = gasolinePlantDao.isDbEmpty();
        ListenableFuture<Integer> priceEmpty = gasolinePricesDao.isDbEmpty();
        ListenableFuture<List<Integer>> emptyTasks = Futures.allAsList(plantEmpty, priceEmpty);
        Futures.addCallback(emptyTasks, new FutureCallback<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> result) {
                if (result.isEmpty() || result.size() < 2 || result.get(0) == 0 || result.get(1) == 0) {
                    mainThreadHandler.post(() ->repositoryCallback.onComplete(true));
                } else
                    mainThreadHandler.post(() ->repositoryCallback.onComplete(false));
            }
            @Override
            public void onFailure(Throwable t) {
                mainThreadHandler.post(() ->repositoryCallback.onFailure(t));
            }
        }, databaseWriteExecutor);
    }

    public void checkIfPlantIsFavorite(int idPlant, RepositoryCallback<Boolean> repositoryCallback) {
        ListenableFuture<Boolean> future = gasolinePlantDao.isPlantFavorite(idPlant);
        Futures.addCallback(future, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                repositoryCallback.onComplete(result);
            }

            @Override
            public void onFailure(Throwable t) {
                repositoryCallback.onFailure(t);
            }
        }, databaseWriteExecutor);
    }

    public void insertFavorite(int idPlant) {
        FavoritePlant favoritePlant = new FavoritePlant(idPlant);
        gasolinePlantDao.insertFavorite(favoritePlant);
    }

    public void removeFavorite(int idPlant) {
        gasolinePlantDao.removeFavorite(idPlant);
    }

    public LiveData<List<String>> getFuelTypes() {
        return gasolinePricesDao.getFuelTypes();
    }

    public LiveData<List<PlantWithPrices>> getFavorites(Location location, String fuelType) {
        return gasolinePlantDao.getFavoritePlants(fuelType != null ? fuelType : "Benzina", location != null ? location.getLatitude() : 0,
                location != null ? location.getLongitude() : 0);
    }

    public void makeUpdateDataRequest() {
        databaseWriteExecutor.submit(()-> {requestPlants(new VolleyCallBack<GasolinePlant>() {
            @Override
            public void onSuccess(List<GasolinePlant> plants) {
                insertAllPlants(plants);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });});
        databaseWriteExecutor.submit(()->{requestPrices(new VolleyCallBack<GasolinePrice>() {
            @Override
            public void onSuccess(List<GasolinePrice> prices) {
                insertAllPrices(prices);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });});
    }

    private void requestPlants(VolleyCallBack<GasolinePlant> volleyCallBack) {
        InputStreamVolleyRequest requestPlants = new InputStreamVolleyRequest(Request.Method.GET, URL_IMPIANTI, null, response -> {
            Log.i("lte", "Start reading plant csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response)));
            databaseWriteExecutor.submit(() ->{List<GasolinePlant> plants = new CsvToBeanBuilder<GasolinePlant>(reader)
                    .withIgnoreQuotations(true)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withOrderedResults(false)
                    .withExceptionHandler(e -> {
                        if (e instanceof CsvDataTypeMismatchException)
                            return null;
                        else
                            throw new CsvException(e.getLocalizedMessage());
                    })
                    .withType(GasolinePlant.class)
                    .build()
                    .parse();
                Log.i("lte", "Finish reading plant csv, start to insert");
                volleyCallBack.onSuccess(plants);});
        }, volleyCallBack::onFailure);
        requestQueue.add(requestPlants);
    }

    private void requestPrices(VolleyCallBack<GasolinePrice> volleyCallBack) {
        InputStreamVolleyRequest requestPrices = new InputStreamVolleyRequest(Request.Method.GET, URL_PREZZI, null, response -> {
            Log.i("lte", "Start reading prices csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response)));
            databaseWriteExecutor.submit(() -> {
                List<GasolinePrice> prices = new CsvToBeanBuilder<GasolinePrice>(reader)
                        .withIgnoreQuotations(true)
                        .withSeparator(';')
                        .withSkipLines(1)
                        .withOrderedResults(false)
                        .withType(GasolinePrice.class)
                        .build()
                        .parse();
                Log.i("lte", "Finish reading prices csv, start to insert");
                volleyCallBack.onSuccess(prices);
            });
        }, volleyCallBack::onFailure);
        requestQueue.add(requestPrices);
    }

    public void getAvgPriceFuel(String fuel, int self, RepositoryCallback<Double> repositoryCallback) {
        ListenableFuture<Double> future = gasolinePricesDao.getAvgPriceFuel(fuel, self);
        Futures.addCallback(future, new FutureCallback<Double>() {
            @Override
            public void onSuccess(Double result) {
                mainThreadHandler.post(() -> repositoryCallback.onComplete(result));
            }

            @Override
            public void onFailure(Throwable t) {
                repositoryCallback.onFailure(t);
            }
        }, databaseWriteExecutor);
    }

    public void getAvgPriceFuels(List<String> fuels, int self, RepositoryCallback<List<BarEntry>> entriesRepositoryCallback) {
        List<ListenableFuture<Double>> taskList = new ArrayList<>();
        for (String fuel : fuels) {
            ListenableFuture<Double> task = gasolinePricesDao.getAvgPriceFuel(fuel, self);
            taskList.add(task);
        }
        ListenableFuture<List<BarEntry>> entriesTask = Futures.whenAllComplete(taskList).call(() -> {
            List<BarEntry> entries = new ArrayList<>();
            int cont = 1;
            for (ListenableFuture<Double> task : taskList) {
                entries.add(new BarEntry(cont, (float) (double) Futures.getDone(task)));
                cont++;
            }
            return entries;
        }, databaseWriteExecutor);
        Futures.addCallback(entriesTask, new FutureCallback<List<BarEntry>>() {
            @Override
            public void onSuccess(List<BarEntry> result) {
                mainThreadHandler.post(() -> entriesRepositoryCallback.onComplete(result));
            }
            @Override
            public void onFailure(Throwable t) {
                entriesRepositoryCallback.onFailure(t);
            }
        }, databaseWriteExecutor);
    }

    public LiveData<List<String>> getPlantsNames() {
        return gasolinePlantDao.getPlantsNames();
    }
}
