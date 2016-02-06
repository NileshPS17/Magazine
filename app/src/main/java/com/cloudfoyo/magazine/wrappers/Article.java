package com.cloudfoyo.magazine.wrappers;


import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable
{
    private int articleId, categoryId;
    private String title="", author="", imageUrl="", date="", content="", categoryName="", videoUrl="";

    public Article(int articleId, int categoryId,String categoryName, String title, String author, String imageUrl, String date, String content) {
        this.articleId = articleId;
        this.categoryId = categoryId;
        this.title = title;
        this.categoryName = categoryName;
        this.author = author;
        this.imageUrl = imageUrl;
        this.date = date;
        this.content = content;
    }


   public Article(int articleId, int categoryId,String categoryName, String title, String author, String imageUrl, String date, String content, String videoUrl) {
        this.articleId = articleId;
        this.categoryId = categoryId;
        this.title = title;
        this.categoryName = categoryName;
        this.author = author;
        this.imageUrl = imageUrl;
        this.date = date;
        this.content = content;
        this.videoUrl = videoUrl;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Article setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(articleId);
        dest.writeInt(categoryId);
        dest.writeString(categoryName);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(imageUrl);
        dest.writeString(date);
        dest.writeString(content);
    }


    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>()
    {

        @Override
        public Article createFromParcel(Parcel source) {
            int articleID = source.readInt();
            int cid      = source.readInt();
            String cname   = source.readString();
            String title = source.readString();
            String author = source.readString();
            String imageUrl = source.readString();
            String date = source.readString();
            String content = source.readString();
            return new Article(articleID, cid, cname, title, author, imageUrl, date,content);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
