package dia.units.refuelapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refuelapp.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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

    public void bind(PlantWithPrices plantWithPrices) {
        textNamePlant.setText(plantWithPrices.gasolinePlant.getName());
        textAddress.setText(plantWithPrices.gasolinePlant.getAddress());
        GasolinePrice gasolineBestPrice = Collections.min(plantWithPrices.gasolinePrices, Comparator.comparing(GasolinePrice::getPrice));
        textPrice.setText(String.valueOf(gasolineBestPrice.getPrice()));
        if(gasolineBestPrice.getIsSelf() == 1)
            textSelf.setText(R.string.self_1);
        else
            textSelf.setText(R.string.self_0);
        Date today = new Date();
        if(gasolineBestPrice.getDaysOfLastUpdate(today) == 0 )
            textDate.setText(R.string.oggi);
        else if(gasolineBestPrice.getDaysOfLastUpdate(today) == 1 )
            textDate.setText(R.string.ieri);
        else {
            String days = "(" + gasolineBestPrice.getDaysOfLastUpdate(today) + " giorni fa)";
            textDate.setText(days);
        }
        setImageLogo(plantWithPrices.gasolinePlant);
        String distance = 100 +" m";
        textDistance.setText(distance);
    }

    private void setImageLogo(GasolinePlant gasolinePlant){
        switch (gasolinePlant.getFlagCompany()){
            case "Api-Ip":
                imageLogo.setImageResource(R.drawable.api_ip);
                break;
            case "Beyfin":
                imageLogo.setImageResource(R.drawable.beyfin);
                break;
            case "Costantin":
                imageLogo.setImageResource(R.drawable.costantin);
                break;
            case "Ego":
                imageLogo.setImageResource(R.drawable.ego);
                break;
            case "Energas":
                imageLogo.setImageResource(R.drawable.energas);
                break;
            case "Agip Eni":
                imageLogo.setImageResource(R.drawable.eni);
                break;
            case "Esso":
                imageLogo.setImageResource(R.drawable.esso);
                break;
            case "Europam":
                imageLogo.setImageResource(R.drawable.europam);
                break;
            case "Giap":
                imageLogo.setImageResource(R.drawable.giap);
                break;
            case "KEROPETROL":
                imageLogo.setImageResource(R.drawable.keropetrol);
                break;
            case "Oil Italia":
                imageLogo.setImageResource(R.drawable.oil_italia);
                break;
            case "Pompe Bianche":
                imageLogo.setImageResource(R.drawable.pompebianche);
                break;
            case "Q8":
                imageLogo.setImageResource(R.drawable.q8);
                break;
            case "Repsol":
                imageLogo.setImageResource(R.drawable.repsol);
                break;
            case "Shell":
                imageLogo.setImageResource(R.drawable.shell);
                break;
            case "Sia fuel":
                imageLogo.setImageResource(R.drawable.sia_fuel);
                break;
            case "San Marco Petroli":
                imageLogo.setImageResource(R.drawable.ic_smp);
                break;
            case "Tamoil":
                imageLogo.setImageResource(R.drawable.tamoil);
                break;
            case "Total Erg":
                imageLogo.setImageResource(R.drawable.totalerg);
                break;
            case "Vega":
                imageLogo.setImageResource(R.drawable.vega);
                break;
            default:
                imageLogo.setImageResource(R.drawable.distributore);
        }
    }
}
