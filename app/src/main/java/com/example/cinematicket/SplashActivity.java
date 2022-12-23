package com.example.cinematicket;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        introAnimation();
        //startActivity(new Intent(this,MainActivity.class));
    }

    private void introAnimation() {
        ImageView logoImg = (ImageView) findViewById(R.id.mainLogo);
//        animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        logoImg.setAnimation(animation);


        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        animation.setStartOffset(500);
        animation.setDuration(2000);
        logoImg.setAnimation(animation);
        logoImg.startAnimation(animation);


        TextView swipeText = (TextView)findViewById(R.id.mainText);

        //Slide TextView and set opacity to 100
        swipeText.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.right_to_left_swipe));
        swipeText.animate().alpha(1f).setDuration(1000);
        Animation fadeIn = new AlphaAnimation(1, 0);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setStartOffset(500);
        fadeIn.setDuration(2000);
        swipeText.setAnimation(fadeIn);


        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    Intent intent = new Intent(SplashActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };myThread.start();






    }

}