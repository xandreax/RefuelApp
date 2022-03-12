package dia.units.refuelapp.ui;

import static dia.units.refuelapp.ui.util.UiUtilMethods.setLogo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refuelapp.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

import dia.units.refuelapp.model.RefuelItem;
import dia.units.refuelapp.model.RefuelItemWithPlantInfo;

public class RefuelItemsAdapter extends ListAdapter<RefuelItemWithPlantInfo, RefuelItemsAdapter.RefuelViewHolder> {

    public RefuelItemsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<RefuelItemWithPlantInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<RefuelItemWithPlantInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull RefuelItemWithPlantInfo oldItem, @NonNull RefuelItemWithPlantInfo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull RefuelItemWithPlantInfo oldItem, @NonNull RefuelItemWithPlantInfo newItem) {
            return oldItem.getId() == newItem.getId();
        }
    };

    @NonNull
    @Override
    public RefuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refuel_account_item, parent, false);
        return new RefuelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefuelViewHolder holder, int position) {
        RefuelItemWithPlantInfo item = getItem(position);
        holder.money.setText(String.valueOf(item.getMoney()));
        holder.liters.setText(String.valueOf(item.getLiters()));
        holder.kilometers.setText(String.valueOf(item.getKilometers()));
        holder.update_date.setText(new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(item.getUpdateDate()));
        holder.namePlant.setText(item.getName());
        holder.price_by_liter.setText(String.valueOf(item.getPrice_by_liter()));
        holder.logoFuelCompany.setImageResource(setLogo(item.getFlagCompany()));
    }

    public class RefuelViewHolder extends RecyclerView.ViewHolder{
        private ImageView logoFuelCompany;
        private TextView namePlant;
        private TextView update_date;
        private TextView price_by_liter;
        private TextView kilometers;
        private TextView money;
        private TextView liters;

        public RefuelViewHolder(@NonNull View itemView) {
            super(itemView);
            logoFuelCompany = itemView.findViewById(R.id.logo_plant_refuel_item);
            namePlant = itemView.findViewById(R.id.name_plant_refuel_item);
            update_date = itemView.findViewById(R.id.date_refuel_item);
            price_by_liter = itemView.findViewById(R.id.price_by_liter_refuel_item);
            kilometers = itemView.findViewById(R.id.kilometers_refuel_item);
            money = itemView.findViewById(R.id.money_refuel_item);
            liters = itemView.findViewById(R.id.liters_refuel_item);
        }
    }
}
