package com.testapp.testapplication.posts;

import java.io.Serializable;

/**
 * Created by noosle on 29.03.2017.
 */

public class Post implements Serializable{

    private int postId;
    private int userId;
    private String title;
    private String body;

    public Post(int postId, int userId, String title, String body) {
        this.postId = postId;
        this.body = body;
        this.userId = userId;
        this.title = title;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
