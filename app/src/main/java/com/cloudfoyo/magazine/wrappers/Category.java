package com.cloudfoyo.magazine.wrappers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nilesh on 15/12/15.
 */
public class Category implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(categoryId);
            dest.writeString(name);
            dest.writeString(description);
            dest.writeString(imageUrl);
            dest.writeString(createdOn);
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>(){


        @Override
        public Category createFromParcel(Parcel source) {
            int id = source.readInt();
            String  name    = source.readString(),
                    desc    = source.readString(),
                    image   = source.readString(),
                    date    = source.readString();

            return new Category(id, name, desc,image, date);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
