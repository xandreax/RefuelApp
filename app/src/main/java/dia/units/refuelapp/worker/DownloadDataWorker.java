package dia.units.refuelapp.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import dia.units.refuelapp.data.RepositoryPlantsAndPrices;

public class DownloadDataWorker extends Worker {
    private static final String URL_PREZZI = "https://www.mise.gov.it/images/exportCSV/prezzo_alle_8.csv";
    private static final String URL_IMPIANTI = "https://www.mise.gov.it/images/exportCSV/anagrafica_impianti_attivi.csv";

    public DownloadDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        RepositoryPlantsAndPrices repositoryPlantsAndPrices = new RepositoryPlantsAndPrices(getApplicationContext());
        repositoryPlantsAndPrices.makeUpdateDataRequest();
        Log.i("lte", String.valueOf(Runtime.getRuntime().availableProcessors()));
        return null;
    }
}
