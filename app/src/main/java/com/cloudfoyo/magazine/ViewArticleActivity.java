package com.cloudfoyo.magazine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cloudfoyo.magazine.extras.AsyncArticleLoader;
import com.cloudfoyo.magazine.extras.DynamicAdapterInterface;
import com.cloudfoyo.magazine.wrappers.Article;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import se.emilsjolander.flipview.FlipView;

public class ViewArticleActivity extends MagazineAppCompatActivity {

    private static final String LOG_TAG = ViewArticleActivity.class.getSimpleName();

    public static final String ACTION_ARTICLE = "com.cloudfoyo.magazine.ViewArticleActivity";

    Toolbar t1,t2;
    ImageButton imageButton;

   /** String text="To shed weight, Cooper jettisons himself and TARS into the black hole, " +
            "so that Amelia and CASE can complete the journey. Cooper and TARS plunge into " +
            "the black hole, but emerge in a tesseract, which appears as a stream of bookshelves;" +
            " with portals that look out into Murphy's bedroom at different times in her life. " +
            "Cooper realizes that the tesseract and wormhole were created by fifth dimensional " +
            "beings from the future to enable him to communicate with Murphy through gravitational " +
            "waves, and that he is her ghost. He relays quantum data that TARS collected from the " +
            "black hole in Morse code by manipulating the second hand of a watch he gave to Murphy " +
            "before he left. Murphy uses the quantum data to solve the remaining gravitational " +
            "equation, enabling Plan A.Cooper emerges from the wormhole and is rescued by the crew" +
            " of a space habitat orbiting Saturn. Aboard, he reunites with Murphy, now elderly and " +
            "near death. After sharing one last goodbye, Cooper, along with TARS, leaves the habitat" +
            " to rejoin Amelia, who is with CASE on Edmunds' Planet, which was found to be habitable."; **/


    private   FlipView flipView;

    private  int categoryId = -1;
    private int articleId = -1;
    private AsyncArticleLoader asyncTask = null;

    private FlipViewAdapter adapter;

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


        Intent intent = getIntent();

        if(intent!=null && intent.getParcelableExtra(ACTION_ARTICLE) != null)
        {
            Article article = (Article)intent.getParcelableExtra(ACTION_ARTICLE);
            categoryId = article.getCategoryId();
            articleId  = article.getArticleId();
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
            asyncTask = new AsyncArticleLoader(this, adapter, true);
            asyncTask.execute(new URL(getString(R.string.base_url)+"/categories/"+ categoryId + "/articles"));

        }catch (MalformedURLException e)
        {
            Log.e(LOG_TAG, e.getMessage());
            asyncTask = null;
        }
    }

    class FlipViewAdapter extends BaseAdapter  implements DynamicAdapterInterface<Article>, View.OnClickListener, CompoundButton.OnCheckedChangeListener{

        public LinkedList<Article> list = new LinkedList<Article>();

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

            ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();

                convertView = getLayoutInflater().inflate(R.layout.article_flip_view, parent, false);
                holder.content=(TextView)convertView.findViewById(R.id.content);
                holder.heading=(TextView)convertView.findViewById(R.id.heading);
                holder.category=(TextView)convertView.findViewById(R.id.category);
                holder.author=(TextView)convertView.findViewById(R.id.author);
                holder.iv=(ImageView)convertView.findViewById(R.id.iv);
                holder.collapsingToolbarLayout = (CollapsingToolbarLayout)convertView.findViewById(R.id.collapsing_toolbar);
                holder.toggleButton = (ToggleButton)convertView.findViewById(R.id.toggleButton);
                holder.shareButton = (ImageButton)convertView.findViewById(R.id.share);
                convertView.setTag(holder);

            }
            else {

                holder = (ViewHolder)convertView.getTag();
            }

            Article article = list.get(position);
            holder.author.setText(article.getAuthor());
            holder.category.setText(article.getCategoryName());
            holder.content.setText(article.getContent());
            holder.heading.setText(article.getTitle());
            Picasso.with(ViewArticleActivity.this).load(/** article.getImageUrl() **/ getString(R.string.url_host)+"img/13.jpg").placeholder(R.drawable.img_loading).error(R.drawable.img_loading).into(holder.iv);
            holder.collapsingToolbarLayout.setTitle("");
            holder.toggleButton.setTag(new Integer(position));
            holder.shareButton.setTag(new Integer(position));
            holder.toggleButton.setOnCheckedChangeListener(this);
            holder.shareButton.setOnClickListener(this);
            convertView.findViewById(R.id.scrollview).setScrollX(0);
            return convertView;

        }

         class ViewHolder {

             TextView content, author,category, heading;
             ImageView iv;
             CollapsingToolbarLayout collapsingToolbarLayout;
             ToggleButton toggleButton;
             ImageButton shareButton;
        }

        @Override
        public void onClick(View v) {
            Article a = list.get((Integer)v.getTag());
            if(v.getId() == R.id.share) // Share the article
            {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, a.getTitle() + "\n\n" + a.getContent());
                    startActivity(Intent.createChooser(intent, "Select an app : " ));

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

