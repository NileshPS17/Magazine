package com.cloudfoyo.magazine.extras;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudfoyo.magazine.R;
import com.cloudfoyo.magazine.wrappers.Article;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncArticleLoader extends AsyncTask<URL, Article, Void>
{


    private static final String LOG_TAG = AsyncArticleLoader.class.getSimpleName();

    private boolean fetchContent = false;
    private DynamicAdapterInterface<Article> adapter;
    private Context context;
    public AsyncArticleLoader(Context c, DynamicAdapterInterface<Article> adapter,boolean fetchContent) {
        this.adapter = adapter;
        this.context = c;
        this.fetchContent = fetchContent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        adapter.clearItems();
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
                if(obj.getBoolean(context.getString(R.string.json_error)))
                {
                    throw new Exception(context.getString(R.string.server_error) + "\t Error => True");
                }
                else
                {
                    JSONArray array = obj.getJSONArray("articles");
                    int n = array.length();
                    for(int  i=0; (i<n && !isCancelled()); ++i)
                    {
                        JSONObject object = array.getJSONObject(i);
                        Article article = new Article(object.getInt(context.getString(R.string.art_id)),
                                object.getInt(context.getString(R.string.art_cat_id)),
                                object.getString(context.getString(R.string.cat_name)),
                                object.getString(context.getString(R.string.art_title)),
                                object.getString(context.getString(R.string.art_author)),
                                object.getString(context.getString(R.string.art_image)),
                                object.getString(context.getString(R.string.art_date)),
                                (fetchContent ? object.getString(context.getString(R.string.art_content)):""));
                        publishProgress(article);
                    }

                }
            }
            else
            {
                throw new Exception(context.getString(R.string.server_error) + "\tBad Response code" +responseCode);
            }

        }catch(Exception e)
        {
            this.cancel(true);
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }


    @Override
    protected void onCancelled(Void aVoid) {

        adapter.clearItems();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Do nothing
    }

    @Override
    protected void onProgressUpdate(Article... values) {
        super.onProgressUpdate(values);
        adapter.addItem(values[0]);
    }

}