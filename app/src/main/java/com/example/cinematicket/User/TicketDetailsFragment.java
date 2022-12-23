package com.example.cinematicket.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cinematicket.R;


public class TicketDetailsFragment extends Fragment {
TextView title,seat,date,time,cname,cmail,cphone;
String name,seats,dates,times,cnames,cmails,cphones;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ticket_details, container, false);

        title = view.findViewById(R.id.movietitle);
        seat = view.findViewById(R.id.seatttxts);
        date = view.findViewById(R.id.showttxt);
        time = view.findViewById(R.id.datetxt);
        cname = view.findViewById(R.id.custnametxt);
        cmail = view.findViewById(R.id.custemailtxt);
        cphone = view.findViewById(R.id.custphonetxt);

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("mname");
            seats = bundle.getString("seats");
            dates = bundle.getString("date");
            times = bundle.getString("time");
            cnames = bundle.getString("cname");
            cmails = bundle.getString("cmail");
            cphones = bundle.getString("cphone");

            title.setText(name);
            date.setText(dates);
            time.setText(times);
            seat.setText(seats);
            cphone.setText(cphones);
            cmail.setText(cmails);
            cname.setText(cnames);

        }


        return view;
    }
}