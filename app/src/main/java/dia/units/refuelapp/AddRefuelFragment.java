package dia.units.refuelapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.refuelapp.R;

public class AddRefuelFragment extends Fragment {

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_refuels_item_fragment, container, false);
        return view;
    }
}
