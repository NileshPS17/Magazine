package com.cloudfoyo.magazine;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageViewActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);



        TouchImageView img = (TouchImageView) findViewById(R.id.iv);
       // img.setImageResource(R.drawable.arathysudheer);
        img.setMaxZoom(4f);
        iv=(ImageView)findViewById(R.id.iv);
        if(Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(Color.BLACK);

        String imageurl = getIntent().getStringExtra("image");
        Picasso.with(ImageViewActivity.this).load(imageurl).into(img);


        findViewById(R.id.back).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
