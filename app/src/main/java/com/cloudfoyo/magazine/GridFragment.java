package com.cloudfoyo.magazine;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {


    private ArrayList<GridItemWrapper> listOfGridItems = new ArrayList<GridItemWrapper>();
    private GridView gridView = null;

    public GridFragment() {
        // Required empty argument public constructor

        listOfGridItems.add(new GridItemWrapper(android.R.color.darker_gray, "Grey"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_green_dark, "Green"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_purple, "Purple"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_orange_dark, "Orange"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_red_dark, "Red"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.darker_gray, "Grey"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_green_dark, "Green"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_purple, "Purple"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_orange_dark, "Orange"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_red_dark, "Red"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.darker_gray, "Grey"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_green_dark, "Green"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_purple, "Purple"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_orange_dark, "Orange"));
        listOfGridItems.add(new GridItemWrapper(android.R.color.holo_red_dark, "Red"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        // Inflate the layout for this fragment

        return view;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = (GridView) getView().findViewById(R.id.gridview);
        ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ArticlesActivity.class);
                startActivity(intent);
            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

     class ImageAdapter extends BaseAdapter {
        Context mcontext;
         LayoutInflater inflater;


         public  ImageAdapter(Context context){
            mcontext=context;
             inflater=LayoutInflater.from(mcontext);
        }


        public int getCount(){

            return listOfGridItems.size();
        }

        public Object getItem(int position){

            return listOfGridItems.get(position);

        }
        public long getItemId(int position){
            return 0;
        }
        public View getView(int position,View view,ViewGroup parent){
            View root;
            TextView name;
            View v=view;
            gridView.setNumColumns(2);
            int size = gridView.getColumnWidth();
            GridItemWrapper curItem = listOfGridItems.get(position);
             if(v==null) {

                 v = inflater.inflate(R.layout.grid_item, parent, false);
                 v.setTag(R.id.picture, v.findViewById(R.id.picture));
                 v.setTag(R.id.text, v.findViewById(R.id.text));
             }
            root = v.findViewById(R.id.gridItem_root);
            root.setBackgroundResource(curItem.color);
            name = (TextView)v.findViewById(R.id.text);
            name.setText(curItem.title);

            root.setLayoutParams(new GridView.LayoutParams(size, size));
            return v;
        }
    }

    class GridItemWrapper
    {
        public int color;
        public String title;

        public GridItemWrapper(int color, String title) {
            this.color = color;
            this.title = title;
        }
    }

}
