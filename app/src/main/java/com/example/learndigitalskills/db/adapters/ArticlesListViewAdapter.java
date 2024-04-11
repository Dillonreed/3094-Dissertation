package com.example.learndigitalskills.db.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learndigitalskills.R;
import com.example.learndigitalskills.db.models.Article;

import java.util.ArrayList;

public class ArticlesListViewAdapter extends ArrayAdapter<Article> {

    private ArrayList<Integer> completedArticles = new ArrayList<>();

    public ArticlesListViewAdapter (Context context, ArrayList<Article> articlesList, ArrayList<Integer> completedArticles) {
        super(context, 0, articlesList);

        // Add the list of articles that the user has completed
        this.completedArticles = completedArticles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Article article = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);
        }

        // Bind UI elements
        TextView textViewTitle = convertView.findViewById(R.id.article_item_title);
        TextView textViewTopic = convertView.findViewById(R.id.article_item_topic);
        TextView textViewShortDescription = convertView.findViewById(R.id.article_item_short_description);
        CheckBox checkBoxCompleted = convertView.findViewById(R.id.article_item_checkBox);

        // Set the data for each of the items
        textViewTitle.setText(article.getTitle());
        textViewTopic.setText(article.getTopic());
        textViewShortDescription.setText(article.getShortDescription());

        if (this.completedArticles.contains(article.getArticleId())) {
            checkBoxCompleted.setChecked(true);
        } else {
            checkBoxCompleted.setChecked(false);
        }

        return convertView;
    }
}
