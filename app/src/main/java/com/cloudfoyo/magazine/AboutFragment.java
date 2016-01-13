package com.cloudfoyo.magazine;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AboutFragment extends Fragment {

    private static final String LOG_TAG = AboutFragment.class.getSimpleName();
    public int[] image_ids={R.drawable.jerintitus1,R.drawable.ajithjnair2,R.drawable.amjadalikhan,R.drawable.nikhilzacharia,R.drawable.hilpa5,R.drawable.kriparajalekshmi,R.drawable.firozpareed7,R.drawable.arathysudheer,R.drawable.anukurup9,R.drawable.naveen10,R.drawable.ayasankar11,R.drawable.devi,R.drawable.ajeeshsabu13,R.drawable.ashik14 };

    List<Contributor> contributors = new ArrayList<>();

    private GridView gridView;

    public AboutFragment() {
        // Required empty public constructor

        contributors.add(new Contributor("Jerin Titus"));
        contributors.add(new Contributor("Ajith J Nair"));
        contributors.add(new Contributor("Amjadali Khan"));
        contributors.add(new Contributor("Nikhil Zacharia"));
        contributors.add(new Contributor("Shilpa S"));
        contributors.add(new Contributor("Kripa Rajalekshmi"));
        contributors.add(new Contributor("Firoz Pareed"));
        contributors.add(new Contributor("Arathy Sudheer"));
        contributors.add(new Contributor("Manu Kurup"));
        contributors.add(new Contributor("Naveen R"));
        contributors.add(new Contributor("Jayasankar"));
        contributors.add(new Contributor("Devipriya"));
        contributors.add(new Contributor("Ajeesh Sabu"));
        contributors.add(new Contributor("Ashik"));
    }

    Handler h = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Populate List here..

        return inflater.inflate(R.layout.fragment_about, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = (GridView)getView().findViewById(R.id.about_gridView);
        gridView.setNumColumns(2);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return contributors.size();
            }

            @Override
            public Object getItem(int position) {
                return contributors.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                int size = gridView.getColumnWidth();
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.text);
                ImageView imageView=(ImageView)convertView.findViewById(R.id.iv);
                imageView.setImageResource(image_ids[position]);
                tv.setText(contributors.get(position).name);
                GridView.LayoutParams params = new GridView.LayoutParams(size, size);
                convertView.setLayoutParams(params);
                return convertView;
            }
        });
    }

    class Contributor
    {
        public String name;


        public Contributor(String name) {
            this.name = name;
        }
    }



}

