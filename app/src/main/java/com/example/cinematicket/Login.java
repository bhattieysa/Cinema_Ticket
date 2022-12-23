package com.example.cinematicket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinematicket.Admin.AdminHome;
import com.example.cinematicket.User.UserHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
TextView signup,login;
EditText email,password;
String emailString, passwordString,type;
ProgressDialog progressDialog;
FirebaseAuth firebaseAuth;
DatabaseReference reference;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        signup = findViewById(R.id.textView2);
        login = findViewById(R.id.login_btn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.passwordedts);
        imageView = findViewById(R.id.passtogle);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Signing In...Please Wait");
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
        firebaseUser = firebaseAuth.getCurrentUser();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Signup.class));
            }
        });


        //login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                emailString = email.getText().toString();
                passwordString = password.getText().toString();


                if (emailString.equals("admin@gmail.com")){

                    firebaseAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String userID = firebaseAuth.getCurrentUser().getUid();

                                progressDialog.dismiss();
                                startActivity(new Intent(Login.this,AdminHome.class));


                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }else {

                    firebaseAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userID = firebaseAuth.getCurrentUser().getUid();


                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.w("TAGfcm", "Fetching FCM registration token failed", task.getException());
                                                    return;
                                                }

                                                // Get new FCM registration token
                                                String token = task.getResult();
                                                Map<String,Object> tokenMap=new HashMap<>(1);
                                                tokenMap.put("fcm_token",token);
                                                FirebaseFirestore.getInstance().collection("tokens")
                                                        .add(tokenMap).addOnSuccessListener(documentReference1 -> {
                                                            Log.d("Success", "onComplete: "+"Success");
                                                        });



                                                Query query = mDatabase.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                query.addValueEventListener(new ValueEventListener() {
                                                    @SuppressLint("NewApi")
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                                            String type = ds.child("type").getValue().toString();
                                                            Log.d("TAGytpe", "onDataChange: " + type);


                                                            if (type.equals("user")) {

                                                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                                                                reference.child("token").setValue(token);
                                                                Log.d("tokencheck", "onDataChange: "+"fff"+token);
                                                                startActivity(new Intent(getApplicationContext(), UserHome.class));
                                                            } else {
                                                                progressDialog.dismiss();
                                                            }


                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        progressDialog.dismiss();

                                                    }
                                                });


                                            }
                                        });


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });





        //.......Password Toggle........
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageView.getId()==R.id.passtogle){

                    if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                        //Show Password
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{

                        //Hide Password
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }


            }
        });



    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                String type = firebaseUser.getEmail();
                            if (!type.equals("admin@gmail.com")) {

                                Intent intent = new Intent(Login.this, UserHome.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else {

                                Intent intent = new Intent(Login.this, AdminHome.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }

        }
    };


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }



}