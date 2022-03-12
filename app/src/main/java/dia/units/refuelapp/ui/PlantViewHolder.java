package dia.units.refuelapp.ui;

import static dia.units.refuelapp.ui.util.UiUtilMethods.setDate;
import static dia.units.refuelapp.ui.util.UiUtilMethods.setLogo;

import android.content.SharedPreferences;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refuelapp.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import dia.units.refuelapp.model.GasolinePlant;
import dia.units.refuelapp.model.GasolinePrice;
import dia.units.refuelapp.model.PlantWithPrices;

class PlantViewHolder extends RecyclerView.ViewHolder{
    private ImageView imageLogo;
    private TextView textAddress;
    private TextView textDistance;
    private TextView textPrice;
    private TextView textDate;
    private TextView textSelf;
    private TextView textNamePlant;

    public PlantViewHolder(@NonNull View itemView) {
        super(itemView);
        imageLogo = itemView.findViewById(R.id.logo);
        textAddress = itemView.findViewById(R.id.indirizzo);
        textDistance = itemView.findViewById(R.id.distanza);
        textPrice = itemView.findViewById(R.id.prezzo);
        textDate = itemView.findViewById(R.id.data_aggiornamento);
        textNamePlant = itemView.findViewById(R.id.name_plant);
        textSelf = itemView.findViewById(R.id.self);
    }

    static PlantViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_plant_item, parent, false);
        return new PlantViewHolder(view);
    }

    public void bind(PlantWithPrices plantWithPrices, Location actualLocation, String fuel_type) {
        textNamePlant.setText(plantWithPrices.gasolinePlant.getName());
        textAddress.setText(plantWithPrices.gasolinePlant.getAddress());
        List<GasolinePrice> prices = plantWithPrices.gasolinePrices.stream().filter(x -> x.getFuelType().equals(fuel_type)).collect(Collectors.toList());
        GasolinePrice SelectedFuelPrice = Collections.min(prices, Comparator.comparing(GasolinePrice::getPrice));
        textPrice.setText(String.valueOf(SelectedFuelPrice.getPrice()));
        if(SelectedFuelPrice.getIsSelf() == 1)
            textSelf.setText(R.string.self_1);
        else
            textSelf.setText(R.string.self_0);
        String date = setDate(SelectedFuelPrice);
        textDate.setText(date);
        imageLogo.setImageResource(setLogo(plantWithPrices.gasolinePlant.getFlagCompany()));
        Location locationPlant = new Location("");
        locationPlant.setLatitude(plantWithPrices.gasolinePlant.getLatitude());
        locationPlant.setLongitude(plantWithPrices.gasolinePlant.getLongitude());
        float distanceFloat = actualLocation != null ? actualLocation.distanceTo(locationPlant) : 0;
        String distance;
        if(distanceFloat < 1000){
            distance = String.format(Locale.getDefault(), "%.0f m", distanceFloat);
        }
        else if (distanceFloat < 10000){
            distance = String.format(Locale.getDefault(), "%.2f km", distanceFloat/1000);
        }
        else if (distanceFloat < 100000){
            distance = String.format(Locale.getDefault(), "%.1f km", distanceFloat/1000);
        }
        else{
            distance = String.format(Locale.getDefault(), "%.0f km", distanceFloat/1000);
        }
        textDistance.setText(distance);
    }
}
