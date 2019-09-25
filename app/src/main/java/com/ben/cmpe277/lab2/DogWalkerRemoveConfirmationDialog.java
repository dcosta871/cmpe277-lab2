package com.ben.cmpe277.lab2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class DogWalkerRemoveConfirmationDialog extends DialogFragment {
    private Button cancelButton;
    private Button removeButton;
    private TextView removeConfirmationMessage;
    public OnDeleteListener onDeleteListener;

    static DogWalkerRemoveConfirmationDialog newInstance(String phoneNumber, String name, OnDeleteListener deleteListener) {
        DogWalkerRemoveConfirmationDialog f = new DogWalkerRemoveConfirmationDialog();

        Bundle args = new Bundle();
        args.putString("phoneNumber", phoneNumber);
        args.putString("name", name);
        args.putSerializable("onDeleteListener", deleteListener);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remove_confirmation_dialog, container, false);
        cancelButton = view.findViewById(R.id.removeConfirmationCancelButton);
        removeButton = view.findViewById(R.id.removeConfirmationRemoveButton);
        removeConfirmationMessage = view.findViewById(R.id.deleteConfirmationMessage);
        removeConfirmationMessage.setText("Press \"Remove\" to delete " + getArguments().getString("name") + " from dog walker list");
        onDeleteListener = (OnDeleteListener) getArguments().getSerializable("onDeleteListener");
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteListener.onDeleteDogWalker(getArguments().getString("phoneNumber"));
                getDialog().dismiss();
            }
        });

        return view;
    }

    public interface OnDeleteListener extends Serializable {
        void onDeleteDogWalker(String phoneNumber);
    }
}
