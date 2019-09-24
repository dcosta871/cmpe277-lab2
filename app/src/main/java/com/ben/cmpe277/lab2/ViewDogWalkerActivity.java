package com.ben.cmpe277.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ben.cmpe277.lab2.dogwalker.DogWalker;

public class ViewDogWalkerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dog_walker);
        DogWalker dogWalker = (DogWalker) getIntent().getSerializableExtra("dogwalker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View " + dogWalker.name + " information");

        ((TextView) findViewById(R.id.nameText)).setText(dogWalker.name);
        ((TextView) findViewById(R.id.phoneText)).setText(dogWalker.phoneNumber);
        ((TextView) findViewById(R.id.walkCountText)).setText(String.valueOf(dogWalker.walkCount));
        ((RatingBar) findViewById(R.id.ratingBar)).setRating(dogWalker.rating);

        ((CheckBox) findViewById(R.id.largeDogCheckBox)).setChecked(dogWalker.largeDogs);
        ((CheckBox) findViewById(R.id.mediumDogCheckBox)).setChecked(dogWalker.mediumDogs);
        ((CheckBox) findViewById(R.id.smallDogCheckBox)).setChecked(dogWalker.smallDogs);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.in_left, R.anim.out_right);
        }
    }
}
