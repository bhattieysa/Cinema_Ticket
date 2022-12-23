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

import com.example.cinematicket.Adapter.AllMoviesAdapter;
import com.example.cinematicket.Adapter.MoviesAdapter;
import com.example.cinematicket.Model.MovieModel;
import com.example.cinematicket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Movies extends AppCompatActivity {
    //Variables
    RecyclerView recyclerView;
    AllMoviesAdapter adapter;
    TextView noproducts;
    ProgressDialog progressDialog;
    ArrayList<MovieModel> list;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    AlertDialog.Builder builder;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        //init views
        recyclerView = findViewById(R.id.allrecmovies);
        progressDialog = new ProgressDialog(Movies.this);
        builder = new AlertDialog.Builder(Movies.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("PLease Wait....!!");
        progressDialog.setCanceledOnTouchOutside(false);
        noproducts = findViewById(R.id.noproducts);
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(Movies.this));
        database=FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Movies");


        progressDialog.show();


        //getting data from Halls DB
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                    id = dataSnapshot.child("id").getValue(String.class);
                    String imgpath = dataSnapshot.child("imgpath").getValue(String.class);



                    MovieModel model = new
                            MovieModel(title,summary,date,image,language,genre,first,second,third,id,imgpath);


                    list.add(model);
                }
                if (list.isEmpty()) {
                    progressDialog.dismiss();
                    noproducts.setVisibility(View.VISIBLE);
                } else {
                    //passing list and context into adapter
                    adapter = new AllMoviesAdapter(list, getApplicationContext(),id,builder);
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
    //inflating menu in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.searchview);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.
                getActionView();
        searchView.setQueryHint("Search By User Name");
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

        ArrayList<MovieModel> arrayList = new ArrayList<>();
        for(MovieModel s : list){
            if (s.getTitle().toLowerCase().contains(query.toLowerCase())){
                arrayList.add(s);
            }

        }
        MoviesAdapter adapter = new MoviesAdapter(arrayList,getApplicationContext());
        adapter.filterlist(arrayList);
        recyclerView.setAdapter(adapter);
        Log.d("erry", "onTextChanged: "+arrayList);



    }
}