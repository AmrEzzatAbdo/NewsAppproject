package com.example.amrez.newsappproject;

/**
 * Created by amrez on 10/20/2017.
 */

public class NewsObjects {
    private final String articleTitle;
    private final String sectionName;
    private final String authorName;
    private final String date;
    private final String previewLink;

    public NewsObjects(String articleTitle, String sectionName, String authorName, String date, String previewLink) {
        this.articleTitle = articleTitle;
        this.sectionName = sectionName;
        this.authorName = authorName;
        this.date = date;
        this.previewLink = previewLink;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDate() {
        return date;
    }

    public String getPreviewLink() {
        return previewLink;
    }
}
