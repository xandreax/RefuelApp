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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import dia.units.refuelapp.db.entities.GasolinePlant;
import dia.units.refuelapp.db.entities.GasolinePrice;
import dia.units.refuelapp.db.entities.PlantWithPrices;

public class PlantsAdapter extends RecyclerView.Adapter<PlantViewHolder> {
    private List<PlantWithPrices> plantWithPrices = new ArrayList<>();

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlantViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        PlantWithPrices currentPlan = plantWithPrices.get(position);
        holder.bind(currentPlan);
    }

    @Override
    public int getItemCount() {
        return plantWithPrices.size();
    }

    public void setPlants(List<PlantWithPrices> plantWithPrices){
        this.plantWithPrices = plantWithPrices;
        notifyDataSetChanged();
    }
}
