package com.pro.fooddonorke.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.pro.fooddonorke.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = SignupActivity.class.getSimpleName();
    private String mName;

    @BindView(R.id.signup_btn) Button mCreateUserButton;
    @BindView(R.id.nameOutlinedTextField)
    TextInputLayout mNameEditText;
    @BindView(R.id.emailOutlinedTextField) TextInputLayout mEmailEditText;
    @BindView(R.id.passwordOutlinedTextField) TextInputLayout mPasswordEditText;
    @BindView(R.id.confirmPasswordOutlinedTextField) TextInputLayout mConfirmPasswordEditText;
    @BindView(R.id.signup_login_text_view) TextView mLoginTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mLoginTextView.setOnClickListener(this);
        mCreateUserButton.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        if (view == mLoginTextView) {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        if (view == mCreateUserButton) {
            createNewUser();
        }
    }

    private void createNewUser() {
        mName = Objects.requireNonNull(mNameEditText.getEditText()).getText().toString().trim();
        final String email = Objects.requireNonNull(mEmailEditText.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(mPasswordEditText.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(mConfirmPasswordEditText.getEditText()).getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(mName);
        boolean validPassword = isValidPassword(password, confirmPassword);
        if (!validEmail || !validName || !validPassword) return;

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Log.d(TAG, "Registration successful");
                createFirebaseUserProfile(Objects.requireNonNull(task.getResult().getUser()));
                FirebaseAuth.getInstance().signOut();
            }else {
                Toast.makeText(SignupActivity.this,"Registration failed."+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return true;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPasswordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void createFirebaseUserProfile(final FirebaseUser user) {

        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "The display name has ben set", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
