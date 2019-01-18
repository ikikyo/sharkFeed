package com.example.yuanjinxie.sharkfeed;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.yuanjinxie.sharkfeed.adapter.RecyclerViewAdapter;
import com.example.yuanjinxie.sharkfeed.model.FeedModel;
import com.example.yuanjinxie.sharkfeed.model.SharkFeed;
import com.example.yuanjinxie.sharkfeed.task.RefreshTask;

// Main page work flow:
// We have a model to maintain a list of feeds
// We use a task to fill or change this list -- refresh task
// We use a recycle + swipe refresh view to handle the refresh interaction
// The Controller is the adaptor
// 1. we will create the view without task. Make sure it's displaying an empty page.
// 2. Bind the model
// 3. tune the adaptor
// 4. fill the task
// We have completed the load process, we need to create the view for images to load pictures
public class MainActivity extends AppCompatActivity {

    private static final String TAG =  "MainActivity";
    private RecyclerView mRecyclerView;
    public static int page=1;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public SharkFeed sharkFeed;
    public RecyclerViewAdapter adapter;
    public RefreshTask refreshTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");

        // Model
        sharkFeed = ViewModelProviders.of(this).get(FeedModel.class).sharkFeed;

        // View
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Controller
        adapter = new RecyclerViewAdapter(this, sharkFeed);
        mRecyclerView.setAdapter(adapter);

        refreshTask = new RefreshTask(this ,mRecyclerView , sharkFeed,adapter );
        refreshTask.execute(page);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                page = 1;
                refreshTask = new RefreshTask(getApplication().getBaseContext() ,mRecyclerView , sharkFeed,adapter );
                refreshTask.execute(page);
            }
        });
    }
}
