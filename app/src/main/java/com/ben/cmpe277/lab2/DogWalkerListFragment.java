package com.ben.cmpe277.lab2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.cmpe277.lab2.dogwalker.DogWalker;

import java.util.ArrayList;

/**
 * A fragment representing a list of Dog walkers.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DogWalkerListFragment extends Fragment implements DogWalkerAdapter.DogWalkerListListener, DogWalkerRemoveConfirmationDialog.OnDeleteListener  {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private DogWalkerAdapter dogWalkerAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DogWalkerListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DogWalkerListFragment newInstance(int columnCount) {
        DogWalkerListFragment fragment = new DogWalkerListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_walker_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            dogWalkerAdapter = new DogWalkerAdapter(this.getDogWalkers(), this);

            recyclerView.setAdapter(dogWalkerAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DogWalker item);
    }

    public ArrayList<DogWalker> getDogWalkers() {
        DogWalkerDbHelper dbHelper = new DogWalkerDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                DogWalkerContract.DogWalkerEntry.COLUMN_NAME_NAME,
                DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_WALKED,
                DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_PHONE_NUMBER,
                DogWalkerContract.DogWalkerEntry.COLUMN_NAME_RATING,
                DogWalkerContract.DogWalkerEntry.COLUMN_SMALL_DOGS,
                DogWalkerContract.DogWalkerEntry.COLUMN_MEDIUM_DOGS,
                DogWalkerContract.DogWalkerEntry.COLUMN_LARGE_DOGS
        };

        String sortOrder = DogWalkerContract.DogWalkerEntry.COLUMN_NAME_NAME + " DESC";
        Cursor cursor = db.query(
                DogWalkerContract.DogWalkerEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        ArrayList<DogWalker> dogWalkers = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getColumnIndexOrThrow(DogWalkerContract.DogWalkerEntry._ID);
            String name = cursor.getString(6);
            dogWalkers.add(new DogWalker(
                    cursor.getString(1),
                    Integer.valueOf(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getFloat(4),
                    Boolean.parseBoolean(cursor.getString(5)),
                    Boolean.parseBoolean(cursor.getString(6)),
                    Boolean.parseBoolean(cursor.getString(7))
            ));
        }
        cursor.close();

        dbHelper.close();
        return dogWalkers;
    }

    private void removeDogWalker(String phoneNumber) {
        DogWalkerDbHelper dbHelper = new DogWalkerDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_PHONE_NUMBER + " LIKE ?";
        String[] selectionArgs = { phoneNumber };
        int deletedRows = db.delete(DogWalkerContract.DogWalkerEntry.TABLE_NAME, selection
                , selectionArgs);
        dbHelper.close();
        updateDogWalkerList();
    }

    public void updateDogWalkerList() {
        dogWalkerAdapter.setDogWalkers(this.getDogWalkers());
        dogWalkerAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeDogWalkerSelected(DogWalker dogWalker) {
        DogWalkerRemoveConfirmationDialog dialog = DogWalkerRemoveConfirmationDialog.newInstance(dogWalker.phoneNumber, dogWalker.name, this);
        dialog.show(getFragmentManager(), "DogWalkerRemoveConfirmationDialog");
    }

    @Override
    public void editDogWalkerSelected(DogWalker dogWalker) {
        Intent myIntent = new Intent(getActivity(), EditDogWalkerActivity.class);
        myIntent.putExtra("dogwalker", dogWalker);
        getActivity().startActivityForResult(myIntent, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().overridePendingTransition(R.anim.in_right, R.anim.out_left);
        }
    }

    @Override
    public void dogWalkerSelected(DogWalker dogWalker) {
        Intent myIntent = new Intent(getActivity(), ViewDogWalkerActivity.class);
        myIntent.putExtra("dogwalker", dogWalker);
        getActivity().startActivity(myIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().overridePendingTransition(R.anim.in_right, R.anim.out_left);
        }
    }

    @Override
    public void onDeleteDogWalker(String phoneNumber) {
        removeDogWalker(phoneNumber);
    }

}
