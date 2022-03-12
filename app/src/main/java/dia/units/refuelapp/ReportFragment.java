package dia.units.refuelapp;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.refuelapp.R;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

import dia.units.refuelapp.data.PlantAndPricesViewModel;

public class ReportFragment extends Fragment {
    private PlantAndPricesViewModel plantsListViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        TextView tAvgPriceBenzina = view.findViewById(R.id.avg_price_benzina_report);
        TextView tAvgPriceGasolio = view.findViewById(R.id.avg_price_gasolio_report);
        TextView tAvgPriceGpl = view.findViewById(R.id.avg_price_gpl_report);
        plantsListViewModel = new ViewModelProvider(requireActivity()).get(PlantAndPricesViewModel.class);
        plantsListViewModel.getAvgPriceFuel("Benzina", 0, tAvgPriceBenzina);
        plantsListViewModel.getAvgPriceFuel("Gasolio", 0, tAvgPriceGasolio);
        plantsListViewModel.getAvgPriceFuel("GPL", 0, tAvgPriceGpl);
        BarChart chart = (BarChart) view.findViewById(R.id.chart_avg_prices_fuels);
        chart.getXAxis().setTextColor(Color.rgb(0,121,107));
        List<String> fuels = new ArrayList<>();
        fuels.add("Benzina");
        fuels.add("Gasolio");
        fuels.add("GPL");
        plantsListViewModel.getAvgPriceFuels(fuels, 0, chart);

        Button btn_add_another_fuel = view.findViewById(R.id.btn_add_another_fuel);
        btn_add_another_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantsListViewModel.getFuelTypes().observe(getViewLifecycleOwner(), fuelTypes -> {
                    showListDialog(fuelTypes);
                });
            }
        });

        return view;
    }

    private void showListDialog(List<String> fuelTypes) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Scegli Carburante");
        fuelTypes.remove("Benzina");
        fuelTypes.remove("Gasolio");
        fuelTypes.remove("GPL");
        String[] items = fuelTypes.toArray(new String[0]);
    }
}