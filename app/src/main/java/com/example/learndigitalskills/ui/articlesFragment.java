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
import android.widget.ListView;
import android.widget.Toast;

import com.example.learndigitalskills.R;
import com.example.learndigitalskills.db.adapters.ArticlesListViewAdapter;
import com.example.learndigitalskills.db.models.Article;
import com.example.learndigitalskills.db.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link articlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class articlesFragment extends Fragment {

    private FirebaseUser currentUser;

    Button buttonFilter;
    ListView listViewArticles;

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

        // Initialize FirebaseUser
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Bind UI elements
        buttonFilter = view.findViewById(R.id.articles_button_filter);
        listViewArticles = view.findViewById(R.id.articles_listView_articles);

        // Setup listeners for buttons
        buttonFilter.setOnClickListener(v -> {
            filterDialogue();
        });

        // Populate ListView
        populateListView();
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

    private void populateListView() {
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


                        // Retrieve articles from database
                        CollectionReference articles = FirebaseFirestore.getInstance().collection("articles");

                        articles.get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        // Data retrieval successful
                                        // Map data to User object
                                        User user = documentSnapshot.toObject(User.class);

                                        // Map Articles to ArrayList of Articles
                                        ArrayList<Article> articlesList = new ArrayList<>();

                                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                            Article article = document.toObject(Article.class);
                                            articlesList.add(article);
                                        }

                                        // Create and setup ListViewAdapter
                                        ArticlesListViewAdapter adapter = new ArticlesListViewAdapter(getContext(), articlesList, user.getArticlesCompleted());
                                        listViewArticles.setAdapter(adapter);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Data retrieval failed
                        Toast.makeText(getActivity(), "Data retrieval failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}