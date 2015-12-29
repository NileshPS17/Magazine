package com.cloudfoyo.magazine;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudfoyo.magazine.extras.ActivityPingListener;
import com.cloudfoyo.magazine.extras.AsyncArticleLoader;
import com.cloudfoyo.magazine.extras.ListItemArticleAdapter;
import com.cloudfoyo.magazine.wrappers.Article;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ActivityPingListener, AdapterView.OnItemClickListener {

    private static final String LOG_TAG = HomeFragment.class.getSimpleName();


    public static final String INTENT_RECENT_LIST = "com.cloudfoyo.magazine.recent_list";

    private TextView viewMore;
    private View headerView, footerView;

    private ListView recentUpdates;

    private ListItemArticleAdapter adapter;

    private AsyncArticleLoader asyncTask = null;

    //int[] images={R.drawable.cheese_1,R.drawable.cheese_2,R.drawable.cheese_3,R.drawable.cheese_4,R.drawable.cheese_5};

    public HomeFragment() {
        // Required empty public constructor



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        setRetainInstance(true);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recentUpdates = (ListView)view.findViewById(R.id.home_recentUpdates);

        headerView=LayoutInflater.from(getContext()).inflate(R.layout.pager,null);

        footerView = LayoutInflater.from(getContext()).inflate(R.layout.home_list_item_footer, null);
        adapter = new ListItemArticleAdapter(getContext());
        populateListView();
        recentUpdates.setAdapter(adapter);
        ViewPager viewPager=(ViewPager)headerView.findViewById(R.id.viewPager);
        PagerAdapter adapter=new ImageSliderAdapter(getActivity(),viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewMore = (TextView) footerView.findViewById(R.id.home_list_item_footer_more);
        viewMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO : Load more posts into 'recent Updates' here
                Toast.makeText(getContext(), "Loading..", Toast.LENGTH_SHORT).show();
            }
        });


        recentUpdates.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(getActivity(), ViewArticleActivity.class);
        intent.putExtra(Intent.EXTRA_SUBJECT, INTENT_RECENT_LIST);
        intent.putExtra(ViewArticleActivity.ACTION_ARTICLE, (Article)adapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(asyncTask != null)
        {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        try {
            asyncTask = new AsyncArticleLoader(getContext(), adapter, false);
            asyncTask.execute(new URL(getString(R.string.base_url)+"recent"));

        }catch (MalformedURLException e)
        {
            Log.e(LOG_TAG, "Malformed URL : "+e.getMessage());
            asyncTask = null;
        }
    }

    @Override
    public void onActivityPing(Intent intent) {
        if(intent.getAction().equals(MainActivity.FULL_RELOAD))
        {
            //Clear all existing data and reload it
            if(! isNetworkAvailable())
            {
                recentUpdates.removeHeaderView(headerView);
                recentUpdates.removeFooterView(footerView);
            }
        }
    }

    public void populateListView()
    {

        recentUpdates.addHeaderView(headerView, null, false);
       // recentUpdates.addFooterView(footerView, null, false);

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
            return 0;
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




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}


