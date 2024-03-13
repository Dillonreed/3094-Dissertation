package com.example.learndigitalskills.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import com.example.learndigitalskills.R;

public class loginPage extends AppCompatActivity {

    Toolbar loginToolbar;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}