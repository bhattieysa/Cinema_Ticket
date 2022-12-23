package com.example.cinematicket.User;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cinematicket.Model.SelectedPlaces;
import com.example.cinematicket.R;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

public class MakeReservationFragment extends Fragment {
EditText name,phone,email;
TextView next;
String nameString,phoneString,emailString,title,image,time,date,lang,genre,seatcount;
List<SelectedPlaces> list;
List<Integer> seatlist;
CountryCodePicker ccp;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_reservation, container, false);

        name = view.findViewById(R.id.etxtname);
        phone = view.findViewById(R.id.phoneedt);
        email = view.findViewById(R.id.emailedt);
        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phone);
        next = view.findViewById(R.id.nextt_btn);


        list = new ArrayList<>();
        seatlist = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            image = bundle.getString("image");
            time = bundle.getString("time");
            date = bundle.getString("date");
            list = bundle.getParcelableArrayList("seat");
            lang = bundle.getString("lang");
            genre = bundle.getString("genre");
            seatcount = bundle.getString("seatcount");

        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("seatsTAG", "onClick: "+seatcount);
                nameString = name.getText().toString();
                emailString = email.getText().toString();
                phoneString = ccp.getFullNumberWithPlus().replace(" ", "");

                if (TextUtils.isEmpty(nameString)){
                    name.setError("Name is Required.");
                    return;
                }

                if (TextUtils.isEmpty(emailString)){
                    email.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(phoneString)){
                    phone.setError("Phone is Required.");
                    return;
                }

                Log.d("TAGphone", "onClick: "+phoneString);
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("time", time);
                bundle.putString("date", date);
                bundle.putParcelableArrayList("seat", (ArrayList<? extends Parcelable>) list);
                bundle.putString("phone", phoneString);
                bundle.putString("name", nameString);
                bundle.putString("email", emailString);
                bundle.putString("genre", genre);
                bundle.putString("lang", lang);
                bundle.putString("seatcount", seatcount);

                Fragment fragment = new ConfirmBookingFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction =getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.hfrag,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }


}