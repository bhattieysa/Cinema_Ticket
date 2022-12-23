package com.example.cinematicket.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinematicket.Model.MovieModel;
import com.example.cinematicket.R;
import com.example.cinematicket.User.DetailsFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    ArrayList<MovieModel> list;
    Context ctx;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    public MoviesAdapter(ArrayList<MovieModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card,parent,false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MyViewHolder holder, int position) {
        //showing data into views of the row layout
        MovieModel model = list.get(position);
        holder.language.setText(model.getLanguage());


        holder.summary = model.getSummary();
        holder.genre = model.getGenre();
        holder.title = model.getTitle();
        holder.date = model.getDate();
        holder.image = model.getImage();
        Glide.with(ctx).load(holder.image).placeholder(R.drawable.camera).into(holder.imageView);

//        //Sending Data on Next Activity through Intent
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("language", holder.language.getText().toString());
                bundle.putString("summary", holder.summary);
                bundle.putString("genre", holder.genre);
                bundle.putString("title", holder.title);
                bundle.putString("image", holder.image);
                bundle.putString("date", holder.date);
                bundle.putString("first", model.getFirstshow());
                bundle.putString("second", model.getSecondshow());
                bundle.putString("third", model.getThirdshow());

                Fragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction =((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.hfrag,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //is used to insert data against search query into list
    public void filterlist(ArrayList<MovieModel> arrayList) {
        this.list = arrayList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView language;
        CardView cardView;
        String date,summary,image,genre,title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_image);
            language = itemView.findViewById(R.id.languages);
            cardView = itemView.findViewById(R.id.clickable);
        }
    }
}
