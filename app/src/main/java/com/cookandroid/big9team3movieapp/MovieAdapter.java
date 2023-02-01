package com.cookandroid.big9team3movieapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.LogRecord;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private ArrayList<Movie> mList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPoster;
        private TextView tvTitle, tvRelease, tvDirector;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRelease = itemView.findViewById(R.id.tvRelease);
            tvDirector = itemView.findViewById(R.id.tvDirector);
        }
    }

    public MovieAdapter(ArrayList<Movie> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {

        holder.tvTitle.setText(String.valueOf(mList.get(position).getTitle()));
        holder.tvRelease.setText(String.valueOf(mList.get(position).getRelease()));
        holder.tvDirector.setText(String.valueOf(mList.get(position).getDirector()));

        GlideApp.with(holder.itemView).load(mList.get(position).getImg_url())
                .override(300,400)
                .into(holder.ivPoster);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
