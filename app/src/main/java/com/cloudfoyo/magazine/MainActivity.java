package com.cloudfoyo.magazine;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.cloudfoyo.magazine.sqlite.MagazineContentProvider;
import com.cloudfoyo.magazine.sqlite.MagazineDatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends MagazineAppCompatActivity {

    TabLayout tabLayout;
    private Toolbar toolbar;
    ViewPager viewPager;
    private int[]tab_icons={R.mipmap.home,R.mipmap.grid,R.mipmap.search,R.mipmap.about};

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


        foo();


    }
    private void setupviewpager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment(),"Home");
        viewPagerAdapter.addFragment(new GridFragment(),"Categories");
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


    //TODO:= Remove before Release .. For testing only
    public void foo()
    {
        MagazineDatabaseHelper helper = new MagazineDatabaseHelper(this);
        helper.getWritableDatabase();
        getContentResolver().query(Uri.parse("content://"+ MagazineContentProvider.AUTHORITY+"/categories/10"), null, null, null, null);
    }
}
