package com.example.learndigitalskills.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.learndigitalskills.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Toolbar loginToolbar;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Setup App Bar
        loginToolbar = findViewById(R.id.login_toolbar);
        loginToolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        setSupportActionBar(loginToolbar);

        // Configure App Bar
        ActionBar loginActionBar = getSupportActionBar();
        if (loginActionBar != null){
            // Setting title
            loginActionBar.setTitle(R.string.login_title);

            // Customize back button
            loginActionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

            // Enable back button
            loginActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Bind UI elements
        editTextUsername = findViewById(R.id.login_editText_username);
        editTextPassword = findViewById(R.id.login_editText_password);
        buttonLogin = findViewById(R.id.login_button_login);

        // Sets up listeners for button clicks
        buttonLogin.setOnClickListener(v -> {
            if (checkFields()) {
                loginUser();
            }
        });
    }

    private boolean checkFields() {
        // Ensure username field is filled
        if (TextUtils.isEmpty(editTextUsername.getText().toString())) {
            editTextUsername.setError("Field empty");
            editTextUsername.requestFocus();
            return false;
        }

        // Ensure password field is filled
        if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            editTextPassword.setError("Field empty");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String email = username + "@learndigitalskills.com";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // User logged in successfully
                        Toast.makeText(loginPage.this, "User Logged In Successfully", Toast.LENGTH_SHORT).show();
                        openBasePage();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // User failed to log in
                        Toast.makeText(loginPage.this, "Log In Failed, Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openBasePage(){
        Intent intent = basePage.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, loginPage.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}