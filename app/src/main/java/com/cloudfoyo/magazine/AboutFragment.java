package com.cloudfoyo.magazine;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AboutFragment extends Fragment {

    private static final String LOG_TAG = AboutFragment.class.getSimpleName();
    public int[] image_ids = {R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10};
    public int[] image_ids2 = {R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17, R.drawable.a18n, R.drawable.a19, R.drawable.a20, R.drawable.a21n, R.drawable.a22n};
    List<Contributor> contributors = new ArrayList<>();
    List<Contributor> contributors2 = new ArrayList<>();

    private ExpandableHeightGridView gridView, gridView2;

    RecyclerView recyclerView,recyclerView2;
    RecyclerView.LayoutManager layoutManager,layoutManager2;
    RelativeLayout layout;


    public AboutFragment() {
        // Required empty public constructor

        contributors.add(new Contributor("AMJAD ALI"));
        contributors.add(new Contributor("NIKHIL ZACHARIA GEORGE"));
        contributors.add(new Contributor("JAYASANKAR"));
        contributors.add(new Contributor("KRIPA RAJALEKSHMI"));
        contributors.add(new Contributor("AJITH J NAIR"));
        contributors.add(new Contributor("DEVAPRIYA"));
        contributors.add(new Contributor("MANU KURUP"));
        contributors.add(new Contributor("FIROZ PAREED"));
        contributors.add(new Contributor("SIJAH"));


        contributors2.add(new Contributor("JIBIN JAMES"));
        contributors2.add(new Contributor("HASEENA BEEGAM"));
        contributors2.add(new Contributor("ABHIJITH G"));
        contributors2.add(new Contributor("NITHIN JS"));
        contributors2.add(new Contributor("ASHIK SHEFIN"));
        contributors2.add(new Contributor("NELWIN JONES"));
        contributors2.add(new Contributor("KAVITHA PAUL"));
        contributors2.add(new Contributor("AMAL AHAMMED"));
        contributors2.add(new Contributor("MILA VS"));
        contributors2.add(new Contributor("AFZAL MUHAMMAD"));
        contributors2.add(new Contributor("ASHITHA G"));
        contributors2.add(new Contributor("SHANID"));




     /*   contributors.add(new Contributor("Naveen R"));
        contributors.add(new Contributor("Jayasankar"));
        contributors.add(new Contributor("Devapriya"));
        contributors.add(new Contributor("Ajeesh Sabu"));
        contributors.add(new Contributor("Ashik"));  */
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

        //layout=(RelativeLayout) view.findViewById(R.id.rl);
        int size=getActivity().getWindowManager().getDefaultDisplay().getWidth()/3;
        Log.d("Size",Integer.toString(size));

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //final Grid layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new MyRecyclerAdapter(contributors, image_ids,size, getActivity()));


        recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler_view2);
        //final Grid layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager2 = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView2.setAdapter(new MyRecyclerAdapter(contributors, image_ids2,size, getActivity()));

        //int size=recyclerView..getWidth();
       // RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(30,30);
       // recyclerView.setLayoutParams(layoutParams);

    }

}

/*
        gridView = (ExpandableHeightGridView)getView().findViewById(R.id.about_gridView);
        gridView.setNumColumns(2);


        gridView2 = (ExpandableHeightGridView)getView().findViewById(R.id.about_gridView2);
      // gridView2.setExpanded(true);
       gridView2.setNumColumns(2);
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
                Picasso.with(getActivity()).load(image_ids[position]).into(imageView);
                //imageView.setImageResource(image_ids[position]);
                tv.setText(contributors.get(position).name);
                GridView.LayoutParams params = new GridView.LayoutParams(size, size);
                convertView.setLayoutParams(params);
                return convertView;
            }
        });

       gridView.setExpanded(true);

       gridView2.setAdapter(new BaseAdapter() {
           @Override
           public int getCount() {
               return contributors2.size();
           }

           @Override
           public Object getItem(int position) {
               return contributors2.get(position);
           }

           @Override
           public long getItemId(int position) {
               return 0;
           }

           @Override
           public View getView(int position, View convertView, ViewGroup parent) {

               int size = gridView2.getColumnWidth();
               if (convertView == null) {
                   convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
               }
               TextView tv = (TextView) convertView.findViewById(R.id.text);
               ImageView imageView=(ImageView)convertView.findViewById(R.id.iv);
              // imageView.setImageResource(image_ids2[position]);
               Picasso.with(getActivity()).load(image_ids2[position]).into(imageView);
               tv.setText(contributors2.get(position).name);
               GridView.LayoutParams params = new GridView.LayoutParams(size, size);
               convertView.setLayoutParams(params);
               return convertView;
           }
       });

       gridView2.setExpanded(true);
    }





}

*/
