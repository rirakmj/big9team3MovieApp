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

    private ArrayList<Movie> mList = new ArrayList<>();

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
            final Movie movie = (Movie) inIntent.getSerializableExtra("movie");

//            MovieAdapter movieAdapter = new MovieAdapter(mList);
//
//            movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(int pos) {
//                    Movie movie = movieAdapter.getItem(pos);
            try {
                Document doc = Jsoup.connect("https://movie.naver.com/" + movie.getDetail_link()).get();
                Log.d("detaillink:", "https://movie.naver.com/" + movie.getDetail_link() + "");
                Elements mElementDataSize = doc.select("div[class=article]").select("div");
                int mElementSize = mElementDataSize.size();

                for (Element elem : mElementDataSize) {
                    String myTitle = elem.select("div h3[class=h_movie] a").text(); // 영화 제목
                    Element lElem = elem.select("div h3[class=h_movie] a").first();
                    String myLink = lElem.select("a").attr("href"); // 링크
                    String myImgUrl = elem.select("div div[class=poster] a img").attr("src"); // 포스터 링크
                    Elements rElem = elem.select("p[class=info_spec] span").next().next();
                    String myRelease = rElem.select("a").text(); // 개봉일
                    String myDirector = elem.select("dt[class=step2] a").text(); // 감독
                    Element gElem = elem.select("dl[class=info_spec] span").next().first();
                    String myGenre = gElem.select("a").text(); // 장르
                    // 좋아요 수
                    // 순위
                    // 별점
                    List myRate = elem.select("div[class=star_score ] a").eachAttr("span");
                    // 누적관객수
                    String myAudiencecnt = elem.select("span[class=count]").text();
                    // 줄거리
                    String mySynopsis = elem.select("div[class=story_area]").text();
                    // 배우
                    Element aElem = elem.select("div[class=people] li").next().first();
                    String myActor = aElem.select("a").text();
                    // 등급
                    String myGrade = elem.select("dt[class=step4] a").text();

                    mList.add(new Movie(myTitle, myLink, myImgUrl, myRelease, myDirector, myGenre,
                            myRate, myAudiencecnt, mySynopsis, myActor, myGrade));
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
            GlideApp.with(ivBigPoster).load(mList.get(posValue).getImg_url())
                    .override(300,400)
                    .into(ivBigPoster);

            tvMovieTitle.setText(String.valueOf(mList.get(posValue).getTitle()));

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
