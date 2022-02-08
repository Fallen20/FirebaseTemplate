package com.example.firebasetemplate;

public class Posts {

    private String content;
    private String authorName;
    private String datePost;

    public Posts(){}
    public Posts(String content, String authorName, String datePost) {
        this.content = content;
        this.authorName = authorName;
        this.datePost = datePost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
