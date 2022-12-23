package com.example.cinematicket.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cinematicket.FcmRetrofitDAO;
import com.example.cinematicket.JavaMailAPI;
import com.example.cinematicket.Model.FCMModel;
import com.example.cinematicket.Model.NotificationModel;
import com.example.cinematicket.Model.ReservationModel;
import com.example.cinematicket.Model.SelectedPlaces;
import com.example.cinematicket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.error.OnError;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBookingFragment extends Fragment {
TextView moviename,movietime,moviegenre,seatcount,movielang,custname,custemail,custphone,date,totalseats,perticket,totalamount;
String mname,mtime,mgenre,mseat,mlang,cname,cmail,cphone,mdate,seatcounts;
int perseats = 8;
String YOUR_CLIENT_ID = "Aam5OM9ool-TKdBHAlteueHIMDHeuciJnOYRjIAHo3pLX0PgRqjbASUOM-7EitN8BVPmHnQ5sukotNEp";
    DatabaseReference databaseReference,reference,dbReference,mDatabase;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    List<SelectedPlaces> list;
    private FcmRetrofitDAO retrofitDAO;
    PayPalButton paymentButtonContainer;
    String amountt;
    String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_confirm_booking, container, false);
        moviegenre = view.findViewById(R.id.genretxt);
        moviename = view.findViewById(R.id.movietitle);
        movietime = view.findViewById(R.id.showttxt);
        seatcount = view.findViewById(R.id.seatttxt);
        movielang = view.findViewById(R.id.langtxt);
        custname = view.findViewById(R.id.custnametxt);
        custemail = view.findViewById(R.id.custemailtxt);
        custphone = view.findViewById(R.id.custphonetxt);
        totalseats = view.findViewById(R.id.tseatstxt);
        perticket = view.findViewById(R.id.ptickettxt);
        totalamount = view.findViewById(R.id.tamounttxt);
        paymentButtonContainer = view.findViewById(R.id.payment_button_container);
        date = view.findViewById(R.id.datetxt);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...Please Wait.");
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
        retrofitDAO = FcmRetrofitDAO.RETROFIT.create(FcmRetrofitDAO.class);
        CheckoutConfig config = new CheckoutConfig(
                getActivity().getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW
        );
        PayPalCheckout.setConfig(config);
        paymentButtonContainer.setEnabled(true);
        paymentButtonContainer.setVisibility(View.VISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        list = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mname = bundle.getString("title");
            mtime = bundle.getString("time");
            list = bundle.getParcelableArrayList("seat");
            mdate = bundle.getString("date");

            mgenre = bundle.getString("genre");
            mlang = bundle.getString("lang");
            seatcounts = bundle.getString("seatcount");
            moviename.setText(mname);
            movietime.setText(mtime);
            seatcount.setText(seatcounts);
            date.setText(mdate);

            moviegenre.setText(mgenre);
            movielang.setText(mlang);
        }

        Query query = mDatabase.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String name = ""+ ds.child("fullName").getValue();
                    String email = ""+ ds.child("email").getValue();
                    phone = ""+ ds.child("phone").getValue();
                    custphone.setText(phone);
                    custname.setText(name);
                    custemail.setText(email);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });

        DecimalFormat formater = new DecimalFormat("0.00");
        totalseats.setText(String.valueOf(list.size()));
        String perseat = formater.format(perseats);
        perticket.setText("$ "+perseat);
        int totals = Integer.parseInt(totalseats.getText().toString())*perseats;
        amountt = formater.format(totals);
        totalamount.setText("$ "+amountt);
        databaseReference = database.getReference("Seats").child(mname).child(mdate.replace("/","")).child(mtime);
        reference = database.getReference("UserReservations");
        dbReference = database.getReference("Reserves");
        paypalButton();


        return view;
    }
    private void paypalButton() {
        paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        progressDialog.show();
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(amountt)
                                                        .build()
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                progressDialog.show();
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                for (int i = 0; i < list.size(); i++) {
                                    SelectedPlaces models = list.get(i);
                                    int pos = models.getPlaces()+1;
                                    ReservationModel model = new ReservationModel(
                                            firebaseUser.getUid(), mname, mdate, cname,phone, cmail, String.valueOf(pos), mtime);

                                    databaseReference.child(String.valueOf(pos)).child(firebaseUser.getUid()).setValue(model);
                                    ReservationModel modelss = new ReservationModel(
                                            firebaseUser.getUid(), mname, mdate, cname, phone, cmail, seatcounts, mtime);
                                    reference.child(firebaseUser.getUid()).child(mname).setValue(modelss);
                                    dbReference.child(mname).setValue(modelss);
                                    getToken();
                                    sendmail();
                                    startActivity(new Intent(getActivity(),UserHome.class));
                                    getActivity().finish();

                                    progressDialog.dismiss();

                                }
                                Toast.makeText(getActivity(), "Payment successful.",
                                                Toast.LENGTH_SHORT)
                                        .show();

                            }
                        });
                    }

                }




        );
        new OnError() {
            @Override
            public void onError(@NotNull ErrorInfo errorInfo) {
                Log.d("OnError", String.format("Error: %s", errorInfo));
            }
        };
    }
    private  void getToken(){
        Query query = mDatabase.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String token = ""+ ds.child("token").getValue();
                    sendNotification(token);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }
    private void sendmail(){

        cname = custname.getText().toString();
        cmail = custemail.getText().toString();
        String email = "cinematicket75@gmail.com";
        String emailpass = "dqwpwsdaegpeaihm";
        String mSubject = "Cinema Ticket App";
        String message = "\nYour Seats Are "+seatcounts+" At "+mtime+" On "+mdate+" Be Ready";
        String mMessage = "Hello "+cname+" You Just Booked "+list.size()+" Seats For "+mname+message;


            JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(), cmail, mSubject, mMessage,email , emailpass);
            javaMailAPI.execute();

    }
    private void sendNotification(String token) {

        String  titles = " Congratulations";
        String subject = "Your Seats Have Been Successfully Booked For "+mname;



                            FCMModel fcmModel = new FCMModel(Collections.singletonList(token), new NotificationModel(titles, subject));
                            Call<Void> voidCall = retrofitDAO.sendNotification(getHeaders(), fcmModel);
                            voidCall.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {

                                    if (response.isSuccessful())

                                        Log.d("TAGfcmtok", "onResponse: " + "Sent");
                                }

                                @Override
                                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {

                                    Log.d("TAGfcmtok", "onResponse: " + "Failed");
                                }
                            });


    }
    private HashMap<String,String> getHeaders(){
        HashMap<String,String> headers=new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Authorization","key=AAAA8oblU0w:APA91bEz7TpVw5M0iFAwynjcPfsi2B1Eq-7LzvdJnginffgXz9Gv3RMLZ_-PQrBuB_HkWXv9L1NWCe5yg-kmH5x4n6155EJ3La2gYaBI5ZrP13YJYNuOoZ0if4wLgd5zQ_sNcRQtX3Sg");
        return headers;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}