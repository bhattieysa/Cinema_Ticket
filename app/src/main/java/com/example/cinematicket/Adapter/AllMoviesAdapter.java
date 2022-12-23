package com.example.cinematicket.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.cinematicket.Admin.EditMovieActivity;
import com.example.cinematicket.Model.MovieModel;
import com.example.cinematicket.R;
import com.example.cinematicket.User.DetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.MyViewHolder>{
    ArrayList<MovieModel> list;
    Context ctx;
    AlertDialog.Builder builder;
    StorageReference photoRef;
    Query applesQuery;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String id;

    public AllMoviesAdapter(ArrayList<MovieModel> list, Context ctx,String id,AlertDialog.Builder builder) {
        this.list = list;
        this.ctx = ctx;
        this.id = id;
        this.builder = builder;
    }

    @NonNull
    @Override
    public AllMoviesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMoviesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        firebaseDatabase = FirebaseDatabase.getInstance();

        //showing data into views of the row layout
        MovieModel model = list.get(position);
        holder.language.setText(model.getLanguage());


        holder.summary = model.getSummary();
        holder.genre = model.getGenre();
        holder.title = model.getTitle();
        holder.date = model.getDate();
        holder.id = model.getId();
        holder.image = model.getImage();
        Glide.with(ctx).load(holder.image).placeholder(R.drawable.camera).into(holder.imageView);


        //Sending Data on Next Activity through Intent
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, EditMovieActivity.class);
                intent.putExtra("language", holder.language.getText().toString());
                intent.putExtra("summary", holder.summary);
                intent.putExtra("genre", holder.genre);
                intent.putExtra("title", holder.title);
                intent.putExtra("image", holder.image);
                intent.putExtra("date", holder.date);
                intent.putExtra("first", model.getFirstshow());
                intent.putExtra("second", model.getSecondshow());
                intent.putExtra("third", model.getThirdshow());
                intent.putExtra("id", model.getId());
                intent.putExtra("imgpath", model.getImgpath());
                ctx.startActivity(intent);



            }
        });
        //press and hold image to delete the item
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                databaseReference = firebaseDatabase.getReference("Movies").child(holder.id);
                photoRef = FirebaseStorage.getInstance().getReferenceFromUrl( holder.image);

                //creating a dialog to confirm
                        // set message, title, and icon

                        builder.setMessage("Are You Sure To Delete This Movie?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                photoRef.delete();
                                databaseReference.removeValue();
                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();

                builder.show();
                return false;
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
        String date,summary,image,genre,title,id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_image);
            language = itemView.findViewById(R.id.languages);
            cardView = itemView.findViewById(R.id.clickable);
        }
    }
}
