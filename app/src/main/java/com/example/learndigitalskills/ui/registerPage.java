package com.example.learndigitalskills.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.learndigitalskills.R;

import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class registerPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Toolbar registerToolbar;
    EditText editTextGeneratedUsername, editTextPassword, editTextConfirmPassword;
    CheckBox checkBoxTermsAndConditions;
    Button buttonCreateAccount, buttonTermsAndConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

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
        editTextGeneratedUsername = findViewById(R.id.register_generated_username);
        editTextPassword = findViewById(R.id.register_editText_password);
        editTextConfirmPassword = findViewById(R.id.register_editText_confirm_password);
        buttonTermsAndConditions = findViewById(R.id.register_button_terms_and_conditions);
        checkBoxTermsAndConditions = findViewById(R.id.register_checkbox_button_terms_and_conditions);
        buttonCreateAccount = findViewById(R.id.register_button_create_account);

        // Setup listeners for buttons
        buttonTermsAndConditions.setOnClickListener(v -> {
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

        buttonCreateAccount.setOnClickListener(v -> {
            // If all fields are filled, and passwords match then register the user
            if (checkFields()) {
                registerUser();
            }
        });

        // Generate Random Username
        generateUsername();
    }

    private boolean checkFields(){
        if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            editTextPassword.setError("Field empty");
            editTextPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(editTextConfirmPassword.getText().toString())) {
            editTextConfirmPassword.setError("Field empty");
            editTextConfirmPassword.requestFocus();
            return false;
        } else if (!TextUtils.equals(editTextPassword.getText().toString(), editTextConfirmPassword.getText().toString())) {
            editTextConfirmPassword.setError("Passwords don't match");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        if (!checkBoxTermsAndConditions.isChecked()) {
            checkBoxTermsAndConditions.setError("Terms and Conditions not agreed to");
            checkBoxTermsAndConditions.requestFocus();
            return false;
        }

        return true;
    }

    private void generateUsername() {
        String randomCharacters = UUID.randomUUID().toString().substring(0, 5);
        String generatedUsername = "User-" + randomCharacters;
        String generatedEmail = generatedUsername + "@learndigitalskills.com";

        // Checks if generated username is already present in the system
        mAuth.fetchSignInMethodsForEmail(generatedEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getSignInMethods().isEmpty()) {
                    // Username is valid, set the generated username
                    editTextGeneratedUsername.setText(generatedUsername);
                } else {
                    // Username is already in use, generate a new one recursively
                    generateUsername();
                }
            }
        });
    }

    private void registerUser() {
        // To Add
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