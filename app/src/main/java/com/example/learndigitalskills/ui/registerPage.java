package com.example.learndigitalskills.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.learndigitalskills.R;

public class registerPage extends AppCompatActivity {

    Toolbar registerToolbar;
    Button termsAndConditionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        // Setup App Bar
        registerToolbar = findViewById(R.id.register_toolbar);
        registerToolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        setSupportActionBar(registerToolbar);

        // Configure App Bar
        ActionBar registerActionBar = getSupportActionBar();
        if (registerActionBar != null){
            // Setting title
            registerActionBar.setTitle(R.string.register_title);

            // Customize back button
            registerActionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

            // Enable back button
            registerActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Bind UI elements
        termsAndConditionsButton = findViewById(R.id.register_button_terms_and_conditions);

        // Setup listeners for buttons
        termsAndConditionsButton.setOnClickListener(v -> {
            // Create a layout inflator to use to create the dialogue box
            LayoutInflater layoutInflater = LayoutInflater.from(this);

            // Inflate the layout file
            View termsAndConditionsDialogueView = layoutInflater.inflate(R.layout.terms_and_conditions_dialog, null);

            // Assigns the view to the dialogue
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setView(termsAndConditionsDialogueView);
            builder.setTitle("Terms and Conditions");

            // Setting up buttons for the dialogue
            builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Creates and shows the dialogue
            AlertDialog termsAndConditionsDialogue = builder.create();
            termsAndConditionsDialogue.show();
        });
    }

    public static Intent getIntent(Context context){
        return new Intent(context, registerPage.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}