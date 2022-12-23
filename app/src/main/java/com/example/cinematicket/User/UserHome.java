package com.example.cinematicket.User;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinematicket.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_home);

        //keep selected fragment when rotating the device
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.hfrag,
//                    new HomeFragment()).commit();
//        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.hostfarg, new HomeFragment()).commit();
                return true;

            case R.id.navigation_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.hostfarg, new FavoritesFragment()).commit();
                return true;

            case R.id.navigation_reserve:
                getSupportFragmentManager().beginTransaction().replace(R.id.hostfarg, new MyReservationsFragment()).commit();
                return true;

            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.hostfarg, new ProfileFragment()).commit();
                return true;
        }
        return false;

    }

}