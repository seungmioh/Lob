package com.example.lob.UI.diet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lob.R;
import com.example.lob.UI.consumption.ConsumptionViewModel;

public class DietFragment extends Fragment {

    private DietViewModel dietViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dietViewModel =
                ViewModelProviders.of(this).get(DietViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diet, container, false);
        final TextView textView = root.findViewById(R.id.text_diet);
        dietViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
