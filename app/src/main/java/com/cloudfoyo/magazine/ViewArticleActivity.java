package com.cloudfoyo.magazine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewArticleActivity extends AppCompatActivity {

    Toolbar t1,t2;
    TextView heading,author,category,content,title;
    ImageView iv;
    ImageButton imageButton;
    String text="To shed weight, Cooper jettisons himself and TARS into the black hole, so that Amelia and CASE can complete the journey. Cooper and TARS plunge into the black hole, but emerge in a tesseract, which appears as a stream of bookshelves; with portals that look out into Murphy's bedroom at different times in her life. Cooper realizes that the tesseract and wormhole were created by fifth dimensional beings from the future to enable him to communicate with Murphy through gravitational waves, and that he is her ghost. He relays quantum data that TARS collected from the black hole in Morse code by manipulating the second hand of a watch he gave to Murphy before he left. Murphy uses the quantum data to solve the remaining gravitational equation, enabling Plan A.Cooper emerges from the wormhole and is rescued by the crew of a space habitat orbiting Saturn. Aboard, he reunites with Murphy, now elderly and near death. After sharing one last goodbye, Cooper, along with TARS, leaves the habitat to rejoin Amelia, who is with CASE on Edmunds' Planet, which was found to be habitable.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);
        content=(TextView)findViewById(R.id.content);
        heading=(TextView)findViewById(R.id.heading);
        category=(TextView)findViewById(R.id.category);
        author=(TextView)findViewById(R.id.author);
        title=(TextView)findViewById(R.id.title);
        iv=(ImageView)findViewById(R.id.iv);
        t1=(Toolbar)findViewById(R.id.toolbar1);
        t2=(Toolbar)findViewById(R.id.toolbar2);
        imageButton=(ImageButton)findViewById(R.id.share);
        setSupportActionBar(t1);
        content.setText(text);
        //content.setMovementMethod(new ScrollingMovementMethod());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                //  shareIntent.setType("image/png");
                shareIntent.setType("text/html");
                shareIntent.putExtra("Hello","World");
                startActivity(Intent.createChooser(shareIntent,"Share using"));
                // share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                //startActivity(Intent.createChooser(sharingIntent, "Share image using"));
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.back) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

