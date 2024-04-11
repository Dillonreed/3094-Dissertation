package com.example.learndigitalskills.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
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

    private ArrayList<Article> articlesList = new ArrayList<>();
    private ArrayList<Article> shownArticlesList = new ArrayList<>();
    private ArrayList<Integer> completedArticlesList = new ArrayList<>();
    private ArrayList<String> filteredTopics = new ArrayList<>();

    SearchView searchViewSearchBar;
    Button buttonFilter, buttonClearFilters;
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
        searchViewSearchBar = view.findViewById(R.id.articles_searchView_search_bar);
        buttonFilter = view.findViewById(R.id.articles_button_filter);
        buttonClearFilters = view.findViewById(R.id.articles_button_clear_filters);
        listViewArticles = view.findViewById(R.id.articles_listView_articles);

        // Setup listeners for buttons
        buttonFilter.setOnClickListener(v -> {
            filterDialogue();
        });

        buttonClearFilters.setOnClickListener(v -> {
            clearFilters();
        });

        // Populate ListView
        populateListView();

        // Setup listener for search bar
        searchViewSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // When a character is changed

                // Create a list of applicable articles
                ArrayList<Article> filteredArticles = new ArrayList<>();

                for (Article article : shownArticlesList) {
                    if (checkArticle(query, article)) {
                        filteredArticles.add(article);
                    }
                }

                // Filter the list
                filterList(filteredArticles);

                return false;
            }
        });

        // Setup listener for click on listView
        listViewArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get article object of selected item
                Article selectedArticle = (Article) listViewArticles.getItemAtPosition(position);

                // Redirect user to article page of selected item
                openArticlePage(selectedArticle);
            }
        });
    }

    private boolean checkArticle(String query, Article article) {
        // Check if query is contained within the title, content, topic or short description
        if (article.getTitle().toLowerCase().contains(query.toLowerCase())) {
            return true;
        }

        if (article.getTopic().toLowerCase().contains(query.toLowerCase())) {
            return true;
        }

        if (article.getShortDescription().toLowerCase().contains(query.toLowerCase())) {
            return true;
        }

        if (article.getContent().toLowerCase().contains(query.toLowerCase())) {
            return true;
        }

        return false;
    }

    private void filterList(ArrayList<Article> filteredArticles) {
        // Update listView with filtered articles
        ArticlesListViewAdapter adapter = new ArticlesListViewAdapter(getContext(), filteredArticles, completedArticlesList);
        listViewArticles.setAdapter(adapter);
    }

    private void filterDialogue() {
        // Clear currently selected filters on opening
        clearFilters();

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
                // Bind UI elements
                CheckBox checkBoxHandlingInformationAndContent = filterDialogueView.findViewById(R.id.articles_filter_checkBox_handling_information_and_content);
                CheckBox checkBoxCommunicating = filterDialogueView.findViewById(R.id.articles_filter_checkBox_communicating);
                CheckBox checkBoxBeingSafeAndLegalOnline = filterDialogueView.findViewById(R.id.articles_filter_checkBox_being_safe_and_legal_online);
                CheckBox checkBoxProblemSolving = filterDialogueView.findViewById(R.id.articles_filter_checkBox_problem_solving);
                CheckBox checkBoxTransacting = filterDialogueView.findViewById(R.id.articles_filter_checkBox_transacting);

                // Update filtered topics
                if (checkBoxHandlingInformationAndContent.isChecked()) {
                    filteredTopics.add(checkBoxHandlingInformationAndContent.getText().toString());
                }
                if (checkBoxCommunicating.isChecked()) {
                    filteredTopics.add(checkBoxCommunicating.getText().toString());
                }
                if (checkBoxBeingSafeAndLegalOnline.isChecked()) {
                    filteredTopics.add(checkBoxBeingSafeAndLegalOnline.getText().toString());
                }
                if (checkBoxProblemSolving.isChecked()) {
                    filteredTopics.add(checkBoxProblemSolving.getText().toString());
                }
                if (checkBoxTransacting.isChecked()) {
                    filteredTopics.add(checkBoxTransacting.getText().toString());
                }

                // Generate new list of filtered articles
                ArrayList<Article> filteredArticles = new ArrayList<>();

                for (Article article : articlesList) {
                    for (String topic : filteredTopics) {
                        if (checkArticle(topic, article)) {
                            filteredArticles.add(article);
                        }
                    }
                }

                // Filter List
                filterList(filteredArticles);

                // Update list of shown articles
                shownArticlesList = filteredArticles;
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

    private void clearFilters() {
        // Clear currently selected filters on opening
        filteredTopics.clear();
        searchViewSearchBar.setQuery("", false);
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
                                        ArrayList<Article> articlesToShow = new ArrayList<>();

                                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                            Article article = document.toObject(Article.class);
                                            articlesToShow.add(article);
                                        }

                                        // Create and setup ListViewAdapter
                                        ArticlesListViewAdapter adapter = new ArticlesListViewAdapter(getContext(), articlesToShow, user.getArticlesCompleted());
                                        listViewArticles.setAdapter(adapter);

                                        articlesList = articlesToShow;
                                        shownArticlesList = articlesList;
                                        completedArticlesList = user.getArticlesCompleted();
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

    private void openArticlePage(Article selectedArticle) {
        Intent intent = new Intent(getContext(), articlePage.class);
        intent.putExtra("article", selectedArticle);
        startActivity(intent);
    }
}