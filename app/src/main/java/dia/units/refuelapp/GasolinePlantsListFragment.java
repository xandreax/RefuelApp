package dia.units.refuelapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refuelapp.R;

import javax.annotation.Nonnull;

import dia.units.refuelapp.data.DetailsPlantViewModel;
import dia.units.refuelapp.data.PlantAndPricesViewModel;
import dia.units.refuelapp.ui.PlantsAdapter;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class GasolinePlantsListFragment extends Fragment {

    private PlantAndPricesViewModel plantsListViewModel;
    private DetailsPlantViewModel detailsPlantViewModel;
    private ProgressBar progressBar;
    private TextView progressBarTextView;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plants_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_gasoline_list);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarTextView = view.findViewById(R.id.text_progressBar);
        //loading();
        final PlantsAdapter plantsAdapter = new PlantsAdapter();
        recyclerView.setAdapter(plantsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        plantsListViewModel = new ViewModelProvider(requireActivity()).get(PlantAndPricesViewModel.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        String fuelType = prefs.getString("fuelType", "Benzina");
        plantsListViewModel.setFuelType(fuelType);
        plantsListViewModel.getAllPlantsWithPrices().observe(getViewLifecycleOwner(), listPlantWithPrices -> {
            //update livedata
            plantsAdapter.setFuel_type(plantsListViewModel.getFuelType());
            plantsAdapter.setActualLocation(plantsListViewModel.getPosition());
            plantsAdapter.submitData(getLifecycle(), listPlantWithPrices);
            //listener
            plantsAdapter.addLoadStateListener(combinedLoadStates -> {
                if (!(combinedLoadStates.getRefresh() instanceof LoadState.NotLoading)) {
                    return Unit.INSTANCE; // this is the void equivalent in kotlin
                }
                if (plantsAdapter.getItemCount() == 0) {
                    loading();
                } else {
                    stopLoading();
                }
                return Unit.INSTANCE; // this is the void equivalent in kotlin
            });
            Log.i("lte", "Size: " + plantsAdapter.getItemCount());
        });
        detailsPlantViewModel = new ViewModelProvider(requireActivity()).get(DetailsPlantViewModel.class);

        plantsAdapter.setOnItemClickListener(plantWithPrices -> {
            detailsPlantViewModel.getPlant().postValue(plantWithPrices);
            if (!detailsPlantViewModel.getPlant().hasActiveObservers()) {
                NavController navController = NavHostFragment.findNavController(this);
                navController.getGraph().findNode(R.id.detailGasolinePlantFragment).setLabel(plantWithPrices.gasolinePlant.getName());
                navController.navigate(R.id.action_gasolinePlantsListFragment_to_detailGasolinePlantFragment);
            }
        });
        return view;
    }

    private void loading() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        progressBar.setVisibility(View.GONE);
        progressBarTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }
}
