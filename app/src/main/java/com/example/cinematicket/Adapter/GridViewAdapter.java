package com.example.cinematicket.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.cinematicket.Model.SelectedPlaces;
import com.example.cinematicket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private final List<Integer> listOfPlaces;
    private final List<SelectedPlaces> selectedPositions;
    private final List<Integer> selPositions;
    private final Context mContext;
    String date, time, title;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView reserve;
    int selectedIndex,pos;

    public GridViewAdapter(Context mContext,String date, String time,String title,TextView reserve) {
        this.listOfPlaces = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        this.mContext = mContext;
        this.selectedPositions = new ArrayList<>();
        this.selPositions = new ArrayList<>();
        this.time = time;
        this.date = date;
        this.title = title;
        this.reserve = reserve;

    }


    @Override
    public int getCount() {
        return listOfPlaces.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfPlaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Seats").child(title).child(date.replace("/","")).child(time);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Button button;
        if (convertView == null) {
            button = new Button(mContext);
            button.setLayoutParams(new GridView.LayoutParams(100, 100));
            button.setPadding(8,8,8,8);
            button.setText(Integer.toString(listOfPlaces.get(position)));
            databaseReference
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(button.getText().toString()).exists()) {
                                button.setBackgroundResource(R.drawable.et_circle);
                                button.setBackgroundColor(Color.LTGRAY);
                                button.setEnabled(false);

                            } else {
                                button.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
                                button.setEnabled(true);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Internet Error", Toast.LENGTH_LONG).show();

                        };
                    });

            button.setTextColor(Color.WHITE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = Integer.parseInt(((Button) view).getText().toString()) - 1;

                    //allow multiple select
                    int selectedIndex = selPositions.indexOf(pos);
                    if (selectedIndex > -1) {

                        selectedPositions.remove(selectedIndex);
                        selPositions.remove(selectedIndex);
                        Log.d("TAGphos", "onClick: tell"+selPositions.toString()+" "+"aa"+selectedPositions);
                        ((Button) view).setBackgroundColor(Color.rgb(53, 172, 72));
                    } else if (selectedIndex == -1){

                        SelectedPlaces places = new SelectedPlaces(pos);
                        selectedPositions.add(places);
                        selPositions.add(pos);
                        Log.d("TAGphos", "onClick: tell"+selPositions.toString()+" "+"aa"+selectedPositions);
                        ((Button) view).setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.selected));
                    }
                }
            });

        } else {
            button = (Button)convertView;
        }

        return button;
    }

    public List<SelectedPlaces> getSelectedPositions() {
        return selectedPositions;
    }

    public List<Integer> getListOfPlaces(){
        return selPositions;
    }

}
