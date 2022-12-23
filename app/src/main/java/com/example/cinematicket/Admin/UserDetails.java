package com.example.cinematicket.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cinematicket.R;

public class UserDetails extends AppCompatActivity {
TextView name, email, phone, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        name = findViewById(R.id.name_textview);
        email = findViewById(R.id.mail_textview);
        phone = findViewById(R.id.phonetxt);
        pass = findViewById(R.id.passtxt);

        Intent intent = getIntent();
        String Name = intent.getStringExtra("name");
        name.setText(Name);
        String Email = intent.getStringExtra("email");
        email.setText(Email);
        String Phone = intent.getStringExtra("phone");
        phone.setText(Phone);
        String Password = intent.getStringExtra("pass");
        pass.setText(Password);
    }
}