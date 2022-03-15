package dia.units.refuelapp.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dia.units.refuelapp.data.RepositoryPlantsAndPrices;

public class DownloadDataWorker extends Worker {

    public DownloadDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        RepositoryPlantsAndPrices repositoryPlantsAndPrices = new RepositoryPlantsAndPrices(getApplicationContext());
        repositoryPlantsAndPrices.makeUpdateDataRequest();
        Log.i("lte", String.valueOf(Runtime.getRuntime().availableProcessors()));
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("data_last_update", date);
        editor.apply();
        return null;
    }
}
