package com.cookandroid.big9team3movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailAdapter extends AppCompatActivity {

    TextView tvMovieTitle, tvMovieRelease, tvMovieGenre, tvLikeCount, tvRank, tvRate, tvAudience, tvSynopsis, tvDirector, tvActor, tvGrade;
    ImageView ivBigPoster;
    Button btnlike, btnStarReview, btnReview, btnBooking;
    RatingBar rbRatingBar;

    private ArrayList<Movie> mList;

    public DetailAdapter(ArrayList<Movie> movieList) {
        this.mList = movieList;
    }

    // 어댑터 내부에 인터페이스
    public interface OnDetailItemClickListener {
        void onDetailItemClick(int pos);
    }

    private DetailAdapter.OnDetailItemClickListener onDetailItemClickListener;

    public void setOnDetailItemClickListener(DetailAdapter.OnDetailItemClickListener onDetailItemClickListener) {
        this.onDetailItemClickListener = onDetailItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent inIntent = getIntent();
        final int posValue = inIntent.getIntExtra("pos", 0);

        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieRelease = findViewById(R.id.tvMovieRelease);
        tvMovieGenre = findViewById(R.id.tvMovieGenre);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        tvRank = findViewById(R.id.tvRate);
        tvRate = findViewById(R.id.tvRate);
        tvAudience = findViewById(R.id.tvAudience);
        tvSynopsis = findViewById(R.id.tvSynopsis);
        tvDirector = findViewById(R.id.tvDirector);
        tvActor = findViewById(R.id.tvActor);
        tvGrade = findViewById(R.id.tvGrade);

        ivBigPoster = findViewById(R.id.ivBigPoster);
        GlideApp.with(ivBigPoster).load(mList.get(posValue).getImg_url())
                .override(300,400)
                .into(ivBigPoster);

        tvMovieTitle.setText(String.valueOf(mList.get(posValue).getTitle()));
    }

}
