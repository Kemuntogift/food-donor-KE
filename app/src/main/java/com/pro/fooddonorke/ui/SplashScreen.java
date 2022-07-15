package com.pro.fooddonorke.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pro.fooddonorke.R;

public class SplashScreen extends AppCompatActivity {

    ImageView splashImg;
    TextView title;
    TextView statement;
    FloatingActionButton arrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

            splashImg = findViewById(R.id.splashImg);
            title = findViewById(R.id.title);
            statement = findViewById(R.id.statement);
            arrowButton = findViewById(R.id.arrowButton);

            addAnimations();

            arrowButton.setOnClickListener(view -> {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
            });

        }

        private void addAnimations(){
            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .playOn(title);

            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .playOn(statement);

            YoYo.with(Techniques.Flash)
                    .duration(700)
                    .playOn(arrowButton);
        }
    }