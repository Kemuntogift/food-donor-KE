package com.pro.fooddonorke.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.utilities.Constants;

import java.util.HashMap;
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
    @BindView(R.id.signUpProgressBar)
    ProgressBar mSignUpProgressBar;
    @BindView(R.id.signUpTextView)
    TextView mLoadingSignUp;

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
            redirectToLogin();
        }

        if (view == mCreateUserButton) {
            createNewUser();
            showProgressBar();
        }
    }

    private void redirectToLogin(){
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void createNewUser() {
        mName = Objects.requireNonNull(mNameEditText.getEditText()).getText().toString().trim();
        final String email = Objects.requireNonNull(mEmailEditText.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(mPasswordEditText.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(mConfirmPasswordEditText.getEditText()).getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(mName);
        boolean validPassword = isValidPassword(password, confirmPassword);
        if (!validEmail || !validName || !validPassword) {
            hideProgressBar();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            hideProgressBar();
            if (task.isSuccessful()){
                final FirebaseUser user = task.getResult().getUser();
                Log.d(TAG, "Registration successful");
                createFirebaseUserProfile(Objects.requireNonNull(user));
                setUpDonationCount(user);
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

    private void setUpDonationCount(FirebaseUser user){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_DONATIONS_STATS).child(user.getUid());

        HashMap<String, String> donationCount = new HashMap<>();
        donationCount.put(Constants.DONATIONS_STAT_FIELD, String.valueOf(0));

        reference.setValue(donationCount).addOnCompleteListener(this, insertTask -> {
            if (insertTask.isSuccessful()){
                Log.d(TAG, "Initial donation count set");
                FirebaseAuth.getInstance().signOut();
                redirectToLogin();
            } else {
                Log.d(TAG, "Initial donation count not set", insertTask.getException());
            }
        });
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

    private void showProgressBar() {
        mSignUpProgressBar.setVisibility(View.VISIBLE);
        mLoadingSignUp.setVisibility(View.VISIBLE);
        mCreateUserButton.setVisibility(View.GONE);
        mLoginTextView.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        mSignUpProgressBar.setVisibility(View.GONE);
        mLoadingSignUp.setVisibility(View.GONE);
        mCreateUserButton.setVisibility(View.VISIBLE);
        mLoginTextView.setVisibility(View.VISIBLE);
    }
}
