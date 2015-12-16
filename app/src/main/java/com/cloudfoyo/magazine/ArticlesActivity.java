package com.cloudfoyo.magazine;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

import com.cloudfoyo.magazine.extras.DynamicAdapterInterface;
import com.cloudfoyo.magazine.wrappers.Article;
import com.cloudfoyo.magazine.wrappers.Category;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class ArticlesActivity extends MagazineAppCompatActivity {

private static final String LOG_TAG = ArticlesActivity.class.getSimpleName();

    private ListView articlesListView;
    Toolbar t1;
    //int[] images={R.drawable.cheese_1,R.drawable.cheese_2,R.drawable.cheese_3,R.drawable.cheese_4,R.drawable.cheese_5};
    private ListItemArticleAdapter adapter;

    private LoadArticlesTask asyncTask = null;

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
        c.setCategoryId(9);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setTitle(c.getName().toUpperCase());

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
                intent.putExtra("article", ((Article)adapter.getItem(position)));
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
                asyncTask = new LoadArticlesTask();
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
            Picasso.with(ArticlesActivity.this).load(/**article.getImageUrl() **/"http://10.42.0.1/img/14.jpg").into(image);
            return convertView;


        }
    }


    class LoadArticlesTask extends AsyncTask<URL, Article, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.clearItems();
        }


        @Override
        protected Void doInBackground(URL... params) {
            try
            {
                HttpURLConnection connection = (HttpURLConnection)params[0].openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if(responseCode == 200 || responseCode == 201) //Proceed
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line ="";
                    StringBuilder builder = new StringBuilder("");
                    while((line =  br.readLine())!=null)
                    {
                        builder.append(line);
                    }
                    connection.disconnect();
                    br.close();
                    JSONObject obj = new JSONObject(builder.toString());
                    if(obj.getBoolean(getString(R.string.json_error)))
                    {
                        throw new Exception(getString(R.string.server_error) + "\t Error => True");
                    }
                    else
                    {
                        JSONArray array = obj.getJSONArray("articles");
                        int n = array.length();
                        for(int  i=0; (i<n && !isCancelled()); ++i)
                        {
                            JSONObject object = array.getJSONObject(i);
                            Article article = new Article(object.getInt(getString(R.string.art_id)),
                                                          object.getInt(getString(R.string.art_cat_id)),
                                                          object.getString(getString(R.string.cat_name)),
                                                          object.getString(getString(R.string.art_title)),
                                                          object.getString(getString(R.string.art_author)),
                                                          object.getString(getString(R.string.art_image)),
                                                          object.getString(getString(R.string.art_date)),
                                                          object.getString(getString(R.string.art_content)));
                            publishProgress(article);
                        }
                    }
                }
                else
                {
                    throw new Exception(getString(R.string.server_error) + "\tBad Response code" +responseCode);
                }

            }catch(Exception e)
            {
                    this.cancel(true);
                    Log.e(LOG_TAG, e.getMessage());
            }

            return null;
        }


        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            adapter.clearItems();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Do nothing
        }

        @Override
        protected void onProgressUpdate(Article... values) {
            super.onProgressUpdate(values);
            adapter.addItem(values[0]);
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

    ArrayDeque<String> a = new ArrayDeque<String>();

}


