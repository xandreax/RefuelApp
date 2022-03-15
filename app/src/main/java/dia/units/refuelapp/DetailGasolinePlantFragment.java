package dia.units.refuelapp;

import static dia.units.refuelapp.ui.util.UiUtilMethods.setLogo;
import static dia.units.refuelapp.ui.util.UiUtilMethods.setStradaStatale;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import dia.units.refuelapp.data.DetailsPlantViewModel;
import dia.units.refuelapp.data.PlantAndPricesViewModel;
import dia.units.refuelapp.ui.PricesAdapter;

public class DetailGasolinePlantFragment extends Fragment {
    private PlantAndPricesViewModel plantsListViewModel;
    private TextView avgRating;
    private TextView totRating;
    private final static String PLANT_COLLECTION = "plants";
    private final static String RATING_COLLECTION = "ratings";
    private final static String AVG_RATING = "avgRatings";
    private final static String TOT_RATING = "totRatings";
    private final static String ID_PLANT = "idPlant";
    private final static String USER_UID = "userUid";
    private final static String RATING = "rating";
    FirebaseFirestore db;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_detail_gasoline_plant, container, false);
        DetailsPlantViewModel detailsPlantViewModel = new ViewModelProvider(requireActivity()).get(DetailsPlantViewModel.class);
        plantsListViewModel = new ViewModelProvider(requireActivity()).get(PlantAndPricesViewModel.class);
        avgRating = view.findViewById(R.id.rating_avg);
        totRating = view.findViewById(R.id.rating_tot);
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
            setRatingData(plantWithPrices.gasolinePlant.getIdPlant());
            detailLogo.setImageResource(setLogo(plantWithPrices.gasolinePlant.getFlagCompany()));
            String address = setStradaStatale(plantWithPrices.gasolinePlant);
            textViewAddress.setText(address);
            String city = plantWithPrices.gasolinePlant.getCity() + " (" + plantWithPrices.gasolinePlant.getProvince() + ")";
            textViewCity.setText(city);
            pricesAdapter.submitList(plantWithPrices.gasolinePrices);
            if (plantWithPrices.gasolinePrices.size() > 4) {
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
                if (isChecked) {
                    plantsListViewModel.insertFavorite(plantWithPrices.gasolinePlant.getIdPlant());
                    buttonView.setChecked(true);
                } else {
                    plantsListViewModel.removeFavorite(plantWithPrices.gasolinePlant.getIdPlant());
                    buttonView.setChecked(false);
                }
            });

            submitButton.setOnClickListener(view12 -> {
                if (ratingBar.getRating() == 0) {
                    Toast.makeText(getContext(), "Inserisci una votazione valida", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null)
                    Toast.makeText(getContext(), "E' richiesto il login per inserire la recensione", Toast.LENGTH_SHORT).show();
                else {
                    //if user is logged send to firestore
                    Query docRef = db.collection(RATING_COLLECTION)
                            .whereEqualTo(ID_PLANT, plantWithPrices.gasolinePlant.getIdPlant())
                            .whereEqualTo(USER_UID, user.getUid());
                    int idPlant = plantWithPrices.gasolinePlant.getIdPlant();
                    String userUid = user.getUid();
                    double rating = ratingBar.getRating();
                    docRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            if (document.isEmpty()) {
                                addRatingUser(idPlant, userUid, rating).addOnCompleteListener((insertTask -> {
                                    if (insertTask.isSuccessful()) {
                                        DocumentReference plantRef = db.collection(PLANT_COLLECTION).document(String.valueOf(idPlant));
                                        db.runTransaction((Transaction.Function<Void>) transaction -> {
                                            DocumentSnapshot plant = transaction.get(plantRef);
                                            if (plant.exists()) {
                                                double avgRating = plant.getDouble(AVG_RATING);
                                                double totRating = plant.getDouble(TOT_RATING);
                                                double newAvgRating = ((avgRating * totRating) + rating) / (totRating + 1);
                                                double newTotRating = totRating+1;
                                                transaction.update(plantRef,
                                                        AVG_RATING, newAvgRating, TOT_RATING, newTotRating
                                                );
                                            } else {
                                                Map<String, Object> data = new HashMap<>();
                                                data.put(AVG_RATING, rating);
                                                data.put(TOT_RATING, 1);
                                                transaction.set(plantRef, data);
                                            }
                                            return null;
                                        });
                                        Toast.makeText(getContext(), "Votazione inserita", Toast.LENGTH_SHORT).show();
                                    }
                                }));
                            }
                            else {
                                //rating already exist -> dialog to resaving or cancel
                                double oldRating = task.getResult().getDocuments().get(0).getDouble(RATING);
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                alertDialog.setTitle("Votazione già inserita");
                                alertDialog.setMessage("Vuoi sovrascrivere la precedente recensione?");
                                alertDialog.setPositiveButton(R.string.si, (dialogInterface, i) -> {
                                    DocumentReference plantRef = db.collection(PLANT_COLLECTION).document(String.valueOf(idPlant));
                                    db.runTransaction((Transaction.Function<Void>) transaction2 -> {
                                        DocumentSnapshot plant = transaction2.get(plantRef);
                                        double oldAvgRating = plant.getDouble(AVG_RATING);
                                        double oldTotRating = plant.getDouble(TOT_RATING);
                                        double newAvgRating;
                                        double newTotRating;
                                        if(oldTotRating > 1){
                                            double avgRating = ((oldAvgRating * oldTotRating) - oldRating) / (oldTotRating - 1);
                                            double totRating = oldTotRating+1;
                                            newAvgRating = ((avgRating * totRating) + rating) / (totRating + 1);
                                            newTotRating = totRating+1;
                                        }
                                        else{
                                            newAvgRating = rating;
                                            newTotRating = 1;
                                        }
                                        transaction2.update(plantRef,
                                                AVG_RATING, newAvgRating, TOT_RATING, newTotRating
                                        );
                                        return null;
                                    });
                                    DocumentReference documentReference = document.getDocuments().get(0).getReference();
                                    updateRatingUser(rating, documentReference);
                                    Toast.makeText(getContext(), "Votazione inserita", Toast.LENGTH_SHORT).show();
                                });
                                alertDialog.setNegativeButton(R.string.no, (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                });
                                alertDialog.create().show();
                            }
                            ratingBar.setRating(0);
                        }
                    });
                }
            });
        });
        return view;
    }

    private void updateRatingUser(double rating, DocumentReference ref) {
        ref.update(RATING, rating);
    }

    private void setRatingData(int idPlant) {
        DocumentReference docRef = db.collection(PLANT_COLLECTION).document(String.valueOf(idPlant));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    avgRating.setText(String.valueOf(document.getDouble(AVG_RATING)));
                    double totRatingDouble = document.getDouble(TOT_RATING);
                    String stringTotRating = String.format(Locale.getDefault(), "(%.0f)", totRatingDouble);
                    totRating.setText(stringTotRating);
                } else {
                    addPlantToFirebase(idPlant, 0, 0);
                }
            } else {
                Log.d("lte", "get failed with ", task.getException());
            }
        });
    }

    private Task<Void> addPlantToFirebase(int idPlant, double avgRating, double totRatings) {
        Map<String, Object> data = new HashMap<>();
        data.put(AVG_RATING, avgRating);
        data.put(TOT_RATING, totRatings);
        return db.collection(PLANT_COLLECTION).document(String.valueOf(idPlant)).set(data);
    }

    private Task<Void> addRatingUser(int idPlant, String userUid, double rating) {
        Map<String, Object> data = new HashMap<>();
        data.put(ID_PLANT, idPlant);
        data.put(USER_UID, userUid);
        data.put(RATING, rating);
        return db.collection(RATING_COLLECTION).document().set(data);
    }
}