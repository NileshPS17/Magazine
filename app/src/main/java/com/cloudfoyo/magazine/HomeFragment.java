package com.cloudfoyo.magazine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ArrayList<ListItem> list = new ArrayList<ListItem>();
    TextView viewMore;
    int[] images={R.drawable.cheese_1,R.drawable.cheese_2,R.drawable.cheese_3,R.drawable.cheese_4,R.drawable.cheese_5};
    public HomeFragment() {
        // Required empty public constructor

        list.add(new ListItem(R.drawable.cheese_1, "Red", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(R.drawable.cheese_2, "Blue", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(R.drawable.cheese_3, "Orange", "Education", "dd/mm/yyyy"));
        list.add(new ListItem(R.drawable.cheese_4, "Green", "Fun", "dd/mm/yyyy"));
        list.add(new ListItem(R.drawable.cheese_1, "Black", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(R.drawable.cheese_4, "Red", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(R.drawable.cheese_5, "Blue", "Entertainment", "dd/mm/yyyy"));
        list.add(new ListItem(R.drawable.cheese_1, "Orange", "Education", "dd/mm/yyyy"));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView recentUpdates = (ListView)view.findViewById(R.id.home_recentUpdates);
        recentUpdates.setAdapter(new RecentUpdatesListAdapter());
        recentUpdates.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.home_list_item_header, null), null, false);
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.home_list_item_footer, null);
        recentUpdates.addFooterView(footerView, null, false);
        viewMore = (TextView) footerView.findViewById(R.id.home_list_item_footer_more);
        viewMore.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO : Load more posts into 'recent Updates' here
                Toast.makeText(getContext(), "Loading..", Toast.LENGTH_SHORT).show();
            }
        });


        recentUpdates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO := Start the ViewArticleAcivity with appropriate Intent Flags
               startActivity(new Intent(getContext(), ViewArticleActivity.class));
            }
        });



    }

    class RecentUpdatesListAdapter extends BaseAdapter {


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
           if( convertView == null)
           {
               convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_list_item, parent, false);
           }

            ListItem item = list.get(position);
            TextView tv = (TextView)convertView.findViewById(R.id.home_list_item_category);
            tv.setText(item.category);
            tv = (TextView) convertView.findViewById(R.id.home_list_item_date);
            tv.setText(item.date);
            tv = (TextView)convertView.findViewById(R.id.home_list_item_title);
            tv.setText(item.title);
            ImageView v = (ImageView)convertView.findViewById(R.id.home_list_item_articleImage);
            v.setImageResource(item.color);
            return convertView;


        }
    }

    class ListItem
    {
        public int color; //TODO : Replace with drawable
        public String title, category, date;

        public ListItem(int color,  String title, String category, String date) {
            this.color = color;
            this.date = date;
            this.category = category;
            this.title = title;
        }
    }

}
