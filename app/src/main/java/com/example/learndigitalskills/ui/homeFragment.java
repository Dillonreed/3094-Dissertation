package com.example.learndigitalskills.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learndigitalskills.R;
import com.example.learndigitalskills.db.adapters.ArticlesListViewAdapter;
import com.example.learndigitalskills.db.models.Article;
import com.example.learndigitalskills.db.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    private FirebaseUser currentUser;
    private Integer recommendedArticleId = null;

    TextView textViewTotalArticlesCompleted;
    ListView listViewRecommendedArticle;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase User
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Bind UI elements
        textViewTotalArticlesCompleted = view.findViewById(R.id.home_total_articles_value);
        listViewRecommendedArticle = view.findViewById(R.id.home_listView_recommended_article);

        // Setup listener for click on listView
        listViewRecommendedArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get article object of selected item
                Article selectedArticle = (Article) listViewRecommendedArticle.getItemAtPosition(position);

                // Redirect user to article page of selected item
                openArticlePage(selectedArticle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Setup query for number of articles
        Query query = FirebaseFirestore.getInstance().collection("articles");
        AggregateQuery countQuery = query.count();

        countQuery.get(AggregateSource.SERVER)
                .addOnSuccessListener(new OnSuccessListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                        // Query ran successfully
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
                                        // Map data to User object
                                        User user = documentSnapshot.toObject(User.class);

                                        // Retrieve number of completed articles
                                        Integer numberOfArticlesCompleted = user.getArticlesCompleted().size();
                                        Long totalNumberOfArticles = aggregateQuerySnapshot.getCount();

                                        // Update UI
                                        textViewTotalArticlesCompleted.setText(numberOfArticlesCompleted + "/" + totalNumberOfArticles);

                                        // Find recommended article id
                                        findRecommendedArticleId(user, totalNumberOfArticles);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Query failed to run
                        Toast.makeText(getActivity(), "Data retrieval failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void findRecommendedArticleId(User user, Long totalNumberOfArticles) {
        // Find ID for recommended article
        for (int i = 1; i < totalNumberOfArticles; i++) {
            if (!user.getArticlesCompleted().contains(i)) {
                recommendedArticleId = i;

                // Break out of the loop once a recommended article is found
                break;
            }
        }

        // If the user has completed all articles, just recommend article 1 for the time being
        if (user.getArticlesCompleted().contains(recommendedArticleId) || recommendedArticleId == null) {
            recommendedArticleId = 1;
        }

        // Retrieve articles from database
        CollectionReference articles = FirebaseFirestore.getInstance().collection("articles");

        articles.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Data retrieval successful

                        // Find recommended article in list
                        // Map Articles to ArrayList of Articles
                        ArrayList<Article> articlesToShow = new ArrayList<>();

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Article article = document.toObject(Article.class);

                            // Check if the article's id is the same as the recommended article
                            if (Objects.equals(article.getArticleId(), recommendedArticleId)) {
                                articlesToShow.add(article);

                                // Create and setup ListViewAdapter
                                ArticlesListViewAdapter adapter = new ArticlesListViewAdapter(getContext(), articlesToShow, user.getArticlesCompleted());
                                listViewRecommendedArticle.setAdapter(adapter);

                                return;
                            }
                        }
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
    private void openArticlePage(Article selectedArticle) {
        Intent intent = new Intent(getContext(), articlePage.class);
        intent.putExtra("article", selectedArticle);
        startActivity(intent);
    }
}