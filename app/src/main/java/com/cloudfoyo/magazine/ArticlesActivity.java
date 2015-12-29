package com.cloudfoyo.magazine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.cloudfoyo.magazine.extras.AsyncArticleLoader;
import com.cloudfoyo.magazine.extras.ListItemArticleAdapter;
import com.cloudfoyo.magazine.wrappers.Article;
import com.cloudfoyo.magazine.wrappers.Category;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class ArticlesActivity extends MagazineAppCompatActivity {

private static final String LOG_TAG = ArticlesActivity.class.getSimpleName();

    private ListView articlesListView;
    Toolbar t1;
    //int[] images={R.drawable.cheese_1,R.drawable.cheese_2,R.drawable.cheese_3,R.drawable.cheese_4,R.drawable.cheese_5};
    private ListItemArticleAdapter adapter;

    private AsyncArticleLoader asyncTask = null;

    private Category c;

    ImageView categoryImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        t1=(Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(t1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        c = intent.getParcelableExtra("category");

        if(c == null)
        {
            Log.e(LOG_TAG, "Category parcelable is null");
            this.finish();
        }
        //c.setCategoryId(9);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setTitle(c.getName());

        articlesListView = (ListView)findViewById(R.id.articles_listView);
        categoryImage = (ImageView)findViewById(R.id.category_image);

        categoryImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                Picasso.with(getBaseContext()).load(getString(R.string.url_host)+"img/3.jpg").resize(categoryImage.getMeasuredHeight(), categoryImage.getMeasuredWidth()).into(categoryImage); // TODO := For testing purposes only


                return true;
            }
        });


        adapter = new ListItemArticleAdapter(this);

        articlesListView.setAdapter(adapter);
        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO := Launch ViewArticleActivity with appropriate Intent Extras
                Intent intent = new Intent(ArticlesActivity.this, ViewArticleActivity.class);
                intent.putExtra(ViewArticleActivity.ACTION_ARTICLE, (Article)adapter.getItem(position));
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        if(asyncTask != null)
        {
            asyncTask.cancel(true);
        }
        else
        {
            try
            {
                asyncTask = new AsyncArticleLoader(this, adapter, false);
                asyncTask.execute(new URL(getString(R.string.base_url) + "categories/" + c.getCategoryId() + "/articles"));
            }
            catch (MalformedURLException e)
            {
                    //That shouldn't happen :-(
                    Log.d(LOG_TAG, e.getMessage());
                    asyncTask = null;
            }
        }

    }




    @Override
    protected void onStop() {
        super.onStop();

        if(asyncTask != null)
        {
            asyncTask.cancel(true);
            asyncTask = null;
        }


    }


}


