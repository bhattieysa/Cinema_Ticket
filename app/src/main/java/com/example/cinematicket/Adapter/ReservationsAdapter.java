package com.example.cinematicket.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematicket.Admin.ReservationDetailsActivity;
import com.example.cinematicket.Model.ReservationModel;
import com.example.cinematicket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.myViewholder> {
    ArrayList<ReservationModel> list;
    AlertDialog.Builder builder;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Context ctx;

    public ReservationsAdapter(ArrayList<ReservationModel> list, AlertDialog.Builder builder, Context ctx) {
        this.list = list;
        this.builder = builder;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_reservation_card,parent,false);
        return new myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewholder holder, @SuppressLint("RecyclerView") int position) {
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
                Intent intent = new Intent(ctx, ReservationDetailsActivity.class);
                intent.putExtra("mname", holder.name.getText().toString());
                intent.putExtra("seats", holder.seat);
                intent.putExtra("time", holder.time.getText().toString());
                intent.putExtra("date", holder.date);
                intent.putExtra("cname", holder.cname);
                intent.putExtra("cmail", holder.cmail);
                intent.putExtra("cphone", holder.cphone);
                ctx.startActivity(intent);

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
    public class myViewholder extends RecyclerView.ViewHolder {
        TextView name,details,time;
        String seat, date,cname,cphone,cmail;
        ImageView imageView;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.moviename);
            details = itemView.findViewById(R.id.ticketdetail);
            time = itemView.findViewById(R.id.tickettime);
            imageView = itemView.findViewById(R.id.delete);
        }
    }
}
