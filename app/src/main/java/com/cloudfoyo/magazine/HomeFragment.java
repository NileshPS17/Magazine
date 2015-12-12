package com.cloudfoyo.magazine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;


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
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView recentUpdates = (ListView)view.findViewById(R.id.home_recentUpdates);
        recentUpdates.setAdapter(new RecentUpdatesListAdapter());
        ViewPager viewPager=(ViewPager)view.findViewById(R.id.viewPager);
        PagerAdapter adapter=new ImageSliderAdapter(getActivity(),viewPager);
        /*View swipeview=LayoutInflater.from(getContext()).inflate(R.layout.home_list_item_header,null);
        recentUpdates.addHeaderView(swipeview,null,false); */

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
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
    public class ImageSliderAdapter extends PagerAdapter{
        Timer timer;
        TimerTask timerTask;
        int pos=1;
        android.os.Handler handler=new android.os.Handler();
        Context mContext;
        LayoutInflater inflater;
        private int[] pageIDsArray;
        private int count;
        ImageView imageView;
        ViewPager viewPager;
        int[] images={R.drawable.cheese_1,R.drawable.cheese_2,R.drawable.cheese_3,R.drawable.cheese_4,R.drawable.cheese_5};

        public ImageSliderAdapter(Context context,final ViewPager pager){
            mContext=context;
            viewPager=pager;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int actualNoOfIDs =images.length;
            count = actualNoOfIDs + 2;
            pageIDsArray = new int[count];
            for (int i = 0; i < actualNoOfIDs; i++) {
                pageIDsArray[i + 1] =images[i];
            }
            pageIDsArray[0] = images[actualNoOfIDs - 1];
            pageIDsArray[count - 1] = images[0];

            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    int pageCount = getCount();
                    if (position == 0){
                        pager.setCurrentItem(pageCount-2,false);
                    } else if (position ==pageCount-1){
                        pager.setCurrentItem(1,false);
                    }
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // TODO Auto-generated method stub
                }
            });
             startTimer();
            }

        public void startTimer(){

            timer=new Timer();
            runTimerTask();
            timer.scheduleAtFixedRate(timerTask,3000,3000);
        }

        public void runTimerTask(){

            timerTask=new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            increment();
                        }
                    });
                }
            };
        }
        public void increment(){
            pos++;
            if(pos==6){
                pos=1;
            }
            viewPager.setCurrentItem(pos);
        }
        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==(LinearLayout)object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView =inflater.inflate(R.layout.home_list_item_header, container, false);
             imageView = (ImageView) itemView.findViewById(R.id.imageView);
             imageView.setImageResource(pageIDsArray[position]);
            container.addView(itemView);
            return itemView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
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
            CardView cv=(CardView)convertView.findViewById(R.id.cv);
            cv.setPadding(4,4,4,4);
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
