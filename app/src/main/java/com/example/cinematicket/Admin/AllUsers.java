package com.example.cinematicket.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinematicket.Adapter.UserAdapter;
import com.example.cinematicket.Model.UserModel;
import com.example.cinematicket.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsers extends AppCompatActivity {
    RecyclerView recyclerView;
    UserAdapter adapter;
    ConstraintLayout constraintLayout;
    ProgressDialog progressDialog;
    ArrayList<UserModel> list;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    TextView noresults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        //Creating Progress Dialog
        progressDialog = new ProgressDialog(AllUsers.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");

        //Initialize Views
        constraintLayout = findViewById(R.id.constraint22);
        recyclerView = findViewById(R.id.recv);
        noresults = findViewById(R.id.nopresults);

        //setting layout manager to recview
        recyclerView.setLayoutManager(new LinearLayoutManager(AllUsers.this));
        recyclerView.hasFixedSize();

        //getting instaces of database and reference
        database= FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        Toast.makeText(this, "Swipe Left To Delete Any User", Toast.LENGTH_LONG).show();



        //getting all of the users data from FireBase
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String FullName = dataSnapshot.child("fullName").getValue(String.class);
                    String Phone = dataSnapshot.child("phone").getValue(String.class);
                    String Email = dataSnapshot.child("email").getValue(String.class);
                    String Passwrod = dataSnapshot.child("passwrod").getValue(String.class);
                    String Token = dataSnapshot.child("token").getValue(String.class);
                    String Type = dataSnapshot.child("type").getValue(String.class);




                    //data will be set to model object
                    UserModel model = new
                            UserModel(FullName,Email,Passwrod,Phone,Token,Type);


                    //adding all data into list from model
                    list.add(model);
                }
                if (list.isEmpty()) {
                    progressDialog.dismiss();
                    noresults.setVisibility(View.VISIBLE);
                } else {
                    adapter = new UserAdapter(list,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                    ItemTouchHelper helper = new ItemTouchHelper(callback);
                    helper.attachToRecyclerView(recyclerView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });

    }


    //Swipe to delete data from recyclerview
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Snackbar snackbar = Snackbar.make(constraintLayout,"Item Deleted",Snackbar.LENGTH_LONG);
            snackbar.show();
            adapter.deleteItem(viewHolder.getAdapterPosition());

        }
    };



    //Inflating the menu in the action bar
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

        ArrayList<UserModel> arrayList = new ArrayList<>();
        for(UserModel s : list){
            if (s.getFullName().toLowerCase().contains(query.toLowerCase())){
                arrayList.add(s);
            }

        }
        UserAdapter adapter = new UserAdapter(arrayList,getApplicationContext());
        adapter.filterlist(arrayList);
        recyclerView.setAdapter(adapter);
        Log.d("erry", "onTextChanged: "+arrayList);



    }

}