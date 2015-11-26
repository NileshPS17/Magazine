package com.cloudfoyo.magazine;


import android.content.Context;
import android.os.Bundle;
import android.renderscript.Element;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;



public class SearchFragment extends Fragment {

    ListView listView;
    //public ArrayList<SearchItem> element = new ArrayList<SearchItem>();

    public SearchFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
    /*
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView=(ListView)view.findViewById(R.id.listview);
        SelectAdapter adapter=new SelectAdapter(getActivity());
        listView.setAdapter(adapter);
    }

  public class SelectAdapter extends BaseAdapter {
      LayoutInflater inflater;
      Context mcontext;

      SelectAdapter(Context context) {
          mcontext = context;
          inflater = LayoutInflater.from(mcontext);
      }

      public int getCount() {
          return element.size();
      }

      public Object getItem(int position) {
          return element.get(position);
      }

      public long getItemId(int position) {
          return 0;
      }

      public View getView(int position, View v, ViewGroup parent) {
          ArrayList<SearchItem> newElement;
          if (v == null) {
              v = inflater.inflate(R.layout.search_selector, parent, false);
          }
          SearchItem curElement = element.get(position);
          TextView topic = (TextView) v.findViewById(R.id.topic);
          topic.setText(curElement.topic);
          TextView author = (TextView) v.findViewById(R.id.author);
          TextView category = (TextView) v.findViewById(R.id.category);
          return v;
      }
  }

      public class SearchItem {

          String topic, author, category;

          public SearchItem(String topic, String author, String category) {
              this.topic = topic;
              this.category = category;
              this.author = author;
          }

      }
  }
*/