package com.pro.fooddonorke.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.fooddonorke.utilities.Constants;

import java.util.HashMap;

import com.pro.fooddonorke.R;

public class ProfileActivity extends AppCompatActivity {

    TextInputLayout name, email, phone, location, description;
    Button updateButton;
    ImageView profileImage;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        updateButton = (Button) findViewById(R.id.updateButton);

        root = db.getReference(Constants.FIREBASE_CHILD_PROFILE).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ProfileData = name.getEditText().getText().toString();
                String ProfilePhone = phone.getEditText().getText().toString();
                String ProfileEmail = email.getEditText().getText().toString();
                String ProfileLocation = location.getEditText().getText().toString();
                String ProfileDescription = description.getEditText().getText().toString();


                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("name", ProfileData);
                userMap.put("email", ProfileEmail);
                userMap.put("phone", ProfilePhone);
                userMap.put("location", ProfileLocation);
                userMap.put("description", ProfileDescription);

                root.push().setValue(userMap);

                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }
        });

    }


//    public void sendData(){
//        String ProfileData = name.getEditText().getText().toString();
//
//        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        if(!TextUtils.isEmpty(ProfileData)){
//
//            ProfileData data = new ProfileData();
//            databaseReference.child(id).setValue(data);
//
//            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
//
//        }else {
//            Toast.makeText(this, "Profile not Updated", Toast.LENGTH_SHORT).show();
//        }
//    }

}