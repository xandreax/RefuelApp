package dia.units.refuelapp.db;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;

import dia.units.refuelapp.data.Repository;
import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;
import dia.units.refuelapp.volley.InputStreamVolleyRequest;

public class DownloadAndSaveCsvThread implements Runnable {
    private static final String URL_PREZZI = "https://www.mise.gov.it/images/exportCSV/prezzo_alle_8.csv";
    private static final String URL_IMPIANTI = "https://www.mise.gov.it/images/exportCSV/anagrafica_impianti_attivi.csv";
    private Context context;
    private Repository repository;

    public DownloadAndSaveCsvThread(Context context) {
        this.context = context;
        repository = new Repository(context);
    }

    @Override
    public void run() {
        Log.i("lte", "Start reading csv");
        InputStreamVolleyRequest requestPlants = new InputStreamVolleyRequest(Request.Method.GET, URL_IMPIANTI, null, response -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response)));
            List<GasolinePlant> plants = new CsvToBeanBuilder<GasolinePlant>(reader)
                    .withIgnoreQuotations(true)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withOrderedResults(false)
                    .withExceptionHandler(e -> {
                        if(e instanceof CsvDataTypeMismatchException)
                            return null;
                        else
                            throw new CsvException(e.getLocalizedMessage());
                    })
                    .withType(GasolinePlant.class)
                    .build()
                    .parse();
            //insert into db
            Log.i("lte", "Finish reading csv, start to insert");
            repository.insertAllPlants(plants);
            Log.i("lte", "Finish insert");
        }, Throwable::printStackTrace);
        InputStreamVolleyRequest requestPrices = new InputStreamVolleyRequest(Request.Method.GET, URL_PREZZI, null, response -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response)));
            List<GasolinePrice> prices = new CsvToBeanBuilder<GasolinePrice>(reader)
                    .withIgnoreQuotations(true)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withOrderedResults(false)
                    .withType(GasolinePrice.class)
                    .build()
                    .parse();
            //insert into db
            Log.i("lte", "Finish reading csv, start to insert");
            repository.insertAllPrices(prices);
            Log.i("lte", "Finish insert");
        }, Throwable::printStackTrace);
        RequestQueue requestQueue = Volley.newRequestQueue(context, new HurlStack());
        requestQueue.add(requestPrices);
        requestQueue.add(requestPlants);
    }
}
