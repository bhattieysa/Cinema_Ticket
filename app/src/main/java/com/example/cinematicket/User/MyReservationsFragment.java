package com.example.cinematicket.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematicket.Adapter.UserReserveAdapter;
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


public class MyReservationsFragment extends Fragment {
    //Variables
    RecyclerView recyclerView;
    UserReserveAdapter adapter;
    TextView noproducts;
    ProgressDialog progressDialog;
    ArrayList<ReservationModel> list;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    AlertDialog.Builder builder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_reservations, container, false);

        //init views
        recyclerView = view.findViewById(R.id.resrecv);
        progressDialog = new ProgressDialog(getActivity());
        builder = new AlertDialog.Builder(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("PLease Wait....!!");
        progressDialog.setCanceledOnTouchOutside(false);
        noproducts = view.findViewById(R.id.nopresults);
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("UserReservations");


        progressDialog.show();

        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
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
                    adapter = new UserReserveAdapter(list, getActivity(),builder);
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });





        return view;
    }
    //inflating menu in action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu,menu);

        //adding searchView in menu
        MenuItem menuItem = menu.findItem(R.id.searchview);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.
                getActionView();
        searchView.setQueryHint("Search By Name");
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    //search query or keyword and passing it into adapter
    private void processsearch(String query) {

        ArrayList<ReservationModel> arrayList = new ArrayList<>();
        for(ReservationModel s : list){
            if (s.getMoviename().toLowerCase().contains(query.toLowerCase())){
                arrayList.add(s);
            }

        }
        UserReserveAdapter adapter = new UserReserveAdapter(arrayList,getActivity(),builder);
        adapter.filterlist(arrayList);
        recyclerView.setAdapter(adapter);
        Log.d("erry", "onTextChanged: "+arrayList);



    }
}