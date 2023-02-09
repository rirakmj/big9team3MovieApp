package com.cookandroid.big9team3movieapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private ArrayList<Movie> mList;
    List<String> keyList;

    public MovieAdapter(ArrayList<Movie> movieList, List<String> keyList) {
        this.mList = movieList;
        this.keyList = keyList;
    }

    // 어댑터 내부에 인터페이스
    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPoster;
        private TextView tvTitle, tvRelease, tvDirector;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRelease = itemView.findViewById(R.id.tvRelease);
            tvDirector = itemView.findViewById(R.id.tvDirector);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    onItemClickListener.onItemClick(pos);
                }
            });
        }
    }

//    public Movie getItem(int position) {
//        Movie movie = mList.get(position);
//        return movie;
//    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
        GlideApp.with(holder.itemView).load(mList.get(position).getImg_url())
                .override(300,400)
                .into(holder.ivPoster);
        holder.tvTitle.setText(String.valueOf(mList.get(position).getTitle()));
        holder.tvRelease.setText(String.valueOf(mList.get(position).getRelease()));
        holder.tvDirector.setText(String.valueOf(mList.get(position).getDirector()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
