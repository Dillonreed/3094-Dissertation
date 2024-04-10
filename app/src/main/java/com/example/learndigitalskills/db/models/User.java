package com.example.learndigitalskills.db.models;

import java.util.ArrayList;

public class User {
    private String userId;
    private String username;
    private ArrayList<Integer> articlesCompleted = new ArrayList<>();

    public User() {
    }

    public User(String userId, String username, ArrayList<Integer> articlesCompleted) {
        this.userId = userId;
        this.username = username;
        this.articlesCompleted = articlesCompleted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Integer> getArticlesCompleted() {
        return articlesCompleted;
    }

    public void setArticlesCompleted(ArrayList<Integer> articlesCompleted) {
        this.articlesCompleted = articlesCompleted;
    }

    public void addCompletedArticle(Integer articleId) {
        this.articlesCompleted.add(articleId);
    }
}
