package com.cloudfoyo.magazine.wrappers;

/**
 * Created by nilesh on 15/12/15.
 */
public class Category {

    private String name, description, createdOn, imageUrl;
    private int categoryId;

    public Category(int categoryId, String name, String description, String imageUrl,String createdOn) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
