package com.ben.cmpe277.lab2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ben.cmpe277.lab2.dogwalker.DogWalker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DogWalkerAdapter extends RecyclerView.Adapter<DogWalkerAdapter.MyViewHolder> {
    private ArrayList<DogWalker> dogwalkers;
    private DogWalkerListListener dogWalkerListListener;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView personNameTextView;
        public TextView walkCountTextView;
        public FloatingActionButton removeWalkerButton;
        public FloatingActionButton editWalkerButton;
        public MyViewHolder(View view) {
            super(view);
            personNameTextView = view.findViewById(R.id.person_name);
            walkCountTextView = view.findViewById(R.id.walk_count);
            removeWalkerButton = view.findViewById(R.id.remove_dog_walker);
            editWalkerButton = view.findViewById(R.id.edit_dog_walker);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DogWalkerAdapter(ArrayList<DogWalker> dogwalkers, DogWalkerListListener dogWalkerListListener) {
        this.dogwalkers = dogwalkers;
        this.dogWalkerListListener = dogWalkerListListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DogWalkerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dog_walker_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.personNameTextView.setText(this.dogwalkers.get(position).name);
        holder.walkCountTextView.setText(String.valueOf(this.dogwalkers.get(position).walkCount) + " dogs walked");
        if (position % 2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("white"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogWalkerListListener.dogWalkerSelected(dogwalkers.get(position));
            }
        });
        holder.removeWalkerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dogWalkerListListener.removeDogWalkerSelected(dogwalkers.get(position));
            }
        });
        holder.editWalkerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dogWalkerListListener.editDogWalkerSelected(dogwalkers.get(position));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.dogwalkers.size();
    }

    public void setDogWalkers(ArrayList<DogWalker> dogWalkers) {
        this.dogwalkers = dogWalkers;
    }

    public interface DogWalkerListListener {
        void removeDogWalkerSelected(DogWalker dogWalker);
        void dogWalkerSelected(DogWalker dogWalker);
        void editDogWalkerSelected(DogWalker dogWalker);
    }
}
