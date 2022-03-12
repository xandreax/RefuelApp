package dia.units.refuelapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.refuelapp.R;

import java.util.List;

import dia.units.refuelapp.data.PlantAndPricesViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {
    private PlantAndPricesViewModel plantsListViewModel;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        plantsListViewModel = new ViewModelProvider(requireActivity()).get(PlantAndPricesViewModel.class);
        ListPreference fuelPreferenceList = findPreference("fuelType");
        SharedPreferences prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String fuelType = prefs.getString("fuelType", "Benzina");
        plantsListViewModel.getFuelTypes().observe(this, fuelTypes -> {
            fuelPreferenceList.setEntries(fuelTypes.toArray(new String[0]));
            fuelPreferenceList.setEntryValues(fuelTypes.toArray(new String[0]));
            fuelPreferenceList.setSummary(fuelType);
        });
        fuelPreferenceList.setOnPreferenceChangeListener((preference, newValue) -> {
            fuelPreferenceList.setSummary(newValue.toString());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("fuelType", newValue.toString());
            editor.apply();
            return true;
        });
    }
}
