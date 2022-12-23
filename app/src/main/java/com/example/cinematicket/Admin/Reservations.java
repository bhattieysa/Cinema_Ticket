package com.example.cinematicket.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematicket.Adapter.ReservationsAdapter;
import com.example.cinematicket.Model.ReservationModel;
import com.example.cinematicket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Reservations extends AppCompatActivity {
    RecyclerView recyclerView;
    ReservationsAdapter adapter;
    TextView noproducts;
    ProgressDialog progressDialog;
    ArrayList<ReservationModel> list;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        recyclerView = findViewById(R.id.reservationsss);
        progressDialog = new ProgressDialog(Reservations.this);
        builder = new AlertDialog.Builder(Reservations.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("PLease Wait....!!");
        progressDialog.setCanceledOnTouchOutside(false);
        noproducts = findViewById(R.id.nopresults);
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(Reservations.this));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Reserves");


        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String custemail = dataSnapshot.child("custemail").getValue(String.class);
                    String custname = dataSnapshot.child("custname").getValue(String.class);
                    String custphone = dataSnapshot.child("custphone").getValue(String.class);
                    String moviedate = dataSnapshot.child("moviedate").getValue(String.class);
                    String moviename = dataSnapshot.child("moviename").getValue(String.class);
                    String seat = dataSnapshot.child("seat").getValue(String.class);
                    String time = dataSnapshot.child("time").getValue(String.class);
                    String uid = dataSnapshot.child("uid").getValue(String.class);




                    ReservationModel model = new ReservationModel(uid,moviename,moviedate,
                            custname,custphone,custemail,seat,time);


                    list.add(model);
                }
                if (list.isEmpty()) {
                    progressDialog.dismiss();
                    noproducts.setVisibility(View.VISIBLE);
                } else {
                    //passing list and context into adapter
                    adapter = new ReservationsAdapter(list,builder, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }
    //Inflating the menu in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.searchview);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.
                getActionView();
        searchView.setQueryHint("Search By Movie Name");
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                processsearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                processsearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    //process the search query and show result
    private void processsearch(String query) {

        ArrayList<ReservationModel> arrayList = new ArrayList<>();
        for(ReservationModel s : list){
            if (s.getMoviename().toLowerCase().contains(query.toLowerCase())){
                arrayList.add(s);
            }

        }
        ReservationsAdapter adapter = new ReservationsAdapter(arrayList,builder,getApplicationContext());
        adapter.filterlist(arrayList);
        recyclerView.setAdapter(adapter);
        Log.d("erry", "onTextChanged: "+arrayList);
    }
}