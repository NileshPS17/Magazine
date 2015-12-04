package com.cloudfoyo.magazine.wrappers;

import com.cloudfoyo.magazine.R;

/**
 * Created by nilesh on 4/12/15.
 */
public class Article {

    private String title, heading, author, category, content;
    private int image = R.drawable.playboy;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Article(String heading, String title, String author, String category, String content, int image) {
        this.heading = heading;
        this.title = title;
        this.author = author;
        this.category = category;
        this.content = content;
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
