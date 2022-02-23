package dia.units.refuelapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refuelapp.R;

import java.util.Objects;

import dia.units.refuelapp.model.PlantWithPrices;
import dia.units.refuelapp.ui.DetailsPlantViewModel;
import dia.units.refuelapp.ui.ItemClickListener;
import dia.units.refuelapp.ui.PlantAndPricesViewModel;
import dia.units.refuelapp.ui.PlantsAdapter;

public class GasolinePlantsListFragment extends Fragment {

    private PlantAndPricesViewModel plantsListViewModel;
    private DetailsPlantViewModel detailsPlantViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plants_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        final PlantsAdapter plantsAdapter = new PlantsAdapter();
        recyclerView.setAdapter(plantsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        plantsListViewModel = new ViewModelProvider(requireActivity()).get(PlantAndPricesViewModel.class);
        plantsListViewModel.getAllPlantsWithPrices().observe(getViewLifecycleOwner(), listPlantWithPrices -> {
            //update livedata
            plantsAdapter.submitData(getLifecycle(), listPlantWithPrices);
            Log.i("lte", "Size: "+ plantsAdapter.snapshot().getItems().size());
        });
        detailsPlantViewModel = new ViewModelProvider(requireActivity()).get(DetailsPlantViewModel.class);

        plantsAdapter.setOnItemClickListener(plantWithPrices -> {
            detailsPlantViewModel.getPlant().postValue(plantWithPrices);
            if(!detailsPlantViewModel.getPlant().hasActiveObservers()){
                NavController navController = NavHostFragment.findNavController(this);
                navController.getGraph().findNode(R.id.detailGasolinePlantFragment).setLabel(plantWithPrices.gasolinePlant.getName());
                navController.navigate(R.id.action_gasolinePlantsListFragment_to_detailGasolinePlantFragment);
                /*DetailGasolinePlantFragment detailGasolinePlantFragment = new DetailGasolinePlantFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentsContainer, detailGasolinePlantFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });
        return view;
    }
}
