package com.example.batmanlol.demandui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static int Splash_Time_Out = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser= mAuth.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

       // ImageView rotateImage = (ImageView) findViewById(R.id.img);
        //Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        //rotateImage.startAnimation(startRotateAnimation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(currentUser==null){
                    Intent Login = new Intent(MainActivity.this, Login.class);
                    startActivity(Login);
                    finish();
                }
                else{
                    Intent Login = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(Login);
                    finish();
                }

            }
        },Splash_Time_Out);
    }



    private void updateUI(FirebaseUser currentUser) {

    }

    private void callLoginPage() {

    }
}
