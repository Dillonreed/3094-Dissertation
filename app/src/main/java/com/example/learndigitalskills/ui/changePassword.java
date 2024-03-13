package com.example.learndigitalskills.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.learndigitalskills.R;

public class changePassword extends AppCompatActivity {

    Toolbar changePasswordToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_page);

        // Setup App Bar
        changePasswordToolbar = findViewById(R.id.change_password_toolbar);
        changePasswordToolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        setSupportActionBar(changePasswordToolbar);

        // Configure App Bar
        ActionBar changePasswordActionBar = getSupportActionBar();
        if (changePasswordActionBar != null){
            // Setting title
            changePasswordActionBar.setTitle(R.string.change_password_title_text);

            // Customize back button
            changePasswordActionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

            // Enable back button
            changePasswordActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static Intent getIntent(Context context){
        return new Intent(context, changePassword.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}