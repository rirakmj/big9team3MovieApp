package com.cookandroid.big9team3movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class DetailActivity extends AppCompatActivity {

    private ArrayList<MovieDetail> mdList = new ArrayList<>();

    TextView tvMTitle, tvMRelease, tvMGenre, tvGrade, tvMRuntime, tvLikeCount, tvStarscore, tvAudiencecnt, tvSynopsis, tvDirector, tvActor;
    ImageView ivBigPoster;
    RatingBar rbStarscore;

    Button btnlike, btnStarReview, btnReview, btnBooking;

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
                // Log.d("detaillink:", "https://movie.naver.com/" + dlink + "");
                Elements elem = doc.select("#content > div.article");
//                int mElementSize = mElementDataSize.size();

                if (elem.select("div div dl dt[class=step3]").isEmpty()) {

//                for (Element elem : mElementDataSize) {
                    String myTitle = elem.select("div.mv_info_area > div.mv_info > h3 > a:nth-child(1)").text(); // 영화 제목
                    String myImgUrl = elem.select("div.mv_info_area > div.poster > a > img").attr("src"); // 포스터 링크
//                    String myStarrating = elem.select("div.mv_info_area > div.mv_info > div.btn_sns > div.end_btn_area > ul > li:nth-child(2) > div > a > em").text();
//                    Log.d("likecnt", "like: " +myStarrating);
                    String myRelease = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(4)").text().replace(" ", ""); // 개봉일
                    String myGenre = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(1)").text().replace(" ", ""); // 영화 장르
                    String myRuntime = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(3)").text();
                    String myGrade = elem.select("div div dl dt[class=step4]").next().first().text().replace(" ", "");
                    Float myStarscorerb = Float.parseFloat(elem.select("div.mv_info_area > div.mv_info > div.main_score > div.score.score_left > div.star_score > a > em").text().replace(" ", ""));
                    String myStarscore = elem.select("div.mv_info_area > div.mv_info > div.main_score > div.score.score_left > div.star_score > a > em").text().replace(" ", "");
                    String myAudiencecnt = elem.select("div div dl dt[class=step9]").next().select("p[class=count]").text().replace(" ", "");
                    String mySynopsis = elem.select("div.section_group.section_group_frst > div:nth-child(1) > div > div > p").text();
                    String myDirector = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(4) > p > a").text();

                    mdList.add(new MovieDetail(myTitle, myImgUrl, myRelease, myGenre, myRuntime, myGrade,
                            myStarscorerb, myStarscore, myAudiencecnt, mySynopsis, myDirector, "출연진 없음"));
                } else {
                    String myTitle = elem.select("div.mv_info_area > div.mv_info > h3 > a:nth-child(1)").text(); // 영화 제목
                    String myImgUrl = elem.select("div.mv_info_area > div.poster > a > img").attr("src"); // 포스터 링크
//                    String myStarrating = elem.select("div.mv_info_area > div.mv_info > div.btn_sns > div.end_btn_area > ul > li:nth-child(2) > div > a > em").text();
//                    Log.d("likecnt", "like: " +myStarrating);
                    String myRelease = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(4)").text().replace(" ", ""); // 개봉일
                    String myGenre = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(1)").text().replace(" ", ""); // 영화 장르
                    String myRuntime = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(3)").text();
                    String myGrade = elem.select("div div dl dt[class=step4]").next().first().text().replace(" ", "");
                    Float myStarscorerb = Float.parseFloat(elem.select("div.mv_info_area > div.mv_info > div.main_score > div.score.score_left > div.star_score > a > em").text().replace(" ", ""));
                    String myStarscore = elem.select("div.mv_info_area > div.mv_info > div.main_score > div.score.score_left > div.star_score > a > em").text().replace(" ", "");
                    String myAudiencecnt = elem.select("div div dl dt[class=step9]").next().select("p[class=count]").text().replace(" ", "");
                    String mySynopsis = elem.select("div.section_group.section_group_frst > div:nth-child(1) > div > div > p").text();
                    String myDirector = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(4) > p > a").text();
                    String myActor = elem.select("div div dl dt[class=step3]").next().first().select("p").text();

                    mdList.add(new MovieDetail(myTitle, myImgUrl, myRelease, myGenre, myRuntime, myGrade,
                            myStarscorerb, myStarscore, myAudiencecnt, mySynopsis, myDirector, myActor));
                }

//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

            tvMTitle = findViewById(R.id.tvMovieTitle);
            tvMRelease = findViewById(R.id.tvMovieRelease);
            tvMGenre = findViewById(R.id.tvMovieGenre);
            tvMRuntime = findViewById(R.id.tvRuntime);
            tvGrade = findViewById(R.id.tvGrade);
            // tvLikeCount = findViewById(R.id.tvLikeCount);
            tvStarscore = findViewById(R.id.tvStarscore);
            tvAudiencecnt = findViewById(R.id.tvAudiencecnt);
            tvSynopsis = findViewById(R.id.tvSynopsis);
            tvDirector = findViewById(R.id.tvDirector);
            tvActor = findViewById(R.id.tvActor);

            rbStarscore = findViewById(R.id.rbStarscore);

            // mdList에 add한 값 화면에 뿌려주기
            tvMTitle.setText(mdList.get(0).getD_title());

            ivBigPoster = findViewById(R.id.ivBigPoster);
            GlideApp.with(ivBigPoster).load(mdList.get(0).getD_img_url())
                    .override(350,550)
                    .into(ivBigPoster);

            tvMRelease.setText(mdList.get(0).getD_release());

            if(mdList.get(0).getD_genre() != null) {
                tvMGenre.setText(mdList.get(0).getD_genre());
            } else {
                tvMGenre.setText(" ");
            }

            tvMRuntime.setText(mdList.get(0).getD_runtime());

            tvGrade.setText(mdList.get(0).getD_grade());

            rbStarscore.setRating(mdList.get(0).getD_starscorerb());
            tvStarscore.setText(mdList.get(0).getD_starscore());

            tvAudiencecnt.setText(mdList.get(0).getD_audiencecnt());

            tvSynopsis.setText(mdList.get(0).getD_synopsis());

            tvDirector.setText(mdList.get(0).getD_director());

            if (mdList.get(0).getD_actor().equals("출연진 없음")) {
                tvActor.setText("출연진 없음");
            } else {
                tvActor.setText(mdList.get(0).getD_actor());
            }
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
