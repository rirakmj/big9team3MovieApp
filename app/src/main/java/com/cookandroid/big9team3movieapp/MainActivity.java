package com.cookandroid.big9team3movieapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private Toolbar toolbar;                        //툴바
    private NavigationView navigationView;          //숨겨진 네비게이션 뷰
    private DrawerLayout drawerLayout;              //숨겨진 뷰를 여는 레이아웃

    private FirebaseDatabase firebaseDatabase; //데이터베이스 객체
    private DatabaseReference databaseReference;
    FirebaseUser user; // 현재 유저 (DB에 들어있는 모든 회원..?)

    private RecyclerView recyclerView;
    private ArrayList<Movie> mList = new ArrayList<>();
    TextView info; //사용자 아이디
    TextView tvWelcome;
    private android.content.SharedPreferences preferences;
    private List<String> keyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MOVIE");

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //액션바 객체
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //뒤로 가기 버튼 이미지 적용
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawer_layout);

        info = findViewById(R.id.Name);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
        //파이어베이스 인증객체 생성
        mFirebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("currentuser", "user: " + user);

        preferences = getSharedPreferences("UserAccount", MODE_PRIVATE);
        if (user != null) {
            databaseReference.child("loginapp").child("UserAccount").child(user.getUid());
            //userid를 가져옴
            String uid = mFirebaseAuth.getUid();
            databaseReference.getRef().child("loginApp").child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    String name = user.getName();
                    info = findViewById(R.id.Name);
                    //님 환영합니다 뷰 수정
                    info.setText(name);
                    info.setTextSize(30);
                    tvWelcome = findViewById(R.id.tvWelcome);
                    tvWelcome.setVisibility(View.VISIBLE);
                    //Editor를 preferences에 쓰겠다고 연결
                    SharedPreferences.Editor editor = preferences.edit();
                    //putString(KEY,VALUE)
                    editor.putString("nickname", name);
                    //항상 commit & apply 를 해주어야 저장이 된다.
                    editor.commit();
                    //메소드 호출
                    getPreferences();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else {
            info = findViewById(R.id.Name);
        }

        //네비게이션 뷰에서 아이템 선택 시 해당 페이지로 이동 또는 주어진 역할 수행
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_login:
                        item.setChecked(true);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_movie:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_booking:
                        item.setChecked(true);
                        // displayMessage("slideshow");
                        Intent intent5 = new Intent(getApplicationContext(), BookingActivity.class);
                        startActivity(intent5);
                        return true;

                    case R.id.nav_setting:
                        item.setChecked(true);
                        // displayMessage("setting");
                        Intent intent2 = new Intent(getApplicationContext(), PasswordResetActivity.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_logout:
                        item.setChecked(true);
                        // displayMessage("logout");
                        if (user != null) {
                            signOut();
                            finish();
                            Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent3);
                            drawerLayout.closeDrawers();
                            return true;
                        }

                    case R.id.nav_delete:
                        item.setChecked(true);
                        // displayMessage("remove");
                        if (user != null) {
                            revokeAccess();
                            signOut();
                            finish();
                            Intent intent4 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent4);
                            drawerLayout.closeDrawers();
                            return true;
                        }
                }
                return false;
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);

        // AsyncTask 작동
        new Description().execute();

    }

    private void getPreferences() {
        //getString(KEY,KEY값이 없을때 대체)
        info.setText("" + preferences.getString("nickname", ""));
    }

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
                    String myKey = elem.select("li div[class=thumb] a").attr("href").substring(30);
                    //Log.d("key", "key: " + myKey);
                    String myTitle = elem.select("li dt[class=tit] a").text(); // 제목
                    String myLink = elem.select("li div[class=thumb] a").attr("href"); // 상세 정보 링크
                    String myImgUrl = elem.select("li div[class=thumb] a img").attr("src"); // 포스터
                    Element rElem = elem.select("dl[class=info_txt1] dt").next().first();
                    String myRelease = "개요: " + rElem.select("dd").text();
                    Element dElem = elem.select("dl[class=info_txt1] dt").select("dt[class=tit_t2]").next().first();
                    String myDirector = "감독: " + dElem.select("a").text();

                    mList.add(new Movie(myKey, myTitle, myLink, myImgUrl, myRelease, myDirector));
                    // addmovie(myKey, myTitle, myLink, myImgUrl, myRelease, myDirector);
                }
                //Log.d("debug: ", "mList " + mElementDataSize);
                } catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute (Void result){

                // 어댑터와 연결
                MovieAdapter movieAdapter = new MovieAdapter(mList, keyList);
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
                        String title = mList.get(pos).getTitle();
                        String key = mList.get(pos).getKey();
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("dlink", link);
                        intent.putExtra("dtitle", title);
                        intent.putExtra("dkey", key);
                        startActivity(intent);
                        //Log.d("key", "Key: " +key);

                    }
                });
            }
        }

        // 아이템을 클릭하면 상세보기 대화상자가 뜸
//                    View dialogview = View.inflate(MainActivity.this, R.layout.dialog_detail, null);
//                    ImageView ivBigPoster = dialogview.findViewById(R.id.ivBigPoster);
//                    GlideApp.with(dialogview).load(mList.get(pos).getImg_url())
//                            .override(500, 800)
//                            .into(ivBigPoster);

//                    Button btnOverview = dialogview.findViewById(R.id.btnOverview);
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
//                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
//                    dlg.setTitle(movie.getTitle());
//                    dlg.setView(dialogview);
//                    dlg.setNegativeButton("닫기", null);
//                    dlg.show();


        // 영화 데이터 파이어베이스에 저장
//        public void addmovie(String key, String title, String link, String url, String release, String director) {
//            Movie movie = new Movie(key, title, link, url, release, director);
//            databaseReference.child("movie").push().setValue(movie);
//        }

//    private void readData() {
//        databaseReference.child("movie").addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                keyList.clear();
//                String key = snapshot.getKey();
//                keyList.add(key);
//                Log.d("key", "key: " + key);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void displayMessage(String message) {
//        Toast.makeText(this, "message", Toast.LENGTH_SHORT).show();
//    }

        //메뉴 선택시 네비게이션 호출
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    addMenuItemInNavMenuDrawer(true);
                    drawerLayout.openDrawer(GravityCompat.START);
            }
            return super.onOptionsItemSelected(item);
        }

        private void addMenuItemInNavMenuDrawer(boolean flag) {
            Menu menu = navigationView.getMenu();
            MenuItem commeMenu = menu.findItem(R.id.common_menu);
            Menu subMenu = commeMenu.getSubMenu();

            MenuItem login = subMenu.findItem(R.id.nav_login);
            MenuItem logout = subMenu.findItem(R.id.nav_logout);
            MenuItem booking = subMenu.findItem(R.id.nav_booking);
            MenuItem setting = subMenu.findItem(R.id.nav_setting);
            MenuItem remove = subMenu.findItem(R.id.nav_delete);

            if (user != null) {
                flag = true;
            } else {
                flag = false;
            }

            if (flag == true) {
                login.setVisible(false);
                logout.setVisible(true);
                booking.setVisible(true);
                setting.setVisible(true);
                remove.setVisible(true);
            } else {
                login.setVisible(true);
                logout.setVisible(false);
                booking.setVisible(false);
                setting.setVisible(false);
                remove.setVisible(false);
            }
            navigationView.invalidate();
        }

        private void signOut() {
            mFirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, task -> {
                    });
            //gsa = null;
        }

        private void revokeAccess() {
            mFirebaseAuth.getCurrentUser().delete();
        }

    }




