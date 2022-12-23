package com.example.cinematicket.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematicket.Model.MovieModel;
import com.example.cinematicket.Model.ReservationModel;
import com.example.cinematicket.R;
import com.example.cinematicket.User.FavoriteDetailsFragment;
import com.example.cinematicket.User.TicketDetailsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserReserveAdapter extends RecyclerView.Adapter<UserReserveAdapter.MyViewHolder>{
    ArrayList<ReservationModel> list;
    AlertDialog.Builder builder;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public UserReserveAdapter(ArrayList<ReservationModel> list, Context ctx,AlertDialog.Builder builder) {
        this.list = list;
        this.ctx = ctx;
        this.builder = builder;

    }

    Context ctx;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_reservation_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        ReservationModel model = list.get(position);
        holder.name.setText(model.getMoviename());
        holder.time.setText(model.getTime());
        holder.seat = model.getSeat();
        holder.date = model.getMoviedate();
        holder.cname = model.getCustname();
        holder.cmail = model.getCustemail();
        holder.cphone = model.getCustphone();

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference("UserReservations").child(firebaseUser.getUid()).child(holder.name.getText().toString());
                builder.setMessage("Are You Sure To Delete This Reservation?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                databaseReference.removeValue();
                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();

                builder.show();

            }
        });

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("mname", holder.name.getText().toString());
                bundle.putString("seats", holder.seat);
                bundle.putString("time", holder.time.getText().toString());
                bundle.putString("date", holder.date);
                bundle.putString("cname", holder.cname);
                bundle.putString("cmail", holder.cmail);
                bundle.putString("cphone", holder.cphone);


                Fragment fragment = new TicketDetailsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction =((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.hfragsa,fragment);
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
    public void filterlist(ArrayList<ReservationModel> arrayList) {
        this.list = arrayList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,details,time;
        String seat, date,cname,cphone,cmail;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.moviename);
            details = itemView.findViewById(R.id.ticketdetail);
            time = itemView.findViewById(R.id.tickettime);
            imageView = itemView.findViewById(R.id.delete);

        }
    }
}
