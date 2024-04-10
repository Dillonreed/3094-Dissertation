package com.example.learndigitalskills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.learndigitalskills.ui.basePage;
import com.example.learndigitalskills.ui.registerPage;
import com.example.learndigitalskills.ui.loginPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        // Initialize buttons
        loginButton = findViewById(R.id.welcome_button_login);
        registerButton = findViewById(R.id.welcome_button_register);

        // Sets up listeners for button clicks
        loginButton.setOnClickListener(v -> {
            openLoginPage();
        });

        registerButton.setOnClickListener(v -> {
            openRegisterPage();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If the user is already authenticated (has logged in/registered recently) then open
        // base page automatically
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            openBasePage();
        }
    }

    private void openRegisterPage(){
        Intent intent = registerPage.getIntent(this);
        startActivity(intent);
    }

    private void openLoginPage(){
        Intent intent = loginPage.getIntent(this);
        startActivity(intent);
    }

    private void openBasePage(){
        Intent intent = basePage.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, MainActivity.class);
    }
}