package dia.units.refuelapp.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dia.units.refuelapp.model.GasolinePrice;
import dia.units.refuelapp.model.PlantWithPrices;

public class PlantsAdapter extends PagingDataAdapter<PlantWithPrices, PlantViewHolder> {
    private onItemClickListener listener;

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

    public PlantsAdapter() {
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
            holder.bind(currentPlan);
    }

    public interface onItemClickListener {
        void onItemClick(PlantWithPrices plantWithPrices);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
