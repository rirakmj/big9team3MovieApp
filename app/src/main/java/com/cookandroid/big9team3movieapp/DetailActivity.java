package com.cookandroid.big9team3movieapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView tvMovieTitle, tvMovieRelease, tvMovieGenre, tvLikeCount, tvRank, tvRate, tvAudience, tvSynopsis, tvDirector, tvActor, tvGrade;
    ImageView ivBigPoster;
    Button btnlike, btnStarReview, btnReview, btnBooking;
    RatingBar rbRatingBar;

    private ArrayList<MovieDetail> mdList = new ArrayList<>();

    // 로그인 안 되어있으면 투표, 리뷰 버튼 안 보이도록.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        new Description().execute();
    }

    private class Description extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent inIntent = getIntent();
            String dlink = inIntent.getStringExtra("dlink");

            try {
                Document doc = Jsoup.connect("https://movie.naver.com/" + dlink).get();
                Log.d("detaillink:", "https://movie.naver.com/" + dlink + "");
                Elements mElementDataSize = doc.select("div[class=wide_info_area]").select("div");
                int mElementSize = mElementDataSize.size();

                for (Element elem : mElementDataSize) {
                    String myTitle = elem.select("h3[class=h_movie] a").text(); // 영화 제목
                    String myImgUrl = elem.select("div[class=poster] a img").attr("src"); // 포스터 링크
                    String myStarrating = elem.select("div[class=star_score ] a em").text();
                    String myRelease = elem.select("p[class=info_spec]").text(); // 개봉일
                    String myDirector = elem.select("dl[class=step1] a").text();

                    mdList.add(new MovieDetail(myTitle, myImgUrl, myStarrating, myRelease, myDirector));
                }
                Log.d("debug: ", "mList " + mElementDataSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

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
            GlideApp.with(ivBigPoster).load(mdList.get(1).getD_img_url())
                    .override(300,400)
                    .into(ivBigPoster);

            tvMovieTitle.setText(mdList.get(0).getD_title());

        }


//            // 줄거리 보기 버튼 이벤트 처리
//            btnOverview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Movie movie = movieAdapter.getItem(pos);
//                    Log.d("link:", mList.get(pos).getDetail_link() + "");
//                    Uri uri = Uri.parse("https://movie.naver.com/" + mList.get(pos).getDetail_link());
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                }
//            });
//
//            //예매하기 이벤트처리
//            btnReservation.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//            //별점리뷰 이벤트처리
//            btnVoting.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), ReviewwithstarActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//            //리뷰 페이지 이벤트 처리
//            btnReviewing.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//
//        }
    }
}
