package com.cloudfoyo.magazine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        iv=(ImageView)findViewById(R.id.iv);
        String imageurl=getIntent().getStringExtra("image");
        Picasso.with(ImageViewActivity.this).load(imageurl).into(iv);


    }
}
