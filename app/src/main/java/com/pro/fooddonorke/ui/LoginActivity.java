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
import com.pro.fooddonorke.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.login_signup_text_view)
    TextView mRegisterTextView;
    @BindView(R.id.login_btn)
    Button mPasswordLoginButton;
    @BindView(R.id.email_text_input_layout)
    TextInputLayout mEmailEditText;
    @BindView(R.id.passwordOutlinedTextField)
    TextInputLayout mPasswordEditText;
    @BindView(R.id.firebaseProgressBar)
    ProgressBar mSignInProgressBar;
    @BindView(R.id.loadingTextView)
    TextView mLoadingSignUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                if (user.isEmailVerified()){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    mAuth.signOut();
                    Toast.makeText(getApplicationContext(), R.string.verification_prompt, Toast.LENGTH_SHORT).show();
                }
            }
        };


        mPasswordLoginButton.setOnClickListener(this);
        mRegisterTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == mRegisterTextView) {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        }

        if (view == mPasswordLoginButton) {
            loginWithPassword();
            showProgressBar();
        }
    }

    private void loginWithPassword() {
        String email = Objects.requireNonNull(mEmailEditText.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(mPasswordEditText.getEditText()).getText().toString().trim();
        if (email.equals("")) {
            mEmailEditText.setError("Please enter your email");
            return;
        }
        if (password.equals("")) {
            mPasswordEditText.setError("Password cannot be blank");
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    hideProgressBar();
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "signInWithEmail", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showProgressBar() {
        mSignInProgressBar.setVisibility(View.VISIBLE);
        mLoadingSignUp.setVisibility(View.VISIBLE);
        mPasswordLoginButton.setVisibility(View.GONE);
        mRegisterTextView.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        mSignInProgressBar.setVisibility(View.GONE);
        mLoadingSignUp.setVisibility(View.GONE);
        mPasswordLoginButton.setVisibility(View.VISIBLE);
        mRegisterTextView.setVisibility(View.VISIBLE);
    }
}
