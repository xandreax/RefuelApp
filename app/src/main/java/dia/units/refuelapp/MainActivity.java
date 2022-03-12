package dia.units.refuelapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.refuelapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import dia.units.refuelapp.data.PlantAndPricesViewModel;
import dia.units.refuelapp.data.RepositoryCallback;
import dia.units.refuelapp.worker.DownloadDataWorker;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CODE = 1234;
    private PlantAndPricesViewModel plantAndPricesViewModel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
    private NavController navController;
    private TextView tUsernameDrawer;
    private Button btnSignIn;
    private Button btnLogout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splashScreen.setKeepOnScreenCondition(() -> !isFirstStartStuffDone());

        //set firebase
        mAuth = FirebaseAuth.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //get location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled()) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Please turn on your location", Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        //define navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.gasolinePlantsListFragment, R.id.reportFragment,
                        R.id.favoritesFragment, R.id.accountFragment).setDrawerLayout(drawerLayout).build();
        Toolbar toolbar = findViewById(R.id.toolbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationView drawer_view = findViewById(R.id.drawer_view);

        View header = drawer_view.getHeaderView(0);
        tUsernameDrawer = header.findViewById(R.id.username_drawer);
        btnSignIn = header.findViewById(R.id.btn_drawer_sign_in);
        btnLogout = header.findViewById(R.id.btn_drawer_logout);
        if (mAuth.getCurrentUser() != null) {
            displayUserInfoDrawerMenu();
        } else {
            displayLogoutDrawerMenu();
        }
        mAuth.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null)
                displayUserInfoDrawerMenu();
            else
                displayLogoutDrawerMenu();
        });

        btnSignIn.setOnClickListener(view1 -> startActivity(new Intent(this, LoginActivity.class)));

        btnLogout.setOnClickListener(view2 -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "User logout successfully", Toast.LENGTH_SHORT).show();
        });

        NavigationUI.setupWithNavController(drawer_view, navController);
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        //hide bottom navigation in detail fragment
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                TransitionManager.beginDelayedTransition(findViewById(R.id.nav_host_fragment), new Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment, true));
                if (f instanceof DetailGasolinePlantFragment || f instanceof AddRefuelFragment)
                    bottomNav.setVisibility(View.GONE);
                else
                    bottomNav.setVisibility(View.VISIBLE);
            }
        }, true);
    }

    private void displayUserInfoDrawerMenu() {
        tUsernameDrawer.setVisibility(View.VISIBLE);
        tUsernameDrawer.setText(mAuth.getCurrentUser().getEmail());
        btnSignIn.setVisibility(View.GONE);
        btnLogout.setVisibility(View.VISIBLE);
    }

    private void displayLogoutDrawerMenu() {
        tUsernameDrawer.setVisibility(View.GONE);
        btnSignIn.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
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
                if (isNetworkAvailable()) {
                    getCurrentLocation();
                } else {
                    Toast.makeText(this, "You are currently offline, please turn on your internet connection", Toast.LENGTH_LONG).show();
                }
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

    private boolean isFirstStartStuffDone() {
        plantAndPricesViewModel = new ViewModelProvider(this).get(PlantAndPricesViewModel.class);
        plantAndPricesViewModel.isDbEmpty(new RepositoryCallback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                if (result) {
                    Toast.makeText(getApplicationContext(), "Db is empty", Toast.LENGTH_SHORT).show();
                    if (isNetworkAvailable()) {
                        PeriodicWorkRequest updateDataRequest = new PeriodicWorkRequest.Builder(DownloadDataWorker.class, 1, TimeUnit.DAYS)
                                .setConstraints(new Constraints.Builder()
                                        .setRequiredNetworkType(NetworkType.CONNECTED).build())
                                //.setInitialDelay(12, TimeUnit.HOURS)
                                .build();
                        WorkManager.getInstance(getApplicationContext()).enqueue(updateDataRequest);
                        //plantAndPricesViewModel.makeUpdateDataRequest();
                    } else {
                        Toast.makeText(getApplicationContext(), "You are currently offline, please turn on your internet connection and restart the application", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Db is not empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "something gone wrong, impossible communicate with database", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancellationTokenSource.cancel();
    }
}