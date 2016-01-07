package com.cloudfoyo.magazine;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudfoyo.magazine.extras.DynamicAdapterInterface;
import com.cloudfoyo.magazine.extras.ListItemArticleAdapter;
import com.cloudfoyo.magazine.extras.Utility;
import com.cloudfoyo.magazine.wrappers.Article;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = SearchFragment.class.getSimpleName();


    private EditText editText;
    private ListView listView;
    private ListItemArticleAdapter adapter;

    private AsyncSearchLoader loader;

    private AlertDialog dialog;

    private ProgressBar progressBar;
    private TextView noResult;
    private Handler handler;

    private InputMethodManager   imanager;
    public SearchFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = (EditText)view.findViewById(R.id.search);
        adapter = new ListItemArticleAdapter(getContext());
        imanager =(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {

                    editText.clearFocus();
                    String t = editText.getText().toString().trim();
                    if(t.length() > 0)
                        doSearch(editText.getText().toString());

                }

                return false;
            }
        });

        listView = (ListView)view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        noResult = (TextView)view.findViewById(R.id.noDataTextView);
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        AlertDialog.Builder buildr = new AlertDialog.Builder(getContext());
        buildr.setCancelable(false);
        buildr.setMessage("Your search did not match any documents !");
        buildr.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        buildr.setTitle("Sorry");
        dialog = buildr.create();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent  = new Intent(getActivity(), ViewArticleActivity.class);
        intent.putExtra(ViewArticleActivity.ACTION_ARTICLE, (Article) adapter.getItem(position));
        startActivity(intent);
    }

    public void doSearch(String q)
    {
        if(loader!=null)
        {
            loader.cancel(true);
            loader = null;
        }
        try {
            loader = new AsyncSearchLoader(getContext(), adapter);
            loader.execute(new URL(getString(R.string.base_url)+"articles/search/"+ URLEncoder.encode(q, "UTF-8")));
        }catch (Exception e)
        {
            Log.d(LOG_TAG, e.getMessage());
        }

        //Toast.makeText(getActivity(), q, Toast.LENGTH_SHORT).show();
    }


    public void showNoSearchResultsInterface()
    {

        dialog.show();


    }


    class AsyncSearchLoader extends AsyncTask<URL, Article, Void>
    {
        private Context c;
        private DynamicAdapterInterface<Article> adapter;

        public AsyncSearchLoader(Context c, DynamicAdapterInterface<Article> adapter) {
            this.c = c;
            this.adapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.clearItems();
            listView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setAlpha(1f);
            noResult.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(URL... params) {
            try
            {
                HttpURLConnection connection = (HttpURLConnection)params[0].openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if(responseCode == 200 || responseCode == 201) //Proceed
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line ="";
                    StringBuilder builder = new StringBuilder("");
                    while((line =  br.readLine())!=null)
                    {
                        builder.append(line);
                    }
                    connection.disconnect();
                    br.close();

                    JSONObject obj = new JSONObject(builder.toString());
                    if(obj.getBoolean(getString(R.string.json_error)))
                    {
                        throw new Exception(getString(R.string.server_error) + "\t Error => True");
                    }
                    else
                    {

                        JSONArray array = obj.getJSONArray("articles");
                        int n = array.length();

                        for (int i = 0; (i < n && !isCancelled()); ++i) {

                            if(i==0) { // Execute only the first iteration
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        noResult.setVisibility(View.INVISIBLE);
                                        Utility.crossFadeViews(progressBar, listView); // Hide ProgressBar, show GridView

                                    }
                                });
                            }

                            JSONObject object = array.getJSONObject(i);
                            Article article = new Article(object.getInt(getString(R.string.art_id)),
                                    object.getInt(getString(R.string.cat_id)),
                                    object.getString(getString(R.string.cat_name)),
                                    object.getString(getString(R.string.art_title)),
                                    object.getString(getString(R.string.art_author)),
                                    object.getString(getString(R.string.art_image)),
                                    object.getString(getString(R.string.art_date)),
                                    "" /** Keep the content empty to save memory and improve performance **/);
                            publishProgress(article);
                        }


                    }
                }
                else
                {
                    throw new Exception(getString(R.string.server_error) + "\tBad Response code" +responseCode);
                }

            }catch(Exception e)
            {
                this.cancel(true);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setVisibility(View.GONE);
                        Utility.crossFadeViews(progressBar, noResult);

                    }
                });
                Log.e(LOG_TAG, e.getMessage());
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(SearchFragment.this.adapter.getCount() == 0)
            {
                listView.setVisibility(View.GONE);
                Utility.crossFadeViews(progressBar, noResult);

            }

        }

        @Override
        protected void onProgressUpdate(Article... values) {
            super.onProgressUpdate(values);
            adapter.addItem(values[0]);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            adapter.clearItems();
        }
    }
}