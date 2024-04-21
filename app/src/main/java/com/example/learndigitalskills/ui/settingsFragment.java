package com.example.learndigitalskills.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learndigitalskills.MainActivity;
import com.example.learndigitalskills.R;
import com.example.learndigitalskills.db.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingsFragment extends Fragment {

    private FirebaseUser currentUser;
    private User user;

    TextView textViewAccountTitle;
    Button buttonNotifications, buttonChangePassword, buttonLogOut, buttonHelp, buttonContactUs, buttonDeleteAccount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public settingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static settingsFragment newInstance(String param1, String param2) {
        settingsFragment fragment = new settingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase User
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Bind UI Elements
        textViewAccountTitle = view.findViewById(R.id.settings_account_title);
        buttonNotifications = view.findViewById(R.id.settings_button_notifications);
        buttonChangePassword = view.findViewById(R.id.settings_button_change_password);
        buttonLogOut = view.findViewById(R.id.settings_button_log_out);
        buttonHelp = view.findViewById(R.id.settings_button_help);
        buttonContactUs = view.findViewById(R.id.settings_button_contact_us);
        buttonDeleteAccount = view.findViewById(R.id.settings_button_delete_account);

        // Setup listeners for buttons
        buttonNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
            startActivity(intent);
        });

        buttonChangePassword.setOnClickListener(v -> {
            Intent intent = changePassword.getIntent(getActivity());
            startActivity(intent);
        });

        buttonLogOut.setOnClickListener(v -> {
            logoutUser();
        });

        buttonHelp.setOnClickListener(v -> {
            Intent intent = helpPage.getIntent(getActivity());
            startActivity(intent);
        });

        buttonContactUs.setOnClickListener(v -> {
            contactUsDialogue();
        });

        buttonDeleteAccount.setOnClickListener(v -> {
            deleteAccountDialogue();
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get string for account title
        String accountTitle = getString(R.string.settings_account_title);

        // Get user object to retrieve username
        // Extract username from email, and capitalize the first letter
        String username = currentUser.getEmail().substring(0, currentUser.getEmail().indexOf("@"));
        username = username.substring(0, 1).toUpperCase() + username.substring(1);

        // Retrieve user information from database
        DocumentReference document = FirebaseFirestore.getInstance().document("users/" + username);
        document.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Data retrieval successful
                        // Map data to user object
                        user = documentSnapshot.toObject(User.class);

                        // Update UI elements
                        textViewAccountTitle.setText(user.getUsername() + "'s " + accountTitle);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Data retrieval unsuccessful
                        Toast.makeText(getActivity(), "Data retrieval failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logoutUser() {
        // Log user out
        FirebaseAuth.getInstance().signOut();

        // Redirect to welcome screen when logging out
        openWelcomePage();
    }

    private void openWelcomePage() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void contactUsDialogue() {
        // Create a layout inflator to use to create the dialogue box
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        // Inflate the layout file
        View contactUsDialogueView = layoutInflater.inflate(R.layout.contact_us_dialogue, null);

        // Assigns the view to the dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(contactUsDialogueView);
        builder.setTitle("Contact Us");

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
    }

    private void deleteAccountDialogue() {
        // Create a layout inflator to use to create the dialogue box
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        // Inflate the layout file
        View deleteAccountDialogueView = layoutInflater.inflate(R.layout.delete_account_dialogue, null);

        // Assigns the view to the dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(deleteAccountDialogueView);
        builder.setTitle("Delete Account");

        // Setting up buttons for the dialogue
        builder.setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // This onClick listener will be overridden later to prevent automatic dismissal
            }
        });

        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Creates the dialogue
        AlertDialog deleteAccountDialogue = builder.create();

        // Set a custom onClick listener for the positive button
        deleteAccountDialogue.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Bind UI element from dialogue
                        EditText editTextPassword = deleteAccountDialogueView.findViewById(R.id.delete_account_editText_password);

                        // Check password field is filled
                        if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                            editTextPassword.setError("Field is empty");
                            editTextPassword.requestFocus();
                        } else {
                            deleteAccount(editTextPassword);
                        }
                    }
                });
            }
        });

        // Show the dialogue
        deleteAccountDialogue.show();
    }



    private void deleteAccount(EditText editTextPassword) {
        // Obtain credential for user using their email and old password
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), editTextPassword.getText().toString());

        // Reauthenticate the user to be able to delete account
        currentUser.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // User is reauthenticated successfully
                        // Delete the user's information from the database

                        // Extract username from email, and capitalize the first letter
                        String username = currentUser.getEmail().substring(0, currentUser.getEmail().indexOf("@"));
                        username = username.substring(0, 1).toUpperCase() + username.substring(1);

                        // Instantiate database reference to users
                        DocumentReference document = FirebaseFirestore.getInstance().document("/users/" + username);

                        document.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // User data deleted
                                        // Delete the user's information from the authentication service
                                        currentUser.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        // User deleted from authentication service
                                                        Toast.makeText(getActivity(), "User Deleted Successfully", Toast.LENGTH_SHORT).show();

                                                        // Redirect user back to welcome page
                                                        openWelcomePage();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Error Deleting User from Authentication Service
                                                        Toast.makeText(getActivity(), "An error has occurred, please try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error Deleting information from database
                                        Toast.makeText(getActivity(), "An error has occurred, please try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // User failed to authenticate
                        editTextPassword.setError("Current password doesn't match our records, please try again.");
                        editTextPassword.requestFocus();
                    }
                });
    }
}