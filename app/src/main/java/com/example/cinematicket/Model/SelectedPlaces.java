package com.example.cinematicket.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectedPlaces implements Parcelable {
    int places;

    protected SelectedPlaces(Parcel in) {
        places = in.readInt();
    }

    public static final Creator<SelectedPlaces> CREATOR = new Creator<SelectedPlaces>() {
        @Override
        public SelectedPlaces createFromParcel(Parcel in) {
            return new SelectedPlaces(in);
        }

        @Override
        public SelectedPlaces[] newArray(int size) {
            return new SelectedPlaces[size];
        }
    };

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public SelectedPlaces(int places) {
        this.places = places;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(places);
    }
}
