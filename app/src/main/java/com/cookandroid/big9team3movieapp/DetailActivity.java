package com.cookandroid.big9team3movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private ArrayList<MovieDetail> mdList = new ArrayList<>();

    TextView tvMTitle, tvMRelease, tvMGenre, tvGrade, tvMRuntime, tvLikeCount, tvMyscore, tvStarscore, tvAudiencecnt, tvSynopsis, tvDirector, tvActor;
    ImageView ivBigPoster;
    RatingBar rbMyscore, rbStarscore;

    Button btnReview, btnBooking;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //데이터베이스 객체
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent inIntent = getIntent();
        String dlink = inIntent.getStringExtra("dlink");
        String dkey = inIntent.getStringExtra("dkey");

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
        //파이어베이스 인증객체 생성
        mFirebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // 로그인 유무에 따라 버튼 보이기/숨기기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // btnStarReview = findViewById(R.id.btnStarReview);
        btnReview = findViewById(R.id.btnReview);
        btnBooking = findViewById(R.id.btnBooking);
        rbMyscore = findViewById(R.id.rbMyscore);
        tvMyscore = findViewById(R.id.tvMyscore);

        if (user == null) {
            // btnStarReview.setVisibility(View.INVISIBLE);
            btnReview.setVisibility(View.INVISIBLE);
            btnBooking.setVisibility(View.INVISIBLE);
        }

//        preferences = getSharedPreferences("UserAccount", MODE_PRIVATE);
        // 로그인한 유저가 등록한 별점이 있으면 그 별점을 내 평점에 표시한다

//        databaseReference.child("loginApp").child("STARREVIEW").child("dkey").addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//               public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String name = user.getDisplayName();
//                Query query = databaseReference.child("loginApp").child("STARREVIEW").child("dkey").equalTo(dkey);
//
//                if (query !=null || name.equals(preferences.getString("nicknake", ""))) {
//
//                    rbMyscore.setRating(Float.parseFloat(myscore));
//                    tvMyscore.setText(myscore);
//                }
//            }

//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

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
                String dkey = inIntent.getStringExtra("dkey");

                try {
                    Document doc = Jsoup.connect("https://movie.naver.com/" + dlink).get();
                    // Log.d("detaillink:", "https://movie.naver.com/" + dlink + "");
                    Elements elem = doc.select("#content > div.article");
//                int mElementSize = mElementDataSize.size();

                    if (elem.select("div div dl dt[class=step3]").isEmpty()) {

//                for (Element elem : mElementDataSize) {
                        String myTitle = elem.select("div.mv_info_area > div.mv_info > h3 > a:nth-child(1)").text(); // 영화 제목
                        String myImgUrl = elem.select("div.mv_info_area > div.poster > a > img").attr("src"); // 포스터 링크
                        String myRelease = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(4)").text().replace(" ", ""); // 개봉일
                        String myGenre = "장르: " + elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(1)").text().replace(" ", ""); // 영화 장르
                        String myRuntime = "상영시간: " + elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(3)").text();
                        String myGrade = "등급: " + elem.select("div div dl dt[class=step4]").next().first().text().replace(" ", "");
                        Float myStarscorerb = Float.parseFloat(elem.select("div.mv_info_area > div.mv_info > div.main_score > div.score.score_left > div.star_score > a > em").text().replace(" ", ""));
                        Float myStarscore = Float.parseFloat(elem.select("div.mv_info_area > div.mv_info > div.main_score > div.score.score_left > div.star_score > a > em").text().replace(" ", ""));
                        String myAudiencecnt = elem.select("div div dl dt[class=step9]").next().select("p[class=count]").text().replace(" ", "");
                        String mySynopsis = elem.select("div.section_group.section_group_frst > div:nth-child(1) > div > div > p").text();
                        String myDirector = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(4) > p > a").text();
                        String myBooking = elem.select("div.mv_info_area > div.mv_info > div.btn_sns > div.end_btn_area > ul > li:nth-child(1) > a").attr("href");
                        mdList.add(new MovieDetail(dkey, myTitle, myImgUrl, myRelease, myGenre, myRuntime, myGrade,
                                myStarscorerb, myStarscore, myAudiencecnt, mySynopsis, myDirector, "출연진 없음", myBooking));
                    } else {
                        String myTitle = elem.select("div.mv_info_area > div.mv_info > h3 > a:nth-child(1)").text(); // 영화 제목
                        String myImgUrl = elem.select("div.mv_info_area > div.poster > a > img").attr("src"); // 포스터 링크
                        String myRelease = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(4)").text().replace(" ", ""); // 개봉일
                        String myGenre = "장르: " + elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(1)").text().replace(" ", ""); // 영화 장르
                        String myRuntime = "상영시간: " + elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(3)").text();
                        String myGrade = "등급: " + elem.select("div div dl dt[class=step4]").next().first().text().replace(" ", "");
                        Float myStarscorerb = Float.parseFloat(elem.select("div.mv_info_area > div.mv_info > div.main_score > div.score.score_left > div.star_score > a > em").text().replace(" ", ""));
                        Float myStarscore = Float.parseFloat(elem.select("div.mv_info_area > div.mv_info > div.main_score > div.score.score_left > div.star_score > a > em").text().replace(" ", ""));
                        String myAudiencecnt = elem.select("div div dl dt[class=step9]").next().select("p[class=count]").text().replace(" ", "");
                        String mySynopsis = elem.select("div.section_group.section_group_frst > div:nth-child(1) > div > div > p").text();
                        String myDirector = elem.select("div.mv_info_area > div.mv_info > dl > dd:nth-child(4) > p > a").text();
                        String myActor = elem.select("div div dl dt[class=step3]").next().first().select("p").text();
                        String myBooking = elem.select("div.mv_info_area > div.mv_info > div.btn_sns > div.end_btn_area > ul > li:nth-child(1) > a").attr("href");

                        mdList.add(new MovieDetail(dkey, myTitle, myImgUrl, myRelease, myGenre, myRuntime, myGrade,
                                myStarscorerb, myStarscore, myAudiencecnt, mySynopsis, myDirector, myActor, myBooking));
                    }

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
                tvMyscore = findViewById(R.id.tvMyscore);
                tvStarscore = findViewById(R.id.tvStarscore);
                tvAudiencecnt = findViewById(R.id.tvAudiencecnt);
                tvSynopsis = findViewById(R.id.tvSynopsis);
                tvDirector = findViewById(R.id.tvDirector);
                tvActor = findViewById(R.id.tvActor);

                rbMyscore = findViewById(R.id.rbMyscore);
                rbStarscore = findViewById(R.id.rbStarscore);

                // mdList에 add한 값 화면에 뿌려주기
                tvMTitle.setText(mdList.get(0).getD_title());

                ivBigPoster = findViewById(R.id.ivBigPoster);
                GlideApp.with(ivBigPoster).load(mdList.get(0).getD_img_url())
                        .override(350, 550)
                        .into(ivBigPoster);

                tvMRelease.setText(mdList.get(0).getD_release());

                if (mdList.get(0).getD_genre() != null) {
                    tvMGenre.setText(mdList.get(0).getD_genre());
                } else {
                    tvMGenre.setText(" ");
                }

                tvMRuntime.setText(mdList.get(0).getD_runtime());

                tvGrade.setText(mdList.get(0).getD_grade());

                rbStarscore.setRating(mdList.get(0).getD_starscorerb() / 2 * 100 / 100.0f);
                tvStarscore.setText(String.format("%.2f", (mdList.get(0).getD_starscore() / 2)));

                tvAudiencecnt.setText(mdList.get(0).getD_audiencecnt());

                tvSynopsis.setText(mdList.get(0).getD_synopsis());

                tvDirector.setText(mdList.get(0).getD_director());

                if (mdList.get(0).getD_actor().equals("출연진 없음")) {
                    tvActor.setText("출연진 없음");
                } else {
                    tvActor.setText(mdList.get(0).getD_actor());
                }

                rbMyscore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        tvMyscore.setText(rating + "");

                        // 영화 제목, 영화 포스터, 내 평점 등록 액티비티로 넘기기
                        Intent inIntent = getIntent();
                        String dkey = inIntent.getStringExtra("dkey");
                        //Log.d("dkey", "dkey: " +dkey);
                        String mTitle = mdList.get(0).getD_title();
                        String mImgurl = mdList.get(0).getD_img_url();
                        String myscore = tvMyscore.getText().toString();
                        String myuid = mFirebaseAuth.getUid();

                        Intent intent = new Intent(DetailActivity.this, ReviewwithstarActivity.class);
                        intent.putExtra("myKey", dkey);
                        intent.putExtra("mTitle", mTitle);
                        intent.putExtra("mImgurl", mImgurl);
                        intent.putExtra("myscore", myscore);
                        intent.putExtra("myuid", myuid);
                        startActivityForResult(intent, 0);
                    }
                });

                //예매하기 이벤트처리
                btnBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
//                    startActivity(intent);
//                }
                        Intent inIntent = getIntent();
                        String dlink = inIntent.getStringExtra("dlink");
                        Uri uri = Uri.parse("https://movie.naver.com" + dlink);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                //별점리뷰 이벤트처리
//            btnStarReview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String mTitle = mdList.get(0).getD_title();
//                    String mImgurl = mdList.get(0).getD_img_url();
//                    Intent intent = new Intent(getApplicationContext(), ReviewwithstarActivity.class);
//                    intent.putExtra("mTitle", mTitle);
//                    intent.putExtra("mImgurl", mImgurl);
//                    startActivity(intent);
//                }
//            });

                //리뷰 페이지 이벤트 처리
                btnReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mTitle = mdList.get(0).getD_title();
                        String mImgurl = mdList.get(0).getD_img_url();
                        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                        intent.putExtra("mTitle", mTitle);
                        intent.putExtra("mImgurl", mImgurl);
                        startActivity(intent);
                    }
                });


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
//
        }

        @Override
        protected void onActivityResult (int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                String myscore2 = data.getStringExtra("myscore2");
                String myshortreview2 = data.getStringExtra("myshortreview2");
                String writer = data.getStringExtra("myname");
                //Log.d("writer", "writer: " +writer);

                if (writer != null) {
                    rbMyscore.setRating(Float.parseFloat(myscore2));
                    tvMyscore.setText(myscore2);
                }
            }
        }

//    // 영화 데이터 파이어베이스에 저장
//    public void updatemovie(String title, String link, String url, String release, String director, String myscore) {
//        Intent inIntent = getIntent();
//        String dtitle = inIntent.getStringExtra("dtitle");
//        if (mdList.get(0).getD_title() == dtitle) {
//            databaseReference.child("movie").child("myscore").setValue(new Movie(title, link, url, release, director, myscore));
//        }
//    }

}



