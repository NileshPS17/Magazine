package com.cloudfoyo.magazine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cloudfoyo.magazine.extras.AsyncArticleLoader;
import com.cloudfoyo.magazine.extras.DynamicAdapterInterface;
import com.cloudfoyo.magazine.extras.Utility;
import com.cloudfoyo.magazine.wrappers.Article;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import se.emilsjolander.flipview.FlipView;
public class ViewArticleActivity extends MagazineAppCompatActivity {

    private static final String LOG_TAG = ViewArticleActivity.class.getSimpleName();

    public static final String ACTION_ARTICLE = "com.cloudfoyo.magazine.ViewArticleActivity";

    public static final int RECOVERY_REQUEST = 1;

    Toolbar t1,t2;
    ImageButton imageButton;

    private   FlipView flipView;

    private  int categoryId = -1;
    private int articleId = -1;
    private AsyncArticleLoader asyncTask = null;

    private String categoryName;

    private FlipViewAdapter adapter;

    private Handler handler;

    private ProgressBar progressBar;

    private TextView noArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);



       // t1=(Toolbar)findViewById(R.id.toolbar1);
        //t2=(Toolbar)findViewById(R.id.toolbar2);
       // setSupportActionBar(t1);
      /*  imageButton=(ImageButton)findViewById(R.id.share);
        //content.setMovementMethod(new ScrollingMovementMethod());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                //  shareIntent.setType("image/png");
                shareIntent.setType("text/html");
                shareIntent.putExtra("Hello", "World");
                startActivity(Intent.createChooser(shareIntent, "Share using"));
                // share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                //startActivity(Intent.createChooser(sharingIntent, "Share image using"));
            }
        });
*/

        if(! isNetworkAvailable() )
        {
            Toast.makeText(getApplicationContext(), "No  Network Connection", Toast.LENGTH_SHORT).show();
            finish();
        }


        Intent intent = getIntent();

        if(intent!=null && intent.getParcelableExtra(ACTION_ARTICLE) != null)
        {
            Article article = (Article)intent.getParcelableExtra(ACTION_ARTICLE);
            categoryId = article.getCategoryId();
            articleId  = article.getArticleId();
            categoryName = article.getCategoryName();
            adapter = new FlipViewAdapter();
            flipView = (FlipView)findViewById(R.id.flip_view);
            flipView.setAdapter(adapter);
        }
        else
        {
            Log.e(LOG_TAG, "Intent | Intent.ACTION_ARTICLE is NULL. Detected illegal start of Activity");
            Toast.makeText(getApplicationContext(), "Something went wrong !" , Toast.LENGTH_SHORT).show();
            this.finish();
        }

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


        noArticles = (TextView)findViewById(R.id.noDataTextView);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        flipView.setVisibility(View.GONE);
    }


    public void animateNoResult()
    {
        //recentUpdates.setVisibility(View.GONE);
        Utility.crossFadeViews(progressBar, noArticles);

    }

    public void animateShowProgress()
    {
        noArticles.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void animateShowList()
    {
        noArticles.setVisibility(View.GONE);
        Utility.crossFadeViews(progressBar, flipView);

    }




    public void back(View view){
        this.finish();
    }



    @Override
    protected void onStart() {
        super.onStart();

        if(asyncTask != null)
        {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        try {
            asyncTask = new AsyncArticleLoader(this, adapter, true, false, handler);
            asyncTask.execute(new URL(getString(R.string.base_url)+"/categories/"+ categoryId + "/articles"));

        }catch (MalformedURLException e)
        {
            Log.e(LOG_TAG, e.getMessage());
            asyncTask = null;
        }
    }

    class FlipViewAdapter extends BaseAdapter  implements DynamicAdapterInterface<Article>, View.OnClickListener, CompoundButton.OnCheckedChangeListener{

        public LinkedList<Article> list = new LinkedList<Article>();
        private LayoutInflater inflater;




        public FlipViewAdapter()
        {
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public void addItem(Article item) {



            if( item.getArticleId() >= articleId )
            {
                list.add(item);
                notifyDataSetChanged();
            }

        }


        @Override
        public void clearItems() {

            list.clear();
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            TextView content, author,category, heading;
            ImageView iv;
            CollapsingToolbarLayout collapsingToolbarLayout;
            ToggleButton toggleButton;
            ImageButton shareButton;
            Button playButton;

            final Article article = list.get(position);


            if(convertView == null) {
                convertView = inflater.inflate(R.layout.article_flip_view, parent, false);
            }

            content=(TextView)convertView.findViewById(R.id.content);
            heading=(TextView)convertView.findViewById(R.id.heading);
            category=(TextView)convertView.findViewById(R.id.category);
            author=(TextView)convertView.findViewById(R.id.author);
            iv=(ImageView)convertView.findViewById(R.id.iv);
            collapsingToolbarLayout = (CollapsingToolbarLayout)convertView.findViewById(R.id.collapsing_toolbar);
            //holder.toggleButton = (ToggleButton)convertView.findViewById(R.id.toggleButton);
            shareButton = (ImageButton)convertView.findViewById(R.id.share);
            playButton  = (Button) convertView.findViewById(R.id.playerButton);
            playButton.setTag(new Integer(position));

            if(article.getVideoUrl().trim().equals("")) // Hide the button if no Video URL is available
                playButton.setVisibility(View.GONE);
            else
                playButton.setOnClickListener(this);

            author.setText(article.getAuthor());
            category.setText(article.getCategoryName());
            content.setText(article.getContent());
            heading.setText(article.getTitle());
            Picasso.with(ViewArticleActivity.this).load(article.getImageUrl()).placeholder(R.drawable.img_loading).error(R.drawable.img_loading).into(iv);
            collapsingToolbarLayout.setTitle(categoryName);
            //toggleButton.setTag(new Integer(position));
            shareButton.setTag(new Integer(position));
            //toggleButton.setOnCheckedChangeListener(this);
            shareButton.setOnClickListener(this);
            convertView.findViewById(R.id.scrollview).setScrollX(0);

            return convertView;

        }

         class ViewHolder {

             TextView content, author,category, heading;
             ImageView iv;
             CollapsingToolbarLayout collapsingToolbarLayout;
             ToggleButton toggleButton;
             ImageButton shareButton;
             YouTubeThumbnailView playerView;
        }




        @Override
        public void onClick(View v) {
            int id = v.getId();
            Article a = list.get((Integer) v.getTag());
            if(id == R.id.share) // Share the article
            {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, a.getTitle() +"\n\n" + a.getImageUrl() + "\n\n" +  a.getContent() + "\n\n" +"https://www.youtube.com?watch="+a.getVideoUrl());
                    startActivity(Intent.createChooser(intent, "Select an app : " ));

            }
            else if(id == R.id.playerButton)
            {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(ViewArticleActivity.this, getString(R.string.YOUTUBE_KEY),a.getVideoUrl(), 0, true, false);
                startActivity(intent);
            }

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getId() == R.id.toggleButton)
            {
                Article a = list.get((Integer)buttonView.getTag());
                if(isChecked) // LIke the article
                {

                }
                else // Unlike it
                {

                }

            }

        }
    }





}

