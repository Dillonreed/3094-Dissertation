package com.example.learndigitalskills.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.learndigitalskills.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link articlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class articlesFragment extends Fragment {

    Button filterButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public articlesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment articlesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static articlesFragment newInstance(String param1, String param2) {
        articlesFragment fragment = new articlesFragment();
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
        return inflater.inflate(R.layout.articles_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind UI elements
        filterButton = view.findViewById(R.id.articles_button_filter);

        // Setup listeners for buttons
        filterButton.setOnClickListener(v -> {
            filterDialogue();
        });
    }

    private void filterDialogue() {
        // Create a layout inflator to use to create the dialogue box
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        // Inflate the layout file
        View filterDialogueView = layoutInflater.inflate(R.layout.articles_filter_dialogue, null);

        // Assigns the view to the dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(filterDialogueView);
        builder.setTitle("Filter");

        // Setting up buttons for the dialogue
        builder.setPositiveButton("Apply Filters", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Creates and shows the dialogue
        AlertDialog articlesFilterDialogue = builder.create();
        articlesFilterDialogue.show();
    }
}