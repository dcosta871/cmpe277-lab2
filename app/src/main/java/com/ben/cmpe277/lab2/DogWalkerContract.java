package com.ben.cmpe277.lab2;

import android.provider.BaseColumns;

public final class DogWalkerContract {
    private DogWalkerContract() {}

    /* Inner class that defines the table contents */
    public static class DogWalkerEntry implements BaseColumns {
        public static final String TABLE_NAME = "dogwalkers";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DOGS_WALKED = "dogswalked";
        public static final String COLUMN_NAME_DOGS_PHONE_NUMBER = "phonenumber";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_SMALL_DOGS = "smalldogs";
        public static final String COLUMN_MEDIUM_DOGS = "mediumdogs";
        public static final String COLUMN_LARGE_DOGS = "largedogs";
    }
}
