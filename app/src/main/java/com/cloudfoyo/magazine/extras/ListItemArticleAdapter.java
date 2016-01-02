package com.cloudfoyo.magazine.extras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudfoyo.magazine.R;
import com.cloudfoyo.magazine.wrappers.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nilesh on 28/12/15.
 */
public class ListItemArticleAdapter extends BaseAdapter implements DynamicAdapterInterface<Article>
{



    private Context c;
    private ArrayList<Article> articlesList = new ArrayList<Article>();

    LayoutInflater inflater;
    public ListItemArticleAdapter(Context c) {
        this.c = c;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

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

            convertView = inflater.inflate(R.layout.home_list_item, parent, false);

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
        Picasso.with(c).load(article.getImageUrl()).placeholder(R.drawable.img_loading).error(R.drawable.img_loading).into(image);
        return convertView;
    }
}
