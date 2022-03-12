package dia.units.refuelapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refuelapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import dia.units.refuelapp.data.PlantAndPricesViewModel;
import dia.units.refuelapp.data.RefuelItemViewModel;
import dia.units.refuelapp.model.RefuelItemWithPlantInfo;
import dia.units.refuelapp.ui.RefuelItemsAdapter;

public class AccountFragment extends Fragment {
    private Button btnSignIn;
    private Button btnLogout;
    private RefuelItemViewModel refuelItemViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        btnSignIn = view.findViewById(R.id.btn_account_signin);
        btnLogout = view.findViewById(R.id.btn_account_logout);
        RecyclerView recyclerViewRefuelItems = view.findViewById(R.id.recycler_view_refuels_list);
        final RefuelItemsAdapter refuelItemsAdapter = new RefuelItemsAdapter();
        recyclerViewRefuelItems.setAdapter(refuelItemsAdapter);
        recyclerViewRefuelItems.setLayoutManager(new LinearLayoutManager(getContext()));
        refuelItemViewModel = new ViewModelProvider(requireActivity()).get(RefuelItemViewModel.class);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            userIsLogged();
        else
            userNotLogged();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null)
                userIsLogged();
            else
                userNotLogged();
        });
        btnSignIn.setOnClickListener(view1 -> startActivity(new Intent(getActivity().getApplication(), LoginActivity.class)));
        btnLogout.setOnClickListener(view2 -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(), "User logout successfully", Toast.LENGTH_SHORT).show();
        });

        refuelItemViewModel.refuelItems.observe(getViewLifecycleOwner(), refuelItemsAdapter::submitList);

        FloatingActionButton btn_add_refuels = view.findViewById(R.id.btn_add_refuel_account_item);
        btn_add_refuels.setOnClickListener(view12 -> {
            NavController navController = NavHostFragment.findNavController(getParentFragment());
            navController.navigate(R.id.action_accountFragment_to_addRefuelFragment);
        });

        return view;
    }

    private void userIsLogged() {
        btnSignIn.setVisibility(View.GONE);
        btnLogout.setVisibility(View.VISIBLE);
    }

    private void userNotLogged() {
        btnLogout.setVisibility(View.GONE);
        btnSignIn.setVisibility(View.VISIBLE);
    }
}
