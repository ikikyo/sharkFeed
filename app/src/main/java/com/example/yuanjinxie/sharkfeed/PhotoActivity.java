package com.example.yuanjinxie.sharkfeed;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanjinxie.sharkfeed.model.SharkFeedEntity;
import com.example.yuanjinxie.sharkfeed.model.SharkImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// This is the activity we use to show individual photos
public class PhotoActivity extends AppCompatActivity {

    private SharkImage image;
    private ImageView img, imagedownload, imgflickr;
    public static TextView text;
    private SharkFeedEntity entity;
    private String photo_url;

    private DownloadManager mDownloadManager;
    private Parcelable mState;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1

            );
        }
        setContentView(R.layout.photolayout);
        imgflickr = findViewById(R.id.imgflickr);
        imagedownload = findViewById(R.id.imagedownload);
        Bundle extra = getIntent().getBundleExtra("extra");
        entity = (SharkFeedEntity) extra.getSerializable("feeditem");
        image = new SharkImage();
        image.setID(entity.getID());
        img = (ImageView) findViewById(R.id.imageView);
        text = (TextView) findViewById(R.id.textView);


        final String url = "https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=949e98778755d1982f537d56236bbb42&photo_id=" + entity.getID() + "&format=json&nojsoncallback=1";

        TextFillerTask task = new TextFillerTask();
        task.execute(url);
        showphoto(entity.getHQ_Thumbnail());


        mDownloadManager = (DownloadManager) PhotoActivity.this.getSystemService(PhotoActivity.this.DOWNLOAD_SERVICE);
        // open url link for photo
        imgflickr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(entity.getHQ_Thumbnail()));
                startActivity(i);


            }
        });

        // download single photo
        imagedownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPhoto(entity.getHQ_Thumbnail());
                Toast.makeText(PhotoActivity.this, "Start downloading", Toast.LENGTH_LONG).show();
            }
        });

        //get image content
        //text.setText(image.getContent());
    }

    private void showphoto(String url) {
        photo_url = url;
        if (photo_url != "") {
            Picasso.with(getBaseContext())
                    .load(photo_url)
                    .into(img);
        }
    }

    private void downloadPhoto(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String fileName = "shark.png";
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("SharkFeed Download");
        request.setDescription(url)

                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        fileName);

        mDownloadManager.enqueue(request);

    }

    public class TextFillerTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
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
                    parseURL(response.toString());
                    return 1;
                } else {
                    return 0;
                }
            } catch (Exception e) {
            }
            return result;
        }

        private void parseURL(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONObject sh = response.getJSONObject("photo");
                JSONObject desc = sh.getJSONObject("description");
                JSONArray url = sh.getJSONObject("urls").getJSONArray("url");
                image.setContent(desc.optString("_content"));
                image.setFlickerLink( url.optJSONObject(0).optString("_content") );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                if (image.getContent().length()>100)
                    text.setText( (" Title: " + entity.getTitle() + "  Content: " +image.getContent()).substring(1,100) +"..." );
                else
                    text.setText(" Title: "+entity.getTitle() +"  Content: "+ image.getContent());
            } else {
                Toast.makeText(getBaseContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
