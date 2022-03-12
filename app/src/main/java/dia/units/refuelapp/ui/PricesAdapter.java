package dia.units.refuelapp.ui;

import static dia.units.refuelapp.ui.util.UiUtilMethods.setDate;
import static dia.units.refuelapp.ui.util.UiUtilMethods.setIconFuelPump;

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

import dia.units.refuelapp.model.GasolinePrice;

public class PricesAdapter extends ListAdapter<GasolinePrice, PricesAdapter.PriceHolder> {

    public PricesAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<GasolinePrice> DIFF_CALLBACK = new DiffUtil.ItemCallback<GasolinePrice>() {
        @Override
        public boolean areItemsTheSame(@NonNull GasolinePrice oldItem, @NonNull GasolinePrice newItem) {
           return oldItem.getIdPlant() == newItem.getIdPlant() && oldItem.getFuelType().equals(newItem.getFuelType())
                   && oldItem.getIsSelf() == newItem.getIsSelf();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GasolinePrice oldItem, @NonNull GasolinePrice newItem) {
            return oldItem.getUpdateDate().equals(newItem.getUpdateDate()) && oldItem.getFuelType().equals(newItem.getFuelType())
                    && oldItem.getPrice() == newItem.getPrice() && oldItem.getIsSelf() == newItem.getIsSelf();
        }
    };

    @NonNull
    @Override
    public PriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_price_item, parent, false);
        return new PriceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceHolder holder, int position) {
        GasolinePrice price = getItem(position);
        holder.iconFuelType.setImageResource(setIconFuelPump(price));
        holder.fuelType.setText(price.getFuelType());
        if(price.getIsSelf() == 1)
            holder.serviceType.setText(R.string.self_1);
        else
            holder.serviceType.setText(R.string.self_0);
        holder.price.setText(String.valueOf(price.getPrice()));
        holder.date.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(price.getUpdateDate()));
        holder.dateInDays.setText(setDate(price));
    }


    public class PriceHolder extends RecyclerView.ViewHolder {
        private ImageView iconFuelType;
        private TextView fuelType;
        private TextView serviceType;
        private TextView price;
        private TextView date;
        private TextView dateInDays;

        public PriceHolder(@NonNull View itemView) {
            super(itemView);
            iconFuelType = itemView.findViewById(R.id.icon_fuel_pump);
            fuelType = itemView.findViewById(R.id.fuel_type);
            serviceType = itemView.findViewById(R.id.fuel_service);
            price = itemView.findViewById(R.id.fuel_price);
            date = itemView.findViewById(R.id.date_last_update);
            dateInDays = itemView.findViewById(R.id.days_by_last_update);
        }
    }
}
