package com.example.learndigitalskills.db.models;

import java.util.List;

public class User {
    private String userId;
    private String username;
    private List<Integer> articlesCompleted;

    public User() {
    }

    public User(String userId, String username, List<Integer> articlesCompleted) {
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

    public List<Integer> getArticlesCompleted() {
        return articlesCompleted;
    }

    public void setArticlesCompleted(List<Integer> articlesCompleted) {
        this.articlesCompleted = articlesCompleted;
    }

    public void addCompletedArticle(Integer articleId) {
        this.articlesCompleted.add(articleId);
    }
}
