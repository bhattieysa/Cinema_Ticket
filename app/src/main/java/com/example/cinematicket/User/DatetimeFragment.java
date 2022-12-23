package com.example.cinematicket.User;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cinematicket.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DatetimeFragment extends Fragment {
RadioButton first,second,third;
TextView next,Date;
RadioGroup radioGroup;
String firsts,seconds,thirds,title,image,genre,lang;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_datetime, container, false);

        first = view.findViewById(R.id.radio1);
        second = view.findViewById(R.id.radio2);
        third = view.findViewById(R.id.radio3);
        next = view.findViewById(R.id.nxttMove);
        Date = view.findViewById(R.id.moviedate);
        radioGroup = (RadioGroup)view.findViewById(R.id.hour_choices);
        radioGroup.clearCheck();

        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            image = bundle.getString("image");
            firsts = bundle.getString("first");
            seconds = bundle.getString("second");
            thirds = bundle.getString("third");
            genre = bundle.getString("genre");
            lang = bundle.getString("lang");

            first.setText(firsts);
            second.setText(seconds);
            third.setText(thirds);

        }

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    // The flow will come here when
                    // any of the radio buttons in the radioGroup
                    // has been clicked

                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton)group
                                .findViewById(checkedId);
                    }
                });

        // Add the Listener to the Submit Button
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                String dated = Date.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton)view.findViewById(selectedId);
                if (selectedButton != null) {
                    String selectedTime = (String) selectedButton.getText();
                    Log.d("TAGgeek", "onClick: "+selectedTime+" "+dated);
                    Bundle bundles = new Bundle();
                    bundles.putString("title", title);
                    bundles.putString("image", image);
                    bundles.putString("time", selectedTime);
                    bundles.putString("date", dated);
                    bundles.putString("genre", genre);
                    bundles.putString("lang", lang);

                    Fragment fragment = new ReservationsFragment();
                    fragment.setArguments(bundles);
                    FragmentTransaction fragmentTransaction =getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.hfrag,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    updateLabel();
                }
            }
        };
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel(){
        String myFormat="dd/MM/YYYY";
        SimpleDateFormat dateFormat= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        }
        Date.setText(dateFormat.format(myCalendar.getTime()));
    }




}