package dia.units.refuelapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refuelapp.R;

import java.util.List;

import dia.units.refuelapp.db.PlantAndPricesViewModel;
import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.entities.GasolinePrice;
import dia.units.refuelapp.db.entities.PlantWithPrices;

public class MainActivity extends AppCompatActivity {
    private PlantAndPricesViewModel plantAndPricesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()){
            //launch the job scheduler service
        }

        // get location
        //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final PlantsAdapter plantsAdapter = new PlantsAdapter();
        recyclerView.setAdapter(plantsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setHasFixedSize(true);



        plantAndPricesViewModel = new ViewModelProvider(this).get(PlantAndPricesViewModel.class);
        //check here if there are any data and in case download both csv and save them in room
        insertData(plantAndPricesViewModel);

        plantAndPricesViewModel.getAllPlantsWithPrices().observe(this, plantWithPrices -> {
            //update livedata
            plantsAdapter.setPlants(plantWithPrices);
            Log.i("lte", String.valueOf(plantWithPrices.isEmpty()));
            Log.i("lte", String.valueOf(plantWithPrices.size()));
        });
    }

    private void insertData(PlantAndPricesViewModel plantAndPricesViewModel) {
        plantAndPricesViewModel.insertGasolinePlant(new GasolinePlant(9737, "RIVA STEFANO", "Esso", "Altro","Stazione di servizio ESSO di Riva Stefano", "PIAZZALE VALMAURA 4 34148","TRIESTE","TS",45.625284722136534, 13.795276130424782));
        plantAndPricesViewModel.insertGasolinePlant(new GasolinePlant(47617,"ZOCCO GIOVANNI","Api-Ip","Altro","STAZIONE DI SERVIZIO IP","PASSEGGIO SANT'ANDREA 10 34123","TRIESTE","TS",45.639361582637804,13.760304350205974));
        plantAndPricesViewModel.insertGasolinePlant(new GasolinePlant(38629,"INTERAUTO DI PRUTEANU DANIELA","Q8","Altro","INTERAUTO","strada della rosandra 62 34147","TRIESTE","TS",45.61287773636554, 13.825717940926552));
        plantAndPricesViewModel.insertGasolinePlant(new GasolinePlant(38618, "ZOL FRANCO - CARBURANTI", "Agip Eni", "Strada Statale", "eni4044", "Statale 14 della Venezia Giulia, Km. 1, dir. tieste 34121","TRIESTE","TS",45.66479868882435 ,13.767396658658981));
        Log.i("lte", "plant inseriti");
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(9737, "Benzina", 1.759,0,"26/01/2022 07:22:15"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(9737,"Benzina",1.759,1,"26/01/2022 07:22:15"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(9737,"Gasolio",1.679,1,"26/01/2022 07:22:15"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(9737,"Gasolio",1.679,0,"26/01/2022 07:22:15"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(47617,"Benzina",1.779,0,"27/01/2022 09:17:22"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(47617,"Gasolio",1.679,0,"27/01/2022 09:17:22"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(38629, "Benzina", 1.789,1,"28/01/2022 07:01:57"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(38629, "Benzina", 1.939,0,"28/01/2022 07:01:57"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(38629, "Gasolio", 1.849,0,"28/01/2022 07:01:57"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(38629, "Gasolio", 1.699,1,"28/01/2022 07:01:57"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(38618, "Benzina", 1.999, 0,"22/01/2022 09:46:49"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(38618, "Benzina", 1.779, 1,"22/01/2022 09:46:49"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(38618, "Blue Diesel",1.999, 0,"22/01/2022 09:46:49"));
        plantAndPricesViewModel.insertGasolinePrice(new GasolinePrice(38618, "Blue Diesel", 1.779, 1,"22/01/2022 09:46:49"));
        Log.i("lte", "prices inseriti");
    }
}