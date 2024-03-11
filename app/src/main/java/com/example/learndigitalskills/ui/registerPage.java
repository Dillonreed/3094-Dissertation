package com.example.learndigitalskills.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.learndigitalskills.R;

public class registerPage extends AppCompatActivity {

    Button termsAndConditionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

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
}