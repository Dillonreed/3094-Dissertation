package com.example.learndigitalskills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.learndigitalskills.ui.registerPage;
import com.example.learndigitalskills.ui.loginPage;

public class MainActivity extends AppCompatActivity {
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
            openRegisterPage();
        });

        registerButton.setOnClickListener(v -> {
            openLoginPage();
        });
    }

    private void openRegisterPage(){
        Intent intent = registerPage.getIntent(this);
        startActivity(intent);
    }

    private void openLoginPage(){
        Intent intent = loginPage.getIntent(this);
        startActivity(intent);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, MainActivity.class);
    }
}