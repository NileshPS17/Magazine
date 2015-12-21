package com.cloudfoyo.magazine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudfoyo.magazine.extras.AsyncArticleLoader;
import com.cloudfoyo.magazine.extras.DynamicAdapterInterface;
import com.cloudfoyo.magazine.wrappers.Article;
import com.cloudfoyo.magazine.wrappers.Category;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ArticlesActivity extends MagazineAppCompatActivity {

private static final String LOG_TAG = ArticlesActivity.class.getSimpleName();

    private ListView articlesListView;
    Toolbar t1;
    //int[] images={R.drawable.cheese_1,R.drawable.cheese_2,R.drawable.cheese_3,R.drawable.cheese_4,R.drawable.cheese_5};
    private ListItemArticleAdapter adapter;

    private AsyncArticleLoader asyncTask = null;

    private Category c;


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
        ImageView categoryImage = (ImageView)findViewById(R.id.category_image);

        Picasso.with(this).load("http://192.168.43.66/img/6.jpg").into(categoryImage); // TODO := For testing purposes only



        adapter = new ListItemArticleAdapter();

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
                asyncTask.execute(new URL(getString(R.string.url_articles_by_category) + "/" + c.getCategoryId() + "/articles"));
            }
            catch (MalformedURLException e)
            {
                    //That shouldn't happen :-(
                    Log.d(LOG_TAG, e.getMessage());
                    asyncTask = null;
            }
        }

    }

    class ListItemArticleAdapter extends BaseAdapter implements DynamicAdapterInterface<Article>
    {
        ArrayList<Article> articlesList = new ArrayList<Article>();

        @Override
        public int getCount() {
            return articlesList.size();
        }

        @Override
        public Object getItem(int position) {
            return articlesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public void addItem(Article item) {
            articlesList.add(item);
            notifyDataSetChanged();
        }

        @Override
        public void clearItems() {
            articlesList.clear();
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                convertView = getLayoutInflater().inflate(R.layout.home_list_item, parent, false);

            }

            Article article = articlesList.get(position);
            TextView tv = (TextView)convertView.findViewById(R.id.home_list_item_category);
            tv.setText(article.getCategoryName());
            tv = (TextView) convertView.findViewById(R.id.home_list_item_date);
            tv.setText(article.getDate());
            tv = (TextView)convertView.findViewById(R.id.home_list_item_title);
            tv.setText(article.getTitle());
            tv = (TextView)convertView.findViewById(R.id.home_list_item_author);
            tv.setText(article.getAuthor());
            ImageView image = (ImageView)convertView.findViewById(R.id.home_list_item_articleImage);
            Picasso.with(ArticlesActivity.this).load(/**article.getImageUrl() **/"http://10.42.0.1/img/6.jpg").placeholder(R.drawable.img_loading).error(R.drawable.img_loading).into(image);
            return convertView;
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


