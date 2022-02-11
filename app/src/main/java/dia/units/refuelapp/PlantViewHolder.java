package dia.units.refuelapp;

import android.util.Log;
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

import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.entities.GasolinePrice;
import dia.units.refuelapp.db.entities.PlantWithPrices;

class PlantViewHolder extends RecyclerView.ViewHolder{
    private ImageView imageLogo;
    private TextView textAddress;
    private TextView textDistance;
    private TextView textPrice;
    private TextView textDate;
    private TextView textSelf;

    public PlantViewHolder(@NonNull View itemView) {
        super(itemView);
        imageLogo = itemView.findViewById(R.id.logo);
        textAddress = itemView.findViewById(R.id.indirizzo);
        textDistance = itemView.findViewById(R.id.distanza);
        textPrice = itemView.findViewById(R.id.prezzo);
        textDate = itemView.findViewById(R.id.data_aggiornamento);
        textSelf = itemView.findViewById(R.id.self);
    }

    static PlantViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gasoline_plant_item, parent, false);
        return new PlantViewHolder(view);
    }

    public void bind(PlantWithPrices plantWithPrices) {
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
            String giorni = "(" + gasolineBestPrice.getDaysOfLastUpdate(today) + " giorni fa)";
            textDate.setText(giorni);
        }
        setImageLogo(plantWithPrices.gasolinePlant);
        textDistance.setText(String.valueOf(100));
    }

    private void setImageLogo(GasolinePlant gasolinePlant){
        switch (gasolinePlant.getFlagCompany()){
            case "Esso":
                imageLogo.setImageResource(R.drawable.esso);
                break;
            case "Api-Ip":
                imageLogo.setImageResource(R.drawable.api_ip);
                break;
            default:
                imageLogo.setImageResource(R.drawable.distributore);
        }
    }
}
