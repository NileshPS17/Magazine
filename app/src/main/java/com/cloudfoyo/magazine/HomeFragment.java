package com.cloudfoyo.magazine;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudfoyo.magazine.extras.ActivityPingListener;
import com.cloudfoyo.magazine.extras.AsyncArticleLoader;
import com.cloudfoyo.magazine.extras.ListItemArticleAdapter;
import com.cloudfoyo.magazine.extras.Utility;
import com.cloudfoyo.magazine.wrappers.Article;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ActivityPingListener, AdapterView.OnItemClickListener {

    private static final String LOG_TAG = HomeFragment.class.getSimpleName();



    private TextView noArticles;
    private ProgressBar progressBar;
    private View headerLogo;

    private ListView recentUpdates;
    private Handler handler;

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
        return view;
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
            Utility.fadeOutView(progressBar);

        }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recentUpdates = (ListView)view.findViewById(R.id.home_recentUpdates);

        headerLogo=LayoutInflater.from(getContext()).inflate(R.layout.home_list_item_header,null,false);
        recentUpdates.addHeaderView(headerLogo, null, false);
        adapter = new ListItemArticleAdapter(getContext());
        recentUpdates.setAdapter(adapter);
        recentUpdates.setOnItemClickListener(this);
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        noArticles = (TextView)view.findViewById(R.id.noDataTextView);


        reloadData();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(getActivity(), ViewArticleActivity.class);
        intent.putExtra(ViewArticleActivity.ACTION_ARTICLE, (Article) adapter.getItem(position-1));
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    public void reloadData()
    {
        if(asyncTask != null)
        {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        try {
            asyncTask = new AsyncArticleLoader(getContext(), adapter, false, true, handler);
            asyncTask.execute(new URL(getString(R.string.base_url)+"article/latest"));

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
            reloadData();
        }
    }



   /** public class ImageSliderAdapter extends PagerAdapter{
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

**/


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}


