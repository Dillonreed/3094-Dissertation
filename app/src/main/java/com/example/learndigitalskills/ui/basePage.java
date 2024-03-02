package com.example.learndigitalskills.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.learndigitalskills.MainActivity;
import com.example.learndigitalskills.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class basePage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    homeFragment homeFragment = new homeFragment();
    articlesFragment articlesFragment = new articlesFragment();
    settingsFragment settingsFragment = new settingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Sets default fragment to be Home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                // Identifies which fragment to open
                if (itemId == R.id.menu_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
                    return true;
                } else if (itemId == R.id.menu_articles) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, articlesFragment).commit();
                    return true;
                } else if (itemId == R.id.menu_settings) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, settingsFragment).commit();
                    return true;
                }

                return false;
            }
        });

    }

    public static Intent getIntent(Context context){
        return new Intent(context, basePage.class);
    }
}