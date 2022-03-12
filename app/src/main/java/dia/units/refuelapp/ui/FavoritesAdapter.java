package dia.units.refuelapp.ui;

import android.location.Location;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import dia.units.refuelapp.model.PlantWithPrices;

public class FavoritesAdapter extends ListAdapter<PlantWithPrices, PlantViewHolder> {
    private PlantsAdapter.onItemClickListener listener;
    private Location actualLocation;
    private String fuel_type;

    private static final DiffUtil.ItemCallback<PlantWithPrices> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlantWithPrices>() {
        @Override
        public boolean areItemsTheSame(@NonNull PlantWithPrices oldItem, @NonNull PlantWithPrices newItem) {
            return oldItem.gasolinePlant.getIdPlant() == newItem.gasolinePlant.getIdPlant();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlantWithPrices oldItem, @NonNull PlantWithPrices newItem) {
            return oldItem.gasolinePlant.getIdPlant() == newItem.gasolinePlant.getIdPlant() && oldItem.gasolinePrices.get(0).getUpdateDate().equals(newItem.gasolinePrices.get(0).getUpdateDate());
        }
    };

    public FavoritesAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlantViewHolder plantViewHolder = PlantViewHolder.create(parent);
        plantViewHolder.itemView.setOnClickListener(view -> {
            int position = plantViewHolder.getBindingAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(getItem(position));
            }
        });
        return plantViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        PlantWithPrices currentPlan = getItem(position);
        if (currentPlan != null)
            holder.bind(currentPlan, actualLocation, fuel_type);
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public void setActualLocation(Location actualLocation) {
        this.actualLocation = actualLocation;
    }

    public interface onItemClickListener {
        void onItemClick(PlantWithPrices plantWithPrices);
    }

    public void setOnItemClickListener(PlantsAdapter.onItemClickListener listener) {
        this.listener = listener;
    }
}
