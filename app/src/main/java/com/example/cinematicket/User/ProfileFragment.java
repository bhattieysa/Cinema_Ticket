package com.example.cinematicket.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cinematicket.Admin.AdminHome;
import com.example.cinematicket.Login;
import com.example.cinematicket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    EditText name, phone, pass;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    TextView update,email,logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        progressDialog = new ProgressDialog(getActivity());

        //creating progress dialog
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait.....");
        progressDialog.setCanceledOnTouchOutside(false);

        //fiirebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");


        name = view.findViewById(R.id.name_textview);
        email = view.findViewById(R.id.mail_textview);
        phone = view.findViewById(R.id.phonetxt);
        pass = view.findViewById(R.id.passtxt);
        update = view.findViewById(R.id.update_btn);
        logout = view.findViewById(R.id.logout_btn);



        progressDialog.show();

        //running a query to get the current user data
        Query query = mDatabase.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String names = ""+ ds.child("fullName").getValue();
                    String emails = ""+ ds.child("email").getValue();
                    String passwrod = ""+ ds.child("passwrod").getValue();
                    String phones = ""+ ds.child("phone").getValue();

                    //setting the values into views
                    name.setText(names);
                    pass.setText(passwrod);
                    phone.setText(phones);
                    email.setText(emails);
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //updating the fields of user in ddatabase
                mDatabase.child(firebaseUser.getUid()).child("fullName").setValue(name.getText().toString());
                mDatabase.child(firebaseUser.getUid()).child("passwrod").setValue(pass.getText().toString());
                mDatabase.child(firebaseUser.getUid()).child("phone").setValue(phone.getText().toString());


                Toast.makeText(getActivity(), "updated", Toast.LENGTH_SHORT).show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    firebaseAuth.signOut();
                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "You aren't login Yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}