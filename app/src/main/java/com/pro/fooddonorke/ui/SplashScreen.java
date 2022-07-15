package com.pro.fooddonorke.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pro.fooddonorke.R;

public class SplashScreen extends AppCompatActivity {

    ImageView splashImg;
    TextView title;
    TextView statement;
    FloatingActionButton arrowButton;
    SharedPreferences welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

            splashImg = findViewById(R.id.splashImg);
            title = findViewById(R.id.title);
            statement = findViewById(R.id.statement);

            arrowButton = findViewById(R.id.arrowButton);
//            arrowButton.setOnClickListener(view -> {
//                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
//                startActivity(intent);
//            });

            // we go ahead and use the handler class
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    welcome = getSharedPreferences("welome", MODE_PRIVATE);
                    boolean isFirstTime = welcome.getBoolean("user", true);


                    if (isFirstTime){
                        // edit the shared preference.
                        SharedPreferences.Editor editor = welcome.edit();
                        editor.putBoolean("user", false);
                        editor.commit();
                        // we declare the intent of moving from the current activity to the next activity.
                        Intent intent = new Intent(getApplicationContext(), Onboarding.class);

                        // we call the startActivity method and pass to it out intent.
                        startActivity(intent);

                        // we call the finish method to destroy or kill the activity.
                        finish();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                        // we call the startActivity method and pass to it out intent.
                        startActivity(intent);

                        // we call the finish method to destroy or kill the activity.
                        finish();
                    }

                }
            }, 2000);

        }
    }