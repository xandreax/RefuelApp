package dia.units.refuelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.refuelapp.R;

import java.util.List;

import dia.units.refuelapp.db.PlantViewModel;
import dia.units.refuelapp.db.entities.GasolinePlant;

public class MainActivity extends AppCompatActivity {
    private PlantViewModel plantViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PlantsAdapter plantsAdapter = new PlantsAdapter();
        recyclerView.setAdapter(plantsAdapter);

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        plantViewModel.getAllPlants().observe(this, new Observer<List<GasolinePlant>>() {
            @Override
            public void onChanged(List<GasolinePlant> gasolinePlants) {
                plantsAdapter.setGasolinePlants(gasolinePlants);
            }
        });
    }
}