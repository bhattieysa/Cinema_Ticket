package com.example.cinematicket.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinematicket.R;

public class ReservationDetailsActivity extends AppCompatActivity {
    TextView title,seat,date,time,cname,cmail,cphone;
    String name,seats,dates,times,cnames,cmails,cphones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);
        title =findViewById(R.id.movietitle);
        seat =findViewById(R.id.seatttxts);
        date =findViewById(R.id.showttxt);
        time =findViewById(R.id.datetxt);
        cname =findViewById(R.id.custnametxt);
        cmail =findViewById(R.id.custemailtxt);
        cphone =findViewById(R.id.custphonetxt);

        Intent intent = getIntent();
        name = intent.getStringExtra("mname");
        seats = intent.getStringExtra("seats");
        dates = intent.getStringExtra("date");
        times = intent.getStringExtra("time");
        cnames = intent.getStringExtra("cname");
        cmails = intent.getStringExtra("cmail");
        cphones = intent.getStringExtra("cphone");

        Log.d("cphones", "onCreate: "+"phone: "+cphones);

        title.setText(name);
        date.setText(dates);
        time.setText(times);
        seat.setText(seats);
        cphone.setText(cphones);
        cmail.setText(cmails);
        cname.setText(cnames);

    }
}