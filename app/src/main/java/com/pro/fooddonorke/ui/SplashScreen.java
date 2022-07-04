package com.pro.fooddonorke.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro.fooddonorke.ProfileActivity;
import com.pro.fooddonorke.R;

public class SplashScreen extends AppCompatActivity {

    ImageView splashImg;
    TextView title;
    TextView statement;
    Button arrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

            splashImg = findViewById(R.id.splashImg);
            title = findViewById(R.id.title);
            statement = findViewById(R.id.statement);

            arrowButton = findViewById(R.id.arrowButton);
            arrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SplashScreen.this, ProfileActivity.class);
                    startActivity(intent);
                }
            });

        }
    }
}