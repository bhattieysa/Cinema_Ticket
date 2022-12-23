package com.example.cinematicket.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematicket.Adapter.FavoriteAdapter;
import com.example.cinematicket.Model.MovieModel;
import com.example.cinematicket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment {
    //Variables
    RecyclerView recyclerView;
    FavoriteAdapter adapter;
    TextView noproducts;
    ProgressDialog progressDialog;
    ArrayList<MovieModel> list;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favorites, container, false);
        //init views
        recyclerView = view.findViewById(R.id.favrecmovies);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("PLease Wait....!!");
        progressDialog.setCanceledOnTouchOutside(false);
        noproducts = view.findViewById(R.id.noproducts);
        progressDialog.show();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        database= FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Favorites");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog.show();


        //getting data from Halls DB
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String date = dataSnapshot.child("date").getValue(String.class);
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String summary = dataSnapshot.child("summary").getValue(String.class);
                    String image = dataSnapshot.child("image").getValue(String.class);
                    String genre = dataSnapshot.child("genre").getValue(String.class);
                    String language = dataSnapshot.child("language").getValue(String.class);
                    String first = dataSnapshot.child("firstshow").getValue(String.class);
                    String second = dataSnapshot.child("secondshow").getValue(String.class);
                    String third = dataSnapshot.child("thirdshow").getValue(String.class);



                    MovieModel model = new
                            MovieModel(title,summary,date,image,language,genre,first,second,third,"","");


                    list.add(model);
                }
                if (list.isEmpty()) {
                    progressDialog.dismiss();
                    noproducts.setVisibility(View.VISIBLE);
                } else {
                    //passing list and context into adapter
                    adapter = new FavoriteAdapter(list, getActivity());
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

}