package com.cloudfoyo.magazine;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudfoyo.magazine.extras.ActivityPingListener;
import com.cloudfoyo.magazine.extras.DynamicAdapterInterface;
import com.cloudfoyo.magazine.extras.Utility;
import com.cloudfoyo.magazine.wrappers.Category;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment implements ActivityPingListener{

    private static final String LOG_TAG = GridFragment.class.getSimpleName();

    private GridView gridView = null;
    private AsyncGridLoader gridLoader = null;
    private ImageAdapter imageAdapter;
    private ProgressBar progressBar;
    private TextView noCategories;
    //int[] images={R.drawable.cheese_1,R.drawable.cheese_2,R.drawable.cheese_3,R.drawable.cheese_4,R.drawable.cheese_5};

    public GridFragment() {
        // Required empty argument public constructor


    }


    private Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        // Inflate the layout for this fragment

        noCategories = (TextView)view.findViewById(R.id.noDataTextView);
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        return view;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView = (GridView)view.findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Activity act = getActivity();
                Intent intent = new Intent(act, ArticlesActivity.class);
                intent.putExtra("category", (Category) imageAdapter.getItem(i));
                startActivity(intent);


            }
        });

        gridView.setNumColumns(2);


        gridView.setVisibility(View.INVISIBLE);
        noCategories.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);


        populateGrid();
    }

    @Override
    public void onActivityPing(Intent intent) {
        if(intent.getAction().equals(MainActivity.FULL_RELOAD))
        {
                populateGrid();
        }
    }

    public void populateGrid()
    {
        try
        {

            if(gridLoader !=null)
            {
                gridLoader.cancel(true);
                gridLoader = null;
            }
            gridLoader =  new AsyncGridLoader();
            gridLoader.execute(new URL(getString(R.string.base_url) + "categories"));
        }
        catch(MalformedURLException e)
        {
            imageAdapter.clearItems();
            noCategories.setVisibility(View.VISIBLE); //Show no Categories
            Log.d(LOG_TAG, "Whooops! That shouldn't happen. Must get the latest version of app.");
        }
    }


    class ImageAdapter extends BaseAdapter implements DynamicAdapterInterface<Category> {
         Context mcontext;
         LayoutInflater inflater;

        private ArrayList<Category> categoriesList = new ArrayList<>();

         public  ImageAdapter(Context context){
              mcontext=context;
             inflater=LayoutInflater.from(mcontext);

        }


        @Override
        public void clearItems()
        {
            categoriesList.clear();
            notifyDataSetChanged();
        }

        @Override
        public void addItem(Category item)
        {
            categoriesList.add(item);
            this.notifyDataSetChanged();
        }




        public int getCount(){

            return categoriesList.size();
        }

        public Object getItem(int position){

            return categoriesList.get(position);

        }
        public long getItemId(int position){
            return 0;
        }
        public View getView(int position,View view,ViewGroup parent){
            View root;
            TextView name;
            View v=view;
            ImageView iv;
            Category curItem = categoriesList.get(position);

             if(v == null) {

                 v = inflater.inflate(R.layout.grid_item, parent, false);
             }
            root = v.findViewById(R.id.root);
            iv=(ImageView)v.findViewById(R.id.iv);
            int size =  gridView.getColumnWidth();
            Picasso.with(mcontext).load(curItem.getImageUrl())
                                  .placeholder(R.drawable.img_loading)
                                  .error(R.drawable.img_loading)
                                  .resize(size, size)
                                  .into(iv);

            name = (TextView)v.findViewById(R.id.text);
            name.setText(curItem.getName());
            root.setLayoutParams(new GridView.LayoutParams(size, size));
            return v;
        }
    }




    class AsyncGridLoader extends AsyncTask<URL, Category, ArrayList<Category> >
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageAdapter.clearItems();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setAlpha(1f);
                    progressBar.setVisibility(View.VISIBLE);//Hide gridView, showProgressbar
                }
            });

        }

        @Override
        protected ArrayList<Category> doInBackground(URL... params) {

            try {
                HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responseCode = connection.getResponseCode();
                if(responseCode == 201 || responseCode == 200)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder("");
                    String line="";
                    while((line = br.readLine()) != null)
                    {
                        builder.append(line);
                    }
                    //Log.d(LOG_TAG, builder.toString());
                    br.close();
                    connection.disconnect();
                    //Parse JSON and obtain the list of Category Objects
                    JSONObject root = new JSONObject(builder.toString());
                    if(! root.getBoolean(getString(R.string.json_error)))
                    {
                        JSONArray array = root.getJSONArray(getString(R.string.categories));
                        int n = array.length();
                        for(int i=0; i<n && !isCancelled(); ++i)
                        {

                            if(i==0) { // Execute only the first iteration
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        noCategories.setVisibility(View.INVISIBLE);
                                        Utility.crossFadeViews(progressBar, gridView); // Hide ProgressBar, show GridView
                                        gridView.setVisibility(View.VISIBLE);
                                        gridView.setAlpha(1f);
                                    }
                                });
                            }

                            JSONObject object = array.getJSONObject(i);
                            Category category = new Category(object.getInt(getString(R.string.cat_id)),
                                                            object.getString(getString(R.string.category_name)),
                                                            object.getString(getString(R.string.cat_description)),
                                                            object.getString(getString(R.string.cat_image)),
                                                            object.getString(getString(R.string.cat_date)));

                            publishProgress(category);



                        }



                    }
                    else
                    {
                        throw new Exception(getString(R.string.server_error) + ":=Server fault!");
                    }

                }
                else
                {

                    throw new Exception(getString(R.string.server_error) + ":= Bad Response Code " + responseCode);
                }

            }catch (Exception e)
            {
                    Log.e(LOG_TAG, e.getMessage());
                    this.cancel(true);
                    handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Utility.crossFadeViews(progressBar, noCategories);
                    }
                });

                //Something went wrong
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Category... values) {
            super.onProgressUpdate(values);

            imageAdapter.addItem(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            super.onPostExecute(categories);
            //Do nothing .. for now

            if(imageAdapter.getCount() == 0)
            {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            Utility.crossFadeViews(progressBar, noCategories);
                    }
                });
            }

        }

        @Override
        protected void onCancelled(ArrayList<Category> categories) {
            super.onCancelled(categories);
            imageAdapter.clearItems();
        }
    }


}


