package com.ben.cmpe277.lab2.dogwalker;

import java.io.Serializable;

public class DogWalker implements Serializable {
    public final String name;
    public final int walkCount;
    public final String phoneNumber;
    public final float rating;
    public final boolean smallDogs;
    public final boolean mediumDogs;
    public final boolean largeDogs;

    public DogWalker(String name, int walkCount, String phoneNumber, float rating, boolean smallDogs, boolean mediumdogs, boolean largedogs) {
        this.name = name;
        this.walkCount = walkCount;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.smallDogs = smallDogs;
        this.mediumDogs = mediumdogs;
        this.largeDogs = largedogs;
    }

    @Override
    public String toString() {
        return name;
    }
}
