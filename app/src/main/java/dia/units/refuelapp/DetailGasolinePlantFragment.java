package dia.units.refuelapp;

import static dia.units.refuelapp.ui.util.UiUtilMethods.setLogo;
import static dia.units.refuelapp.ui.util.UiUtilMethods.setStradaStatale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.refuelapp.R;
import com.google.firebase.auth.FirebaseAuth;

import dia.units.refuelapp.data.DetailsPlantViewModel;
import dia.units.refuelapp.data.PlantAndPricesViewModel;
import dia.units.refuelapp.ui.PricesAdapter;

public class DetailGasolinePlantFragment extends Fragment {
    private PlantAndPricesViewModel plantsListViewModel;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_gasoline_plant, container, false);
        DetailsPlantViewModel detailsPlantViewModel = new ViewModelProvider(requireActivity()).get(DetailsPlantViewModel.class);
        plantsListViewModel = new ViewModelProvider(requireActivity()).get(PlantAndPricesViewModel.class);
        TextView textViewAddress = view.findViewById(R.id.detail_address);
        TextView textViewCity = view.findViewById(R.id.detail_city);
        ImageView detailLogo = view.findViewById(R.id.detail_logo);
        Button locationButton = view.findViewById(R.id.detail_location_button);
        CheckBox isFavoriteCheckBox = view.findViewById(R.id.checkbox_add_favorite);
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        Button submitButton = view.findViewById(R.id.submit_rating_button);
        RecyclerView pricesRecyclerView = view.findViewById(R.id.detail_gasoline_prices_recyclerview);
        PricesAdapter pricesAdapter = new PricesAdapter();
        pricesRecyclerView.setAdapter(pricesAdapter);
        pricesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        detailsPlantViewModel.getPlant().observe(getViewLifecycleOwner(), plantWithPrices -> {
            plantsListViewModel.checkIfPlantIsFavorite(plantWithPrices.gasolinePlant.getIdPlant(), isFavoriteCheckBox);
            detailLogo.setImageResource(setLogo(plantWithPrices.gasolinePlant.getFlagCompany()));
            String address = setStradaStatale(plantWithPrices.gasolinePlant);
            textViewAddress.setText(address);
            String city = plantWithPrices.gasolinePlant.getCity() + " (" + plantWithPrices.gasolinePlant.getProvince() + ")";
            textViewCity.setText(city);
            pricesAdapter.submitList(plantWithPrices.gasolinePrices);
            if(plantWithPrices.gasolinePrices.size() > 4){
                ViewGroup.LayoutParams params = pricesRecyclerView.getLayoutParams();
                params.height = 1000;
                pricesRecyclerView.setLayoutParams(params);
            }
            locationButton.setOnClickListener(view1 -> {
                Uri intentUri = Uri.parse("geo:" + plantWithPrices.gasolinePlant.getLatitude() + ","
                        + plantWithPrices.gasolinePlant.getLongitude()
                        + "?q=" + plantWithPrices.gasolinePlant.getLatitude() + ","
                        + plantWithPrices.gasolinePlant.getLongitude());
                Log.i("lte", intentUri.toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                Log.i("lte", "intent created");
                requireContext().startActivity(mapIntent);
            });

            isFavoriteCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    plantsListViewModel.insertFavorite(plantWithPrices.gasolinePlant.getIdPlant());
                    buttonView.setChecked(true);
                }
                else{
                    plantsListViewModel.removeFavorite(plantWithPrices.gasolinePlant.getIdPlant());
                    buttonView.setChecked(false);
                }
            });

            submitButton.setOnClickListener(view12 -> {
                if(FirebaseAuth.getInstance().getCurrentUser() == null)
                    Toast.makeText(getContext(), "E' richiesto il login per inserire la recensione", Toast.LENGTH_SHORT).show();
                else {
                    //if user is logged send to firebase
                    String value = String.valueOf(ratingBar.getRating());
                    Toast.makeText(getContext(), "Rating is: " + value, Toast.LENGTH_SHORT).show();
                }
            });

        });
        return view;
    }
}