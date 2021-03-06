package com.cloudfoyo.magazine.extras;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cloudfoyo.magazine.R;
import com.cloudfoyo.magazine.wrappers.Article;
import com.cloudfoyo.magazine.wrappers.Category;

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
    private boolean isRequestedTypeLatest = true;
    private Handler handler;
    private Message msg = new Message();
    public AsyncArticleLoader(Context c, DynamicAdapterInterface<Article> adapter,boolean fetchContent, boolean isRequestedTypeLatest,Handler handler ) {
        this.adapter = adapter;
        this.context = c;
        this.fetchContent = fetchContent;
        this.isRequestedTypeLatest = isRequestedTypeLatest;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        adapter.clearItems();
        if(handler!=null)
        {
            msg = new Message();
            msg.what = Utility.SHOW_PROGRESS;
            handler.sendMessage(msg);
        }
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
                Log.d(LOG_TAG, params[0] + " \n " + builder.toString());

                JSONObject obj = new JSONObject(builder.toString());
                if(obj.getBoolean(context.getString(R.string.json_error)))
                {
                    throw new Exception(context.getString(R.string.server_error) + "\t Error => True");
                }
                else
                {

                        if(isRequestedTypeLatest) {
                            getLatestArticles(obj);
                        }
                        else // Then it is obviously fetch articles by category
                        {
                            getArticlesByCategory(obj);
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
            e.printStackTrace();
        }

        return null;
    }



    public void getLatestArticles(JSONObject obj) throws Exception
    {

        JSONArray array = obj.getJSONArray("articles");
        int n = array.length();
        int i;
        for(i=0; (i<n && !isCancelled()); ++i) {
            if(i==0 && handler!= null)
            {
                msg = new Message();
                msg.what = Utility.HIDE_PROGRESS;
                handler.sendMessage(msg);
            }
            JSONObject object = array.getJSONObject(i);
            Article article = new Article(object.getInt(context.getString(R.string.art_id)),
                    object.getInt(context.getString(R.string.art_cat_id)),
                    object.getString(context.getString(R.string.cat_name)),
                    object.getString(context.getString(R.string.art_title)),
                    object.getString(context.getString(R.string.art_author)),
                    object.getString(context.getString(R.string.art_image)),
                    object.getString(context.getString(R.string.art_date)),
                    (fetchContent ? object.getString(context.getString(R.string.art_content)) : ""), object.getString("article_video"));
            publishProgress(article);

        }


        if(i==0 && handler!=null){
            msg = new Message();
            msg.what = Utility.NO_RESULT;
            handler.sendMessage(msg);
        }

    }



    public void getArticlesByCategory(JSONObject obj1) throws Exception
    {

        JSONObject c = obj1.getJSONArray("category").getJSONObject(0);
        Category category = new Category(c.getInt("cat_id"),
                                         c.getString("category_name"),
                                         c.getString(context.getString(R.string.cat_description)),
                                         c.getString(context.getString(R.string.cat_image)),
                                         c.getString("createdAt"));

        JSONArray array = obj1.getJSONArray("articles");
        int n = array.length();
        int i;
        for(i=0; (i<n && !isCancelled()); ++i)
        {
            if(i==0 && handler!= null)
            {
                msg = new Message();
                msg.what = Utility.HIDE_PROGRESS;
                handler.sendMessage(msg);
            }
            JSONObject obj = array.getJSONObject(i);
            Article article = new Article(obj.getInt(context.getString(R.string.art_id)) ,
                                            category.getCategoryId() ,
                                            category.getName() ,
                                            obj.getString(context.getString(R.string.art_title)),
                                            obj.getString(context.getString(R.string.art_author)) ,
                                            obj.getString(context.getString(R.string.art_image)),
                                            obj.getString(context.getString(R.string.art_date)),
                             (fetchContent)?obj.getString(context.getString(R.string.art_content)):"", obj.getString("video"));

            publishProgress(article);



        }


        if(i==0 && handler!=null)
        {
            msg = new Message();
            msg.what = Utility.NO_RESULT;
            handler.sendMessage(msg);
        }

    }


    @Override
    protected void onCancelled(Void aVoid) {

        adapter.clearItems();
        if(handler!=null)
        {
            msg = new Message();
            msg.what = Utility.NO_RESULT;
            handler.sendMessage(msg);
        }

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Article... values) {
        super.onProgressUpdate(values);
        adapter.addItem(values[0]);
    }

}
