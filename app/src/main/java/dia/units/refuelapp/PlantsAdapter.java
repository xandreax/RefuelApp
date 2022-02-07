package dia.units.refuelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refuelapp.R;

import java.util.ArrayList;
import java.util.List;

import dia.units.refuelapp.db.entities.GasolinePlant;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.PlantHolder> {
    private List<GasolinePlant> gasolinePlants = new ArrayList<>();

    @NonNull
    @Override
    public PlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gasoline_plant_item, parent, false);
        return new PlantHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantHolder holder, int position) {
        GasolinePlant currentPlant = gasolinePlants.get(position);
        holder.textAddress.setText(currentPlant.getAddress());
        // definisci gli altri cmapi
    }

    @Override
    public int getItemCount() {
        return gasolinePlants.size();
    }

    public void setGasolinePlants(List<GasolinePlant> gasolinePlants){
        this.gasolinePlants = gasolinePlants;
        //update data
    }

    class PlantHolder extends RecyclerView.ViewHolder {
        private ImageView imageLogo;
        private TextView textAddress;
        private TextView textDistance;
        private TextView textPrice;
        private TextView textDate;
        private TextView textSelf;

        public PlantHolder(@NonNull View itemView) {
            super(itemView);
            imageLogo = itemView.findViewById(R.id.logo);
            textAddress = itemView.findViewById(R.id.indirizzo);
            textDistance = itemView.findViewById(R.id.distanza);
            textPrice = itemView.findViewById(R.id.prezzo);
            textDate = itemView.findViewById(R.id.data_aggiornamento);
            textSelf = itemView.findViewById(R.id.self);
        }
    }
}
