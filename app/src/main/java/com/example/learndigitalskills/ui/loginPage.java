package com.example.learndigitalskills.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.learndigitalskills.R;

public class loginPage extends AppCompatActivity {

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize buttons
        loginButton = findViewById(R.id.login_button_login);

        // Sets up listeners for button clicks
        loginButton.setOnClickListener(v -> {
            // Will add authentication later, just need to setup basic navigation for app
            openBasePage();
        });
    }

    private void openBasePage(){
        Intent intent = basePage.getIntent(this);
        startActivity(intent);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, loginPage.class);
    }
}