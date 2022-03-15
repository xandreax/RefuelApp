package dia.units.refuelapp;


import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.refuelapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dia.units.refuelapp.data.PlantAndPricesViewModel;

public class ReportFragment extends Fragment {
    private PlantAndPricesViewModel plantsListViewModel;
    BarChart chart;
    SwitchMaterial self_switch;
    private final List<String> fuels = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        TextView tAvgPriceBenzina = view.findViewById(R.id.avg_price_benzina_report);
        TextView tAvgPriceGasolio = view.findViewById(R.id.avg_price_gasolio_report);
        TextView tAvgPriceGpl = view.findViewById(R.id.avg_price_gpl_report);
        TextView tDateLastUpdate = view.findViewById(R.id.date_last_update_report);
        self_switch = view.findViewById(R.id.switch_self_service);
        chart = (BarChart) view.findViewById(R.id.chart_avg_prices_fuels);

        plantsListViewModel = new ViewModelProvider(requireActivity()).get(PlantAndPricesViewModel.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dateLastUpdate = prefs.getString("data_last_update", "Non disponibile");
        tDateLastUpdate.setText(dateLastUpdate);
        chart.getXAxis().setTextColor(Color.rgb(0, 121, 107));
        fuels.add("Benzina");
        fuels.add("Gasolio");
        fuels.add("GPL");
        if(self_switch.isChecked()) {
            plantsListViewModel.getAvgPriceFuel("Benzina", 1, tAvgPriceBenzina);
            plantsListViewModel.getAvgPriceFuel("Gasolio", 1, tAvgPriceGasolio);
            plantsListViewModel.getAvgPriceFuel("GPL", 1, tAvgPriceGpl);
            plantsListViewModel.getAvgPriceFuels(fuels, 1, chart);
        }
        else {
            plantsListViewModel.getAvgPriceFuel("Benzina", 0, tAvgPriceBenzina);
            plantsListViewModel.getAvgPriceFuel("Gasolio", 0, tAvgPriceGasolio);
            plantsListViewModel.getAvgPriceFuel("GPL", 0, tAvgPriceGpl);
            plantsListViewModel.getAvgPriceFuels(fuels, 0, chart);
        }
        self_switch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked) {
                plantsListViewModel.getAvgPriceFuel("Benzina", 1, tAvgPriceBenzina);
                plantsListViewModel.getAvgPriceFuel("Gasolio", 1, tAvgPriceGasolio);
                plantsListViewModel.getAvgPriceFuel("GPL", 1, tAvgPriceGpl);
                plantsListViewModel.getAvgPriceFuels(fuels, 1, chart);
            }
            else {
                plantsListViewModel.getAvgPriceFuel("Benzina", 0, tAvgPriceBenzina);
                plantsListViewModel.getAvgPriceFuel("Gasolio", 0, tAvgPriceGasolio);
                plantsListViewModel.getAvgPriceFuel("GPL", 0, tAvgPriceGpl);
                plantsListViewModel.getAvgPriceFuels(fuels, 0, chart);
            }
        });
        Button btn_add_another_fuel = view.findViewById(R.id.btn_add_another_fuel);
        btn_add_another_fuel.setOnClickListener(view1 ->
                plantsListViewModel.getFuelTypes().observe(getViewLifecycleOwner(), this::showListDialog));
        return view;
    }

    private void showListDialog(List<String> fuelTypes) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Scegli Carburante");
        fuelTypes.remove("Benzina");
        fuelTypes.remove("Gasolio");
        fuelTypes.remove("GPL");
        String[] items = fuelTypes.toArray(new String[0]);
        alertDialog.setSingleChoiceItems(items, -1, (dialogInterface, i) -> {
            if (fuels.size() > 3)
                fuels.remove(fuels.size() - 1);
            fuels.add(items[i]);
        })
                .setPositiveButton("Accetta", (dialogInterface, i) -> {
                    if(self_switch.isChecked())
                        plantsListViewModel.getAvgPriceFuels(fuels, 1, chart);
                    else
                        plantsListViewModel.getAvgPriceFuels(fuels, 0, chart);
                    dialogInterface.dismiss();
                })
                .setNeutralButton("Annulla", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.create().show();
    }
}