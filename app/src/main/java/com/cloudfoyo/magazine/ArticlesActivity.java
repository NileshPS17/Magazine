package com.cloudfoyo.magazine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nilesh on 24/11/15.
 */
public class ArticlesActivity extends MagazineAppCompatActivity {

    private ListView articlesListView;
    private ArrayList<ListItem> list = new ArrayList<ListItem>();
    Toolbar t1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        t1=(Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(t1);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Category Name");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


        articlesListView = (ListView)findViewById(R.id.articles_listView);

        populateList(); // TODO := For testing purposes only

        ListItemArticleAdapter adapter = new ListItemArticleAdapter();

        articlesListView.setAdapter(adapter);
       // articlesListView.addHeaderView(getLayoutInflater().inflate(R.layout.article_list_header, null), null, false);
        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO := Launch ViewArticleActivity with appropriate Intent Extras

                startActivity(new Intent(ArticlesActivity.this, ViewArticleActivity.class));
            }
        });

    }

    public void back(View view){
        this.finish();
    }
    public void populateList()
    {

        list.add(new ListItem(android.R.color.holo_red_light, "Red", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(android.R.color.holo_blue_dark, "Blue", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(android.R.color.holo_orange_dark, "Orange", "Education", "dd/mm/yyyy"));
        list.add(new ListItem(android.R.color.holo_green_dark, "Green", "Fun", "dd/mm/yyyy"));
        list.add(new ListItem(android.R.color.black, "Black", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(android.R.color.holo_red_light, "Red", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(android.R.color.holo_blue_dark, "Blue", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(android.R.color.holo_orange_dark, "Orange", "Education", "dd/mm/yyyy"));

    }

    class ListItemArticleAdapter extends BaseAdapter
    {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                convertView = getLayoutInflater().inflate(R.layout.home_list_item, parent, false);

            }

            ListItem item = list.get(position);
            TextView tv = (TextView)convertView.findViewById(R.id.home_list_item_category);
            tv.setText(item.category);
            tv = (TextView) convertView.findViewById(R.id.home_list_item_date);
            tv.setText(item.date);
            tv = (TextView)convertView.findViewById(R.id.home_list_item_title);
            tv.setText(item.title);
            View v = convertView.findViewById(R.id.home_list_item_articleImage);
            v.setBackgroundResource(item.color);
            return convertView;


        }
    }


    class ListItem
    {
        public int color = R.drawable.playboy;
        public String title, author = "Author" , date = "dd/mm/yyyy", category;

        public ListItem(int color, String title, String category, String date) {
            this.title = "Title";
            this.category = category;
            this.author="Author";
            this.date="dd/mm/yyyy";
        }
    }

}
