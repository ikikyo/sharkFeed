package com.example.yuanjinxie.sharkfeed.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.yuanjinxie.sharkfeed.MainActivity;
import com.example.yuanjinxie.sharkfeed.adapter.RecyclerViewAdapter;
import com.example.yuanjinxie.sharkfeed.model.SharkFeed;
import com.example.yuanjinxie.sharkfeed.model.SharkFeedEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RefreshTask extends AsyncTask<Integer, Void, Integer> {
    private static final String TAG = "Asynctask";
    private List<SharkFeedEntity> feedsList;
    //    private Adaptor adapter;
    private Context context;
    private RecyclerView mRecyclerView;
    private SharkFeed feeds;
    private RecyclerViewAdapter Myadaptor;

    public RefreshTask(Context c, RecyclerView mR, SharkFeed fds, RecyclerViewAdapter Myadaptor) {
        this.context = c;
        this.mRecyclerView = mR;
        this.feeds = fds;
        this.feedsList = fds.getFeedsList();
        this.Myadaptor = Myadaptor;
    }


    @Override
    protected Integer doInBackground(Integer... params) {
        Integer result = 0;
        HttpURLConnection urlConnection;
        try {
            String Surl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=949e98778755d1982f537d56236bbb42&text=shark&format=json&nojsoncallback=1&page=" + params[0] + "&extras=url_t,url_c,url_l,url_o";

            URL url = new URL(Surl);
            urlConnection = (HttpURLConnection) url.openConnection();
            int statusCode = urlConnection.getResponseCode();

            // 200 represents HTTP OK
            if (statusCode == 200) {
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    response.append(line);
                }
                parseResult(response.toString());
                result = 1; // Successful
            } else {
                result = 0; //"Failed to fetch data!";
            }
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return result; //"Failed to fetch data!";
    }

    @Override
    protected void onPostExecute(Integer result) {


        if (result == 1) {
            Myadaptor.setFeedEntity(feedsList);
            Myadaptor.notifyDataSetChanged();
            MainActivity.swipeRefreshLayout.setRefreshing(false);


        } else {
            Toast.makeText(context, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }


    private void parseResult(String result) {
        Log.d(TAG, result);

        try {
            JSONObject response = new JSONObject(result);
            JSONArray photos = response.getJSONObject("photos").getJSONArray("photo");
            feedsList = feeds.getFeedsList();

            for (int i = 0; i < photos.length(); i++) {
                JSONObject post = photos.optJSONObject(i);
                SharkFeedEntity item = new SharkFeedEntity();
                item.setTitle(post.optString("title"));
                item.setID(post.optString("id"));
                item.setThumbnail(post.optString("url_t").toString());
                item.setHigh_QThumbnail(post.optString("url_c").toString());
                feedsList.add(item);
            }
            Log.d(TAG, "B:::" + feedsList.size());
            feeds.setFeedsList(feedsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
