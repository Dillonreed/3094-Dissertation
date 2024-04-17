package com.example.learndigitalskills.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.learndigitalskills.R;
import com.example.learndigitalskills.db.models.Article;
import com.example.learndigitalskills.db.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class articlePage extends AppCompatActivity {

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private Article article;

    Toolbar articlePageToolbar;
    TextView textViewTitle, textViewContent;
    WebView webViewVideo;
    Button buttonComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_page);

        // Setup App Bar
        articlePageToolbar = findViewById(R.id.article_page_toolbar);
        articlePageToolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        setSupportActionBar(articlePageToolbar);

        // Configure App Bar
        ActionBar articlePageActionBar = getSupportActionBar();
        if (articlePageActionBar != null){
            // Setting title
            articlePageActionBar.setTitle(R.string.articles_title);

            // Customize back button
            articlePageActionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

            // Enable back button
            articlePageActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Get passed article
        article = (Article) getIntent().getSerializableExtra("article");

        // Bind UI elements
        textViewTitle = findViewById(R.id.article_text_view_title);
        textViewContent = findViewById(R.id.article_text_view_content);
        webViewVideo = findViewById(R.id.article_webView_video);
        buttonComplete = findViewById(R.id.article_button_complete);

        // Populate page with article information
        textViewTitle.setText(article.getTitle());

        // Use HTML to enable formatting of content
        textViewContent.setText(Html.fromHtml(article.getContent(), 1));

        webViewVideo.loadData(article.getVideoLink(), "text/html", "utf-8");
        webViewVideo.getSettings().setJavaScriptEnabled(true);
        webViewVideo.setWebChromeClient(new WebChromeClient());

        // Setup listener for buttons
        buttonComplete.setOnClickListener(v -> {
            markArticleComplete();
        });
    }

    private void markArticleComplete() {
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

                        // Add article id to completed articles list
                        user.addCompletedArticle(article.getArticleId());

                        // Update user document
                        document.update("articlesCompleted", user.getArticlesCompleted())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // Data updated successfully
                                        Toast.makeText(articlePage.this, "Article Marked As Complete", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Data failed to update
                                        Toast.makeText(articlePage.this, "Data update failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Data retrieval failed
                        Toast.makeText(articlePage.this, "Data retrieval failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static Intent getIntent(Context context){
        return new Intent(context, articlePage.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}