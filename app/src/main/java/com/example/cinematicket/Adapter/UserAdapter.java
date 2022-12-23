package com.example.cinematicket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematicket.Admin.UserDetails;
import com.example.cinematicket.Model.UserModel;
import com.example.cinematicket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
ArrayList<UserModel> list;
    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public UserAdapter(ArrayList<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModel model = list.get(position);
        firebaseDatabase = FirebaseDatabase.getInstance();

        //getting data from model object and create views
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        holder.alphabet = model.getFullName();
        holder.name.setText(model.getFullName());
        char first = holder.alphabet.charAt(0);
        holder.first.setText(String.valueOf(first));
        holder.cardView.setCardBackgroundColor(getRandomColor());
        holder.email = model.getEmail();
        holder.password = model.getPasswrod();
        holder.phone = model.getPhone();



        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserDetails.class);
                intent.putExtra("name",holder.name.getText().toString());
                intent.putExtra("pass",holder.password);
                intent.putExtra("phone",holder.phone);
                intent.putExtra("email",holder.email);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    public void filterlist(ArrayList<UserModel> arrayList) {
        this.list = arrayList;

    }

    public void deleteItem(int position) {

        list.remove(position);
        notifyItemRemoved(position);

        databaseReference.removeValue();

    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name,first;
        String password,email,phone,alphabet;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.lettercard);
            name = itemView.findViewById(R.id.firstletter);
            first= itemView.findViewById(R.id.username);
            imageView= itemView.findViewById(R.id.nxt);

        }
    }
}












