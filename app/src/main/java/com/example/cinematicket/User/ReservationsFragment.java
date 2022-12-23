package com.example.cinematicket.User;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.cinematicket.Adapter.GridViewAdapter;
import com.example.cinematicket.Model.SelectedPlaces;
import com.example.cinematicket.R;

import java.util.ArrayList;
import java.util.List;


public class ReservationsFragment extends Fragment {
    private GridViewAdapter adapter;
    String film;
    String image,date,time,genre,lang;
    TextView reserve,title,times;
    ImageView imageView;
    static List<Boolean> cinemaPlaces;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reservations, container, false);


        imageView = view.findViewById(R.id.moviePosterImageView);
        title = view.findViewById(R.id.movieTitleTextView);
        times = view.findViewById(R.id.movieHourTextView);
        //retrieve values from Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            film = bundle.getString("title");
            image = bundle.getString("image");
            time = bundle.getString("time");
            date = bundle.getString("date");
            lang = bundle.getString("lang");
            genre = bundle.getString("genre");
            times.setText(time);

            Log.d("TAGgeek", "onCreateView: " + film + " " + date + " " + time);


            Glide.with(getActivity()).load(image).placeholder(R.drawable.camera).into(imageView);
            title.setText(film);

            GridView gridView = (GridView) view.findViewById(R.id.gridPlaces);
            adapter = new GridViewAdapter(getActivity(), date, time, film,reserve);
            gridView.setAdapter(adapter);

            //Attach swipe listener to SwipeButton
            reserve = view.findViewById(R.id.reserve_btn);
//            if (adapter.getSelectedPositions().size() > 0) {
//                reserve.setEnabled(true);
//            } else {
//                //swipeButton shouldnt expand
//                reserve.setEnabled(false);
//            }
            reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<SelectedPlaces> selected =
                            new ArrayList<>();
                    selected = adapter.getSelectedPositions();
                    int pos = adapter.getSelectedPositions().size();
                    List<Integer> list = new ArrayList<>();
                    list = adapter.getListOfPlaces();
                    Log.d("TAGindias", "onClick: "+pos+selected);
                    List<Integer> seats = new ArrayList<>();
                    for (int i=0; i<list.size(); i++){
                        seats.add(list.get(i)+1);
                        Log.d("seatsTAG", "onClick: "+seats);
                    }


                    Bundle bundle = new Bundle();
                    bundle.putString("title", film);
                    bundle.putString("image", image);
                    bundle.putString("time", times.getText().toString());
                    bundle.putString("date", date);
                    bundle.putParcelableArrayList("seat", (ArrayList<? extends Parcelable>) selected);
                    bundle.putString("seatcount", String.valueOf(seats));
                    bundle.putString("genre", genre);
                    bundle.putString("lang", lang);

                    Fragment fragment = new ConfirmBookingFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.hfrag,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
            });
        }
        return view;

    }

}