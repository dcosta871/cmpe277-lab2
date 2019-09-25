package com.ben.cmpe277.lab2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ben.cmpe277.lab2.dogwalker.DogWalker;


public class EditDogWalkerActivity extends AppCompatActivity {

    private Button addButton;
    private Button cancelButton;
    private EditText nameInput;
    private EditText phoneNumberInput;
    private EditText walkCountInputText;
    private RatingBar ratingBar;
    private CheckBox smallDogCheckBox;
    private CheckBox mediumDogCheckBox;
    private CheckBox largeDogCheckBox;
    private String originalPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dog_walker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DogWalker dogWalker = (DogWalker) getIntent().getSerializableExtra("dogwalker");
        cancelButton = findViewById(R.id.cancelEditDogWalkerButton);
        addButton = findViewById(R.id.editEditDogWalkerButton);
        nameInput = findViewById(R.id.editNameInputText);
        phoneNumberInput = findViewById(R.id.editPhoneInputText);
        ratingBar = findViewById(R.id.editRatingBar);
        walkCountInputText = findViewById(R.id.editWalkCountInputText);
        smallDogCheckBox = findViewById(R.id.editSmallDogCheckBox);
        mediumDogCheckBox = findViewById(R.id.editMediumDogCheckBox);
        largeDogCheckBox = findViewById(R.id.editLargeDogCheckBox);
        originalPhoneNumber = dogWalker.phoneNumber;

        nameInput.setText(dogWalker.name);

        getSupportActionBar().setTitle("Edit " + dogWalker.name);
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        phoneNumberInput.setText(dogWalker.phoneNumber);
        ratingBar.setRating(dogWalker.rating);
        walkCountInputText.setText(String.valueOf(dogWalker.walkCount));
        smallDogCheckBox.setChecked(dogWalker.smallDogs);
        mediumDogCheckBox.setChecked(dogWalker.mediumDogs);
        largeDogCheckBox.setChecked(dogWalker.largeDogs);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameInput.getText().toString().isEmpty()) {
                    showErrorToast("Name must be entered");
                    return;
                }
                else if (phoneNumberInput.getText().toString().isEmpty()) {
                    showErrorToast("Phone number must be entered");
                    return;
                }
                else if (walkCountInputText.getText().toString().isEmpty()) {
                    showErrorToast("Walk count must be entered");
                }


                DogWalkerDbHelper dbHelper = new DogWalkerDbHelper(view.getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String newStr = phoneNumberInput.getText().toString();
                if (!originalPhoneNumber.equals(phoneNumberInput.getText().toString()) && phoneNumberExists(phoneNumberInput.getText().toString(), db)) {
                    showErrorToast("Phone number already exists");
                    return;
                }

                String selection = DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_PHONE_NUMBER + " LIKE ?";
                String[] selectionArgs = { originalPhoneNumber };
                ContentValues values = new ContentValues();
                values.put(DogWalkerContract.DogWalkerEntry.COLUMN_NAME_NAME, nameInput.getText().toString());
                values.put(DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_WALKED, Integer.parseInt(walkCountInputText.getText().toString()));
                values.put(DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_PHONE_NUMBER, phoneNumberInput.getText().toString());
                values.put(DogWalkerContract.DogWalkerEntry.COLUMN_NAME_RATING, ratingBar.getRating());
                values.put(DogWalkerContract.DogWalkerEntry.COLUMN_SMALL_DOGS, String.valueOf(smallDogCheckBox.isChecked()));
                values.put(DogWalkerContract.DogWalkerEntry.COLUMN_MEDIUM_DOGS, String.valueOf(mediumDogCheckBox.isChecked()));
                values.put(DogWalkerContract.DogWalkerEntry.COLUMN_LARGE_DOGS, String.valueOf(largeDogCheckBox.isChecked()));
                long newRowId = db.update(DogWalkerContract.DogWalkerEntry.TABLE_NAME, values, selection, selectionArgs);

                dbHelper.close();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
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

    public void showErrorToast(String error) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_custom_layout));
        TextView toastText = (TextView) layout.findViewById(R.id.toast_text);
        toastText.setText(error);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public boolean phoneNumberExists(String phoneNumber, SQLiteDatabase db) {
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
        String selection = DogWalkerContract.DogWalkerEntry.COLUMN_NAME_DOGS_PHONE_NUMBER + " LIKE ?";
        String[] selectionArgs = { phoneNumber };
        Cursor cursor = db.query(
                DogWalkerContract.DogWalkerEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        boolean phoneNumberExists = false;
        if (cursor.getCount() > 0) phoneNumberExists = true;

        cursor.close();
        return phoneNumberExists;
    }
}
