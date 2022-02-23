package dia.units.refuelapp;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.refuelapp.R;

import java.util.Objects;

import dia.units.refuelapp.model.PlantWithPrices;
import dia.units.refuelapp.ui.DetailsPlantViewModel;

public class DetailGasolinePlantFragment extends Fragment {
    private DetailsPlantViewModel detailsPlantViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_gasoline_plant, container, false);
        detailsPlantViewModel = new ViewModelProvider(requireActivity()).get(DetailsPlantViewModel.class);
        TextView textViewName = view.findViewById(R.id.detail_name_plant);
        detailsPlantViewModel.getPlant().observe(getViewLifecycleOwner(), new Observer<PlantWithPrices>() {
            @Override
            public void onChanged(PlantWithPrices plantWithPrices) {
                textViewName.setText(plantWithPrices.gasolinePlant.getName());
            }
        });
        return view;
    }
}