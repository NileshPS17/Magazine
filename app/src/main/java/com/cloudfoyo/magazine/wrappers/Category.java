package com.cloudfoyo.magazine.wrappers;

/**
 * Created by nilesh on 15/12/15.
 */
public class Category {

    private String name, description, createdOn, categoryId;

    public Category(String name, String description, String createdOn, String categoryId) {
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.categoryId = categoryId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
