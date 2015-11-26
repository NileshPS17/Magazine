package com.cloudfoyo.magazine;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AboutFragment extends Fragment {


    List<Contributor> contributors = new ArrayList<>();

    private GridView gridView;

    public AboutFragment() {
        // Required empty public constructor

        contributors.add(new Contributor("Alpha"));
        contributors.add(new Contributor("Beeta"));
        contributors.add(new Contributor("Gamma"));
        contributors.add(new Contributor("Omega"));
    }


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
                tv.setText(contributors.get(position).name);
                GridView.LayoutParams params = new GridView.LayoutParams(size, size);
                convertView.setLayoutParams(params);
                return convertView;
            }
        });
    }

    class Contributor
    {
        public String name; // TODO := Remove this class if no other info required

        public Contributor(String name) {
            this.name = name;
        }
    }



}