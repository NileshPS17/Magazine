package com.cloudfoyo.magazine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudfoyo.magazine.extras.AsyncArticleLoader;
import com.cloudfoyo.magazine.extras.ListItemArticleAdapter;
import com.cloudfoyo.magazine.extras.Utility;
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

    private ProgressBar progressBar;

    private TextView noArticles;

    Handler handler;

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
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setTitle(c.getName());


        articlesListView = (ListView)findViewById(R.id.articles_listView);
        categoryImage = (ImageView)findViewById(R.id.category_image);

        progressBar = (ProgressBar)findViewById(R.id.progress);
        noArticles = (TextView)findViewById(R.id.noDataTextView);
        categoryImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                Picasso.with(getBaseContext()).load(c.getImageUrl()).resize(categoryImage.getMeasuredHeight(), categoryImage.getMeasuredWidth()).into(categoryImage);


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
                intent.putExtra(ViewArticleActivity.ACTION_ARTICLE, (Article) adapter.getItem(position));
                startActivity(intent);
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what)
                {
                    case Utility.SHOW_PROGRESS:
                        animateShowProgress();
                        break;

                    case Utility.NO_RESULT:
                        animateNoResult();
                        break;

                    case Utility.HIDE_PROGRESS:
                        animateShowList();
                        break;

                    default:
                        //Do nothing

                }
                return true;
            }
        });

        populateListView();




    }



    public void animateNoResult()
    {
        articlesListView.setVisibility(View.GONE);
        Utility.crossFadeViews(progressBar, noArticles);

    }

    public void animateShowProgress()
    {
        noArticles.setVisibility(View.GONE);
        articlesListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void animateShowList()
    {
        noArticles.setVisibility(View.GONE);
        Utility.crossFadeViews(progressBar, articlesListView);

    }




    public void populateListView()
    {
        if(asyncTask != null)
        {
            asyncTask.cancel(true);
        }
        else
        {
            try
            {
                asyncTask = new AsyncArticleLoader(this, adapter, false, false, handler);
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




   /**  class AsyncArticleLoader extends AsyncTask<URL, Article, Void>
    {


        private  final String LOG_TAG = AsyncArticleLoader.class.getSimpleName();

        private boolean fetchContent = false;
        private DynamicAdapterInterface<Article> adapter;
        private Context context;
        private boolean isRequestedTypeLatest = true;
        private Handler handler;
        private Message msg = new Message();
        public AsyncArticleLoader(Context c, DynamicAdapterInterface<Article> adapter,boolean fetchContent, boolean isRequestedTypeLatest,Handler handler ) {
            this.adapter = adapter;
            this.context = c;
            this.fetchContent = fetchContent;
            this.isRequestedTypeLatest = isRequestedTypeLatest;
            this.handler = handler;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.clearItems();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    animateShowProgress();
                }
            });
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
                    if(obj.getBoolean(context.getString(R.string.json_error)))
                    {
                        throw new Exception(context.getString(R.string.server_error) + "\t Error => True");
                    }
                    else
                    {

                        if(isRequestedTypeLatest) {
                            getLatestArticles(obj);
                        }
                        else // Then it is obviously fetch articles by category
                        {
                            getArticlesByCategory(obj);
                        }

                    }
                }
                else
                {
                    throw new Exception(context.getString(R.string.server_error) + "\tBad Response code" +responseCode);
                }

            }catch(Exception e)
            {
                this.cancel(true);
                Log.e(LOG_TAG, e.getMessage());
            }

            return null;
        }



        public void getLatestArticles(JSONObject obj) throws Exception
        {

            JSONArray array = obj.getJSONArray("articles");
            int n = array.length();
            int i;
            for(i=0; (i<n && !isCancelled()); ++i) {
                if(i==0 && handler!= null)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                                animateShowList();
                        }
                    });
                }
                JSONObject object = array.getJSONObject(i);
                Article article = new Article(object.getInt(context.getString(R.string.art_id)),
                        object.getInt(context.getString(R.string.art_cat_id)),
                        object.getString(context.getString(R.string.cat_name)),
                        object.getString(context.getString(R.string.art_title)),
                        object.getString(context.getString(R.string.art_author)),
                        object.getString(context.getString(R.string.art_image)),
                        object.getString(context.getString(R.string.art_date)),
                        (fetchContent ? object.getString(context.getString(R.string.art_content)) : ""));
                publishProgress(article);

            }


            if(i==0 && handler!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        animateNoResult();
                    }
                });
            }

        }



        public void getArticlesByCategory(JSONObject obj1) throws Exception
        {

            JSONObject c = obj1.getJSONArray("category").getJSONObject(0);
            Category category = new Category(c.getInt("cat_id"),
                    c.getString("category_name"),
                    c.getString(context.getString(R.string.cat_description)),
                    c.getString(context.getString(R.string.cat_image)),
                    c.getString("createdAt"));

            JSONArray array = obj1.getJSONArray("articles");
            int n = array.length();
            int i;
            for(i=0; (i<n && !isCancelled()); ++i)
            {
                if(i==0 && handler!= null)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            animateShowList();
                        }
                    });
                }

                JSONObject obj = array.getJSONObject(i);
                Article article = new Article(obj.getInt(context.getString(R.string.art_id)) ,
                        category.getCategoryId() ,
                        category.getName() ,
                        obj.getString(context.getString(R.string.art_title)),
                        obj.getString(context.getString(R.string.art_author)) ,
                        obj.getString(context.getString(R.string.art_image)),
                        obj.getString(context.getString(R.string.art_date)),
                        (fetchContent)?obj.getString(context.getString(R.string.art_content)):"");

                publishProgress(article);


            }


            if(i==0 && handler!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        animateNoResult();
                    }
                });
            }

        }


        @Override
        protected void onCancelled(Void aVoid) {

            adapter.clearItems();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    animateNoResult();
                }
            });

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Article... values) {
            super.onProgressUpdate(values);
            adapter.addItem(values[0]);
        }

    }


**/

}


