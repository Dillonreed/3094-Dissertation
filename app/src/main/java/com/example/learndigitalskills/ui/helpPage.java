package com.example.learndigitalskills.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.learndigitalskills.R;

public class helpPage extends AppCompatActivity {

    Toolbar helpToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_page);

        // Setup App Bar
        helpToolbar = findViewById(R.id.help_toolbar);
        helpToolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        setSupportActionBar(helpToolbar);

        // Configure App Bar
        ActionBar helpActionBar = getSupportActionBar();
        if (helpActionBar != null){
            // Setting title
            helpActionBar.setTitle(R.string.help_title_text);

            // Customize back button
            helpActionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

            // Enable back button
            helpActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static Intent getIntent(Context context){
        return new Intent(context, helpPage.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}