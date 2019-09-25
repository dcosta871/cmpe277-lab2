package com.ben.cmpe277.lab2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class DogWalkerFilterDialog extends DialogFragment {
    private Button cancelButton;
    private Button filterButton;
    private CheckBox smallDogCheckbox;
    private CheckBox mediumDogCheckbox;
    private CheckBox largeDogCheckbox;
    private EditText minDogWalksInput;
    public OnFilterListener onFilterListener;

    static DogWalkerFilterDialog newInstance(boolean smallDogs, boolean mediumDogs, boolean largeDogs, int walkFilter, OnFilterListener filterListener) {
        DogWalkerFilterDialog f = new DogWalkerFilterDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putBoolean("smallDogs", smallDogs);
        args.putBoolean("mediumDogs", mediumDogs);
        args.putBoolean("largeDogs", largeDogs);
        args.putInt("walkFilter", walkFilter);
        args.putSerializable("onFilterListener", filterListener);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_dialog, container, false);
        smallDogCheckbox = view.findViewById(R.id.filterSmallDogsCheckBox);
        mediumDogCheckbox = view.findViewById(R.id.filterMediumDogsCheckBox);
        largeDogCheckbox = view.findViewById(R.id.filterLargeDogsCheckBox);
        minDogWalksInput = view.findViewById(R.id.minDogWalksInput);
        smallDogCheckbox.setChecked(getArguments().getBoolean("smallDogs"));
        mediumDogCheckbox.setChecked(getArguments().getBoolean("mediumDogs"));
        largeDogCheckbox.setChecked(getArguments().getBoolean("largeDogs"));
        minDogWalksInput.setText(String.valueOf(getArguments().getInt("walkFilter")));
        cancelButton = view.findViewById(R.id.cancelFilterButton);
        filterButton = view.findViewById(R.id.filterButton);
        onFilterListener = (OnFilterListener) getArguments().getSerializable("onFilterListener");
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minDogsWalked = Integer.parseInt(minDogWalksInput.getText().toString());
                onFilterListener.onFilter(smallDogCheckbox.isChecked(), mediumDogCheckbox.isChecked(), largeDogCheckbox.isChecked(), minDogsWalked);
                getDialog().dismiss();
            }
        });

        return view;
    }

    public interface OnFilterListener extends Serializable {
        void onFilter(boolean smallDogs, boolean mediumDogs, boolean largeDogs, int walkFilter);
    }
}
