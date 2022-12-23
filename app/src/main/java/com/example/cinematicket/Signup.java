package com.example.cinematicket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinematicket.Model.UserModel;
import com.example.cinematicket.User.UserHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    EditText name, email, password, conpass, mobile;
    CountryCodePicker ccp;
    TextView signp,login;
    String emailstring, passwordstring, namestring, phonestring,token;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userID;
    ProgressDialog progressDialog;
    ImageView passtoggle, confirmpasstoggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        name = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.passwordedt);
        conpass = findViewById(R.id.conpasswordedt);
        mobile =findViewById(R.id.phoneedt);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(mobile);
        signp = findViewById(R.id.signup_btn);
        login = findViewById(R.id.textView2);
        passtoggle = findViewById(R.id.passtog);
        confirmpasstoggle = findViewById(R.id.passtogs);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        progressDialog = new ProgressDialog(Signup.this);
        progressDialog.setMessage("Loading....Please Wait");




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Login.class));
            }
        });


        //.......Password Toggle........
        passtoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(passtoggle.getId()==R.id.passtog){

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




        //.......Password Toggle........
        confirmpasstoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(confirmpasstoggle.getId()==R.id.passtogs){

                    if(conpass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                        //Show Password
                        conpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{

                        //Hide Password
                        conpass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }


            }
        });




        signp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailstring = email.getText().toString().trim();
                passwordstring = password.getText().toString().trim();
                namestring = name.getText().toString();
                phonestring  = ccp.getFullNumberWithPlus().replace(" ", "");

                if (TextUtils.isEmpty(emailstring)){
                    email.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(phonestring)){
                    email.setError("Phone is Required.");
                    return;
                }

                if (TextUtils.isEmpty(namestring)){
                    email.setError("Name is Required.");
                    return;
                }

                if (TextUtils.isEmpty(passwordstring)){
                    password.setError("Password is Required.");
                    return;
                }

                if (passwordstring.length() <8 ){
                    password.setError("Password Must Be Atleast 8 Characters");
                    return;
                }

                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(emailstring,passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){



                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w("TAGfcm", "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }

                                            // Get new FCM registration token
                                             token = task.getResult();
                                            Map<String,Object> tokenMap=new HashMap<>(1);
                                            tokenMap.put("fcm_token",token);
                                            FirebaseFirestore.getInstance().collection("tokens")
                                                    .add(tokenMap).addOnSuccessListener(documentReference1 -> {
                                                        Log.d("Success", "onComplete: "+"Success");
                                                    });

                                            Log.d("TAGtken", "onComplete: "+token);

                                            userID = mAuth.getCurrentUser().getUid();
                                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                                            DatabaseReference root = db.getReference("Users");

                                            UserModel obj = new UserModel(
                                                    namestring,emailstring,passwordstring,phonestring,token,"user");
                                            root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(obj);

                                            progressDialog.dismiss();
                                            Toast.makeText(Signup.this, "SignUp Successfull.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Signup.this, UserHome.class));
                                        }
                                    });

                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }



}