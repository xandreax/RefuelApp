package dia.units.refuelapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.refuelapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import dia.units.refuelapp.data.PlantAndPricesViewModel;
import dia.units.refuelapp.data.RefuelItemViewModel;
import dia.units.refuelapp.model.RefuelItem;

public class AddRefuelFragment extends Fragment {
    private TextInputEditText etLitri;
    private TextInputEditText etMoney;
    private TextInputEditText etPriceLiter;
    private TextInputEditText etkilometers;
    private TextInputEditText etDate;
    private MaterialAutoCompleteTextView etPlant;
    private Button btn_save_refuel_item;
    private RefuelItemViewModel refuelItemViewModel;
    final Calendar myCalendar = Calendar.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_refuels_item_fragment, container, false);
        etLitri = view.findViewById(R.id.et_add_liters);
        etMoney = view.findViewById(R.id.et_add_money);
        etPriceLiter = view.findViewById(R.id.et_add_priceliter);
        etkilometers = view.findViewById(R.id.et_add_km);
        etPlant = view.findViewById(R.id.et_add_plant);
        etDate = view.findViewById(R.id.et_add_date);
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Seleziona una data");
        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        etDate.setOnClickListener(view12 -> {
            materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
        });
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
            String formattedDate  = format.format(calendar.getTime());
            etDate.setText(formattedDate);
        });
        PlantAndPricesViewModel plantAndPricesViewModel = new ViewModelProvider(this).get(PlantAndPricesViewModel.class);
        btn_save_refuel_item = view.findViewById(R.id.btn_save_refuel_item);
        refuelItemViewModel = new ViewModelProvider(requireActivity()).get(RefuelItemViewModel.class);
        plantAndPricesViewModel.getPlantsNames().observe(getViewLifecycleOwner(), names -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, names);
            etPlant.setAdapter(adapter);
        });
        btn_save_refuel_item.setOnClickListener(view1 -> {
            saveRefuelItem();
        });

        return view;
    }

    private void saveRefuelItem() {
        String litri = etLitri.getText().toString();
        String money = etMoney.getText().toString();
        String priceLiter = etPriceLiter.getText().toString();
        String kilometers = etkilometers.getText().toString();
        String date = etDate.getText().toString();
        String plant = etPlant.getText().toString();

        if (TextUtils.isEmpty(litri)) {
            etLitri.setError("Inserire i litri");
            etLitri.requestFocus();
        } else if (TextUtils.isEmpty(money)) {
            etMoney.setError("Inserire i soldi spesi");
            etMoney.requestFocus();
        } else if (TextUtils.isEmpty(priceLiter)) {
            etPriceLiter.setError("Inserire il prezzo al litro");
            etPriceLiter.requestFocus();
        } else if (TextUtils.isEmpty(plant)) {
            etPlant.setError("Inserire il distributore");
            etPlant.requestFocus();
        } else if (TextUtils.isEmpty(date)) {
            etPlant.setError("Inserire la data");
            etPlant.requestFocus();
        } else {
            try {
                double dLitri = Double.parseDouble(litri);
                double dMoney = Double.parseDouble(money);
                double dPriceLiter = Double.parseDouble(priceLiter);
                double dKilometers = 0;
                Date dDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).parse(date);
                if (!TextUtils.isEmpty(kilometers))
                    dKilometers = Double.parseDouble(kilometers);
                RefuelItem item = new RefuelItem(plant, dMoney, dLitri, dPriceLiter, dKilometers, dDate);
                refuelItemViewModel.insertRefuelItem(item);
                Toast.makeText(getContext(), "Rifornimento inserito", Toast.LENGTH_SHORT).show();
                NavController navController = NavHostFragment.findNavController(this);
                navController.popBackStack();
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Some value is not correctly formed, try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
