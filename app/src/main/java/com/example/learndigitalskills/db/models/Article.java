package com.example.learndigitalskills.db.models;

public class Article {
    private Integer articleId;
    private String title;
    private String shortDescription;
    private String content;
    private String videoLink;

    public Article(Integer articleId, String title, String shortDescription, String content, String videoLink) {
        this.articleId = articleId;
        this.title = title;
        this.shortDescription = shortDescription;
        this.content = content;
        this.videoLink = videoLink;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
