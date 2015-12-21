package com.cloudfoyo.magazine;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends MagazineAppCompatActivity {

    TabLayout tabLayout;
    private Toolbar toolbar;
    ViewPager viewPager;
    private int[]tab_icons={R.mipmap.home,R.mipmap.grid,R.mipmap.search,R.mipmap.about};

    private HomeFragment homeFragment = new HomeFragment();
    private GridFragment gridFragment = new GridFragment();

    public static final String FULL_RELOAD = "com.cloudfoyo.magazine.mainactivity.full_reload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        setupviewpager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0;i<tabLayout.getTabCount();i++){
            tabLayout.getTabAt(i).setIcon(tab_icons[i]);

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(! isNetworkAvailable())
        {
            showSnackBar();
        }
    }

    public void showSnackBar()
    {
        Snackbar.make(findViewById(R.id.mainActivityRoot), "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isNetworkAvailable()) {
                            //Do nothing
                            showSnackBar();
                        } else {

                        }
                    }
                }).setActionTextColor(Color.MAGENTA).show();
    }


    private void setupviewpager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(homeFragment,"Home");
        viewPagerAdapter.addFragment(gridFragment,"Categories");
        viewPagerAdapter.addFragment(new SearchFragment(),"Search");
        viewPagerAdapter.addFragment(new AboutFragment(), "About");
        viewPager.setAdapter(viewPagerAdapter);

    }
    class ViewPagerAdapter extends FragmentPagerAdapter{
        ArrayList<android.support.v4.app.Fragment> fragmentList=new ArrayList<android.support.v4.app.Fragment>();
        ArrayList<String> names=new ArrayList<String>();
        ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position){
            return fragmentList.get(position);
        }
        public int getCount(){
            return fragmentList.size();
        }
        public void addFragment(android.support.v4.app.Fragment fragment,String name){
            fragmentList.add(fragment);
            names.add(name);
        }
        public CharSequence getPageTitle(int position) {
            return null;

        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
