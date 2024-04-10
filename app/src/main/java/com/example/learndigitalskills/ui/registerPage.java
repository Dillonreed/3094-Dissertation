package com.example.learndigitalskills.ui;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.learndigitalskills.R;

import com.example.learndigitalskills.db.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
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
        // Ensure password field is filled
        if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            editTextPassword.setError("Field empty");
            editTextPassword.requestFocus();
            return false;
        }

        // Ensure password is atleast 8 characters to meet Firebase requirements
        if (editTextPassword.getText().toString().length() < 8) {
            editTextPassword.setError("Password must be atleast 8 characters");
            editTextPassword.requestFocus();
            return false;
        }

        // Ensure confirm password field is filled, and that it matches the password field
        if (TextUtils.isEmpty(editTextConfirmPassword.getText().toString())) {
            editTextConfirmPassword.setError("Field empty");
            editTextConfirmPassword.requestFocus();
            return false;
        } else if (!TextUtils.equals(editTextPassword.getText().toString(), editTextConfirmPassword.getText().toString())) {
            editTextConfirmPassword.setError("Passwords don't match");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        // Ensures that the terms and conditions check box is checked
        if (!checkBoxTermsAndConditions.isChecked()) {
            checkBoxTermsAndConditions.setError("Terms and Conditions not agreed to");
            checkBoxTermsAndConditions.requestFocus();
            return false;
        }

        return true;
    }

    private void generateUsername() {
        // Generate random username/email
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
        // Retrieve generated username and provided password
        String generatedUsername = editTextGeneratedUsername.getText().toString();
        String generatedEmail = generatedUsername + "@learndigitalskills.com";
        String password = editTextPassword.getText().toString();

        // Creates account with Firebase with generated/provided information
        mAuth.createUserWithEmailAndPassword(generatedEmail, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(registerPage.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();

                        // Create User object
                        User user = new User();
                        user.setUserId(mAuth.getUid());
                        user.setUsername(generatedUsername);
                        user.setArticlesCompleted(new ArrayList<Integer>());

                        // Instantiate database reference to users
                        DocumentReference document = FirebaseFirestore.getInstance().document("users/"+user.getUsername());

                        // Add data to database
                        document.set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // User added to database
                                        // Move user to base activity
                                        Intent intent = basePage.getIntent(registerPage.this);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error adding user to database
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(registerPage.this, "Registration Failed, Please Try Again", Toast.LENGTH_LONG).show();
                    }
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