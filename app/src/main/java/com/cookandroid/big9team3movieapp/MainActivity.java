package com.cookandroid.big9team3movieapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;

    Button btnLoginPage, btnMyPage;
    TextView tvTitle;

    private RecyclerView recyclerView;
    private ArrayList<Movie> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoginPage = (Button) findViewById(R.id.btnLoginPage);
        btnMyPage = (Button) findViewById(R.id.btnMyPage);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
        //파이어베이스 인증객체 생성
        mFirebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // 로그인 유무 확인
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            btnLoginPage.setVisibility(View.INVISIBLE);
            btnMyPage.setVisibility(View.VISIBLE);

            // 마이페이지로 이동
            btnMyPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            btnLoginPage.setVisibility(View.VISIBLE);
            btnMyPage.setVisibility(View.INVISIBLE);
        }
            // 로그인 페이지로 이동
            btnLoginPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);

        // AsyncTask 작동
        new Description().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class Description extends AsyncTask<Void, Void, Void> {

        // 진행바 표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 진행바 시작
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://movie.naver.com/movie/running/current.naver").get();
                Elements mElementDataSize = doc.select("ul[class=lst_detail_t1]").select("li");
                int mElementSize = mElementDataSize.size();

                for (Element elem : mElementDataSize) {
                    String myTitle = elem.select("li dt[class=tit] a").text(); // 제목
                    String myLink = elem.select("li div[class=thumb] a").attr("href"); // 상세 정보 링크
                    String myImgUrl = elem.select("li div[class=thumb] a img").attr("src"); // 포스터
                    Element rElem = elem.select("dl[class=info_txt1] dt").next().first();
                    String myRelease = "개요: " + rElem.select("dd").text();
                    Element dElem = elem.select("dl[class=info_txt1] dt").select("dt[class=tit_t2]").next().first();
                    String myDirector = "감독: " + dElem.select("a").text();

                    mList.add(new Movie(myTitle, myLink, myImgUrl, myRelease, myDirector));
                }
                Log.d("debug: ", "mList " + mElementDataSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 어댑터와 연결
            MovieAdapter movieAdapter = new MovieAdapter(mList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(movieAdapter);

            progressDialog.dismiss();

            // 상세보기 액티비티 띄우기
            movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos) {
                    // 선택한 아이템의 detail_link를 넘긴다.
                    String link = mList.get(pos).getDetail_link();
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("dlink", link);
                    startActivity(intent);
                    // 아이템을 클릭하면 상세보기 대화상자가 뜸
//                    View dialogview = View.inflate(MainActivity.this, R.layout.dialog_detail, null);
//                    ImageView ivBigPoster = dialogview.findViewById(R.id.ivBigPoster);
//                    GlideApp.with(dialogview).load(mList.get(pos).getImg_url())
//                            .override(500, 800)
//                            .into(ivBigPoster);

//                    Button btnOverview = dialogview.findViewById(R.id.btnOverview);
//                    Button btnReservation = dialogview.findViewById(R.id.btnReservation);
//                    Button btnVoting = dialogview.findViewById(R.id.btnVoting);
//                    Button btnReviewing = dialogview.findViewById(R.id.btnReviewing);
//
//                    // 줄거리 보기 버튼 이벤트 처리
//                    btnOverview.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Movie movie = movieAdapter.getItem(pos);
//                            Log.d("link:", mList.get(pos).getDetail_link() + "");
//                            Uri uri = Uri.parse("https://movie.naver.com/" + mList.get(pos).getDetail_link());
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(intent);
//                        }
//                    });
//
//                    //예매하기 이벤트처리
//                    btnReservation.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(),BookingActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//                    //별점리뷰 이벤트처리
//                    btnVoting.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(), ReviewwithstarActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//                    //리뷰 페이지 이벤트 처리
//                    btnReviewing.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
//                    dlg.setTitle(movie.getTitle());
//                    dlg.setView(dialogview);
//                    dlg.setNegativeButton("닫기", null);
//                    dlg.show();
                }
            });

        }

    }
}



