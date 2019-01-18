package com.example.yuanjinxie.sharkfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yuanjinxie.sharkfeed.PhotoActivity;
import com.example.yuanjinxie.sharkfeed.R;
import com.example.yuanjinxie.sharkfeed.model.SharkFeed;
import com.example.yuanjinxie.sharkfeed.model.SharkFeedEntity;

import java.util.List;

// This will bind data to the recycle view
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private List<SharkFeedEntity> mImages;
    private Context mContext;
    private SharkFeedEntity sharkFeedEntity;

    public RecyclerViewAdapter(Context context, SharkFeed sharkFeed) {
        this.mContext = context;
        this.mImages = sharkFeed.getFeedsList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_list,viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       Glide.with(mContext)
               .asBitmap()
               .load(mImages.get(i).getThumbnail())
               .into(viewHolder.sharkImg);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView sharkImg;
        CardView parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sharkImg = itemView.findViewById(R.id.shark_img_id);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharkFeedEntity = mImages.get(getAdapterPosition());
                    Bundle extra = new Bundle();
                    extra.putSerializable("feeditem", sharkFeedEntity);

                    Intent i = new Intent(mContext, PhotoActivity.class);
                    i.putExtra("extra", extra);
                    mContext.startActivity(i);
                }
            });
        }
    }

    public void setFeedEntity(List<SharkFeedEntity> entities) {
        this.mImages = entities;
    }
}
