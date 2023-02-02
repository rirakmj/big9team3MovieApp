package com.cookandroid.big9team3movieapp;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("deprecation")
public class MyPageActivity extends TabActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        //TabHost 객체 생성
        TabHost tabHost = getTabHost();
        //탭스팩 선언학, 탭의 내부 명칭, 탭에 출력될 글 작성
        TabHost.TabSpec spec;
        //인텐트 객체 생성
        Intent intent;

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성(Info)
        intent = new Intent().setClass(this,InformationActivity.class);
        //객체 생성
        spec = tabHost.newTabSpec("Info");
        spec.setIndicator("Info");
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성(review)
        intent = new Intent().setClass(this,RegisterActivity.class);
        //객체 생성
        spec = tabHost.newTabSpec("Review");
        spec.setIndicator("Review");
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성(booking)
        intent = new Intent().setClass(this,BookingActivity.class);
        //객체 생성
        spec = tabHost.newTabSpec("Booking");
        spec.setIndicator("Booking");
        spec.setContent(intent);
        tabHost.addTab(spec);

        //먼저 열릴 탭을 설정
        tabHost.setCurrentTab(0);

        Button btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //로그아웃 버튼
        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
    private void signOut(){
        mFirebaseAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this,task->{

                });
        //gsa = null;
    }
}