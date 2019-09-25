package com.ben.cmpe277.lab2;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.ben.cmpe277.lab2.dogwalker.DogWalker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.widget.EditText;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements DogWalkerListFragment.OnListFragmentInteractionListener, SearchView.OnQueryTextListener, DogWalkerFilterDialog.OnFilterListener {

    private String lastSearch = "";
    private boolean largeDogFilter = false;
    private boolean mediumDogFilter = false;
    private boolean smallDogFilter = false;
    private int minDogsWalked = 0;
    private boolean filterEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewDogWalkerActivity();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */

            }
        });
    }

    private void openNewDogWalkerActivity() {
        Intent myIntent = new Intent(this, AddDogWalkerActivity.class);
        startActivityForResult(myIntent, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.in_right, R.anim.out_left);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            DogWalkerFilterDialog dialog = DogWalkerFilterDialog.newInstance(smallDogFilter, mediumDogFilter, largeDogFilter, minDogsWalked, this);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(getSupportFragmentManager(), "DogWalkerFilterDialog");
            return true;
        }
        else if (id == R.id.action_clear_filter) {
            filterEnabled = false;
            onQueryTextChange(lastSearch);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                DogWalkerListFragment dogWalkerListFragment = (DogWalkerListFragment) getSupportFragmentManager().findFragmentById(R.id.dog_walker_fragment);
                dogWalkerListFragment.updateDogWalkerList();
                onQueryTextChange(lastSearch);
            }
        }
    }

    @Override
    public boolean onQueryTextChange(String inputtedText) {
        DogWalkerListFragment dogWalkerListFragment = (DogWalkerListFragment) getSupportFragmentManager().findFragmentById(R.id.dog_walker_fragment);
        dogWalkerListFragment.onSearchChanged(inputtedText, filterEnabled, smallDogFilter, mediumDogFilter, largeDogFilter, minDogsWalked);
        this.lastSearch = inputtedText;
        return false;
    }

    @Override
    public void onFilter(boolean smallDogs, boolean mediumDogs, boolean largeDogs, int walkFilter) {
        this.smallDogFilter = smallDogs;
        this.mediumDogFilter = mediumDogs;
        this.largeDogFilter = largeDogs;
        this.minDogsWalked = walkFilter;
        this.filterEnabled = true;

        onQueryTextChange(lastSearch);
    }

    @Override
    public boolean onQueryTextSubmit(String submittedText) {
        return false;
    }

    public void onListFragmentInteraction(DogWalker item) {
        return;
    }
}
