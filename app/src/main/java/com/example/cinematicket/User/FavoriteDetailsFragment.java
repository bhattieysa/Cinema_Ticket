package com.example.cinematicket.User;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cinematicket.Model.MovieModel;
import com.example.cinematicket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoriteDetailsFragment extends Fragment {
    ImageView imageView;
    TextView name,summary,genres,language,date,reserve;
    String title,desc,genre,lang,rdate,image,first,second,third;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_details, container, false);
        imageView = view.findViewById(R.id.image_movie);
        name = view.findViewById(R.id.tvtitle);
        summary = view.findViewById(R.id.tvdescr);
        genres = view.findViewById(R.id.text_view_genre);
        language = view.findViewById(R.id.text_view_language);
        date = view.findViewById(R.id.text_viewreleased);
        reserve = view.findViewById(R.id.reserve_btn);



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Favorites");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            desc = bundle.getString("summary");
            genre = bundle.getString("genre");
            lang = bundle.getString("language");
            rdate = bundle.getString("date");
            image = bundle.getString("image");
            first = bundle.getString("first");
            second = bundle.getString("second");
            third = bundle.getString("third");




            name.setText(title);
            summary.setText(desc);
            genres.setText(genre);
            language.setText(lang);
            date.setText(rdate);
            Glide.with(getActivity()).load(image).placeholder(R.drawable.camera).into(imageView);

        }



        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("image", image);
                bundle.putString("first", first);
                bundle.putString("second", second);
                bundle.putString("third", third);
                bundle.putString("genre", genre);
                bundle.putString("lang", lang);

                Fragment fragment = new DatetimeFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction =getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.hfragment,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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