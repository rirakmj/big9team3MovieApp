package com.cookandroid.big9team3movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewwithstarActivity extends AppCompatActivity {
    ImageView ivRSM;
    RatingBar rbMyscore;
    TextView tvRSM, tvMyscore, tvWriter;
    EditText etShortreview;
    Button saveButton, cancelButton;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();
    private ArrayList<ReviewStarItem> starReview;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewwithstar);

        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("mTitle");
        String mImgurl = intent.getStringExtra("mImgurl");
        String myscore = intent.getStringExtra("myscore");
        String myuid = intent.getStringExtra("myuid");

        ivRSM = findViewById(R.id.ivRSM);
        tvRSM = findViewById(R.id.tvRSM);
        tvWriter = findViewById(R.id.tvWriter);
        rbMyscore = findViewById(R.id.rbMyscore);
        tvMyscore = findViewById(R.id.tvMyscore);
        etShortreview = findViewById(R.id.etShortreview);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // 선택한 영화 포스터와 타이틀
        GlideApp.with(ivRSM).load(mImgurl)
                .override(250, 450)
                .into(ivRSM);
        tvRSM.setText(mTitle);

        // 상세보기 화면에서 넣은 별점 가져오기
        if (myscore != null) {
            rbMyscore.setRating(Float.parseFloat(myscore));
            tvMyscore.setText(Float.toString(rbMyscore.getRating()));

            // 작성 화면에서도 별점 변경 가능하게 하기
            rbMyscore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    tvMyscore.setText(rating + "");
                }
            });
        }

        // tvWriter (hidden)에 로그인 된 유저 닉네임 불러오기
        mReference.child("loginApp").child("UserAccount").child(myuid)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount user = snapshot.getValue(UserAccount.class);
                        String name = user.getName();
                        tvWriter = findViewById(R.id.tvWriter);
                        tvWriter.setText(name);
                        Log.d("myname", "myname: " + name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String score = tvMyscore.getText().toString();
                String shortreview = etShortreview.getText().toString();
                String writer = tvWriter.getText().toString();
                String title = tvRSM.getText().toString();
                ReviewStarItem starReview = new ReviewStarItem(score, shortreview, writer, title);
                mReference.child("starreview").push().setValue(starReview);

                // 양방향 액티비티로 별점 넘겨주기

//                if (mTitleDb.equals(mTitle2)) {
//                    String myscore = tvMyscore.getText().toString();
//                    String myshortreview = etShortreview.getText().toString();
//                    String writer = tvWriter.getText().toString();
//
//                    databaseReference.child("movie").child("title").push().setValue(new ReviewStarItem(myscore, myshortreview, writer));
//                    Toast.makeText(getApplicationContext(), "평점 등록 완료", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "로그인 해 주세요.", Toast.LENGTH_SHORT).show();
//                    Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent2);
//                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
}