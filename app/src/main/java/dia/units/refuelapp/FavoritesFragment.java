package dia.units.refuelapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuelapp.R;

import java.util.List;

import dia.units.refuelapp.data.DetailsPlantViewModel;
import dia.units.refuelapp.data.PlantAndPricesViewModel;
import dia.units.refuelapp.model.PlantWithPrices;
import dia.units.refuelapp.ui.FavoritesAdapter;
import dia.units.refuelapp.ui.PlantsAdapter;

public class FavoritesFragment extends Fragment {

    private PlantAndPricesViewModel plantsListViewModel;
    private DetailsPlantViewModel detailsPlantViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_favorite_list);
        final FavoritesAdapter favoritesAdapter = new FavoritesAdapter();
        recyclerView.setAdapter(favoritesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        plantsListViewModel = new ViewModelProvider(requireActivity()).get(PlantAndPricesViewModel.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        String fuelType = prefs.getString("fuelType", "Benzina");
        plantsListViewModel.setFuelType(fuelType);
        plantsListViewModel.getFavorites().observe(getViewLifecycleOwner(), listPlantWithPrices -> {
            favoritesAdapter.setFuel_type(plantsListViewModel.getFuelType());
            favoritesAdapter.setActualLocation(plantsListViewModel.getPosition());
            favoritesAdapter.submitList(listPlantWithPrices);
        });
        detailsPlantViewModel = new ViewModelProvider(requireActivity()).get(DetailsPlantViewModel.class);

        favoritesAdapter.setOnItemClickListener(plantWithPrices -> {
            detailsPlantViewModel.getPlant().postValue(plantWithPrices);
            if(!detailsPlantViewModel.getPlant().hasActiveObservers()){
                NavController navController = NavHostFragment.findNavController(this);
                navController.getGraph().findNode(R.id.detailGasolinePlantFragment).setLabel(plantWithPrices.gasolinePlant.getName());
                navController.navigate(R.id.action_favoritesFragment_to_detailGasolinePlantFragment);
            }
        });

        return view;
    }
}