package dia.units.refuelapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.refuelapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dia.units.refuelapp.ui.PlantAndPricesViewModel;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CODE = 1234;
    private PlantAndPricesViewModel plantAndPricesViewModel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //do stuff on first start
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            doSomethingOnFirstStart();
        }
        //get position
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled())
                getLastLocation();
            else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //startActivity(intent);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
        plantAndPricesViewModel = new ViewModelProvider(this).get(PlantAndPricesViewModel.class);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.gasolinePlantsListFragment, R.id.searchFragment, R.id.favoritesFragment).build();
        Toolbar toolbar = findViewById(R.id.toolbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            Log.i("lte", "Grant Results length: " + grantResults.length);
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                getLastLocation();
            } else {
                // permission not granted
                Log.i("lte", "permission not granted");
                Toast.makeText(this, "Denied location permission: unable to find position", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        Log.i("lte", "capture last location");
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnFailureListener(e -> {
            Log.e("lte", "onFailure: " + e.getLocalizedMessage());
        });
        locationTask.addOnSuccessListener(location -> {
            if (location != null) {
                Log.i("lte", "latitude: " + location.getLatitude());
                Log.i("lte", "longitude: " + location.getLongitude());
                plantAndPricesViewModel.setPosition(location);
            } else {
                Log.i("lte", "last location was null, get current location");
                getCurrentLocation();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        Log.i("lte", "asking new location");
        Task<Location> currentLocationTask = fusedLocationProviderClient.getCurrentLocation(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.getToken()
        );
        currentLocationTask.addOnCompleteListener((task -> {
            String result;
            if (task.isSuccessful() && task.getResult() != null) {
                // Task completed successfully
                Location location = task.getResult();
                result = "Location (success): " +
                        location.getLatitude() +
                        ", " +
                        location.getLongitude();
                plantAndPricesViewModel.setPosition(task.getResult());
                Log.d("lte", "getCurrentLocation() result: " + result);
            } else {
                // Task failed with an exception
                Exception exception = task.getException();
                Log.d("lte", "Exception: " + exception);
            }
        }));
    }

    private void doSomethingOnFirstStart() {
        //do the stuff on first start

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancellationTokenSource.cancel();
    }
}