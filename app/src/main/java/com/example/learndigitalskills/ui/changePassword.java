package com.example.learndigitalskills.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learndigitalskills.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changePassword extends AppCompatActivity {

    private FirebaseUser currentUser;

    Toolbar changePasswordToolbar;
    EditText editTextOldPassword, editTextNewPassword, editTextNewPasswordConfirm;
    Button buttonChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_page);

        // Initialize FirebaseUser
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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

        // Bind UI Elements
        editTextOldPassword = findViewById(R.id.change_password_editText_old_password);
        editTextNewPassword = findViewById(R.id.change_password_editText_new_password);
        editTextNewPasswordConfirm = findViewById(R.id.change_password_editText_new_password_confirm);
        buttonChangePassword = findViewById(R.id.change_password_button_change_password);

        // Setup listener for button clicks
        buttonChangePassword.setOnClickListener(v -> {
            if (checkFields()) {
                changeUserPassword();
            }
        });
    }

    private boolean checkFields(){
        // Ensure old password field is filled
        if (TextUtils.isEmpty(editTextOldPassword.getText().toString())) {
            editTextOldPassword.setError("Field empty");
            editTextOldPassword.requestFocus();
            return false;
        }

        // Ensure new password is atleast 8 characters to meet Firebase requirements
        if (editTextNewPassword.getText().toString().length() < 8) {
            editTextNewPassword.setError("Password must be atleast 8 characters");
            editTextNewPassword.requestFocus();
            return false;
        }

        // Ensure confirm password field is filled, and that it matches the password field
        if (TextUtils.isEmpty(editTextNewPassword.getText().toString())) {
            editTextNewPassword.setError("Field empty");
            editTextNewPassword.requestFocus();
            return false;
        } else if (!TextUtils.equals(editTextNewPassword.getText().toString(), editTextNewPasswordConfirm.getText().toString())) {
            editTextNewPasswordConfirm.setError("Passwords don't match");
            editTextNewPasswordConfirm.requestFocus();
            return false;
        }

        return true;
    }

    private void changeUserPassword(){
        // Obtain credential for user using their email and old password
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), editTextOldPassword.getText().toString());

        // Reauthenticate user to be able to change password
        currentUser.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // User is reauthenticated
                        currentUser.updatePassword(editTextNewPassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // Password changed successfully
                                        Toast.makeText(changePassword.this, "Password Changed", Toast.LENGTH_LONG).show();
                                        // Returns user to activity they were sent from
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Password change failed
                                        Toast.makeText(changePassword.this, "Password Change Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // User failed to authenticate
                        editTextOldPassword.setError("Current password doesn't match our records, please try again.");
                        editTextOldPassword.requestFocus();
                    }
                });
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