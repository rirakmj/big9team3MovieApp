package com.cookandroid.big9team3movieapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReviewActivity extends AppCompatActivity {
    ArrayList<MovieDetail> mdList;

    ImageView ivRM;
    TextView tvRM,tvRM2;
    Button buttonAdd;

    RecyclerView recyclerView;
    ArrayList<ReviewItem> reviewItemArrayList;
    ReviewRecyclerAdapter adapter;

    DatabaseReference databaseReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();

    long mNow;
    Date regdate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    Gson gson;
    String name;
    private SharedPreferences preferences;
    private List<String> keyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
//        Objects.requireNonNull(getSupportActionBar()).hide();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseAuth = FirebaseAuth.getInstance();

        reviewItemArrayList = new ArrayList<>();

        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("mTitle");
        String mImgurl = intent.getStringExtra("mImgurl");

        tvRM = findViewById(R.id.tvRM);
        ivRM = findViewById(R.id.ivRM);

        tvRM.setText(mTitle);
        GlideApp.with(ivRM).load(mImgurl)
                .override(250, 450)
                .into(ivRM);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                viewDialogAdd.showDialog(ReviewActivity.this);
            }
        });

        readData();

        //getSharedPreferences("파일이름",'모드')
        //모드 => 0 (읽기,쓰기가능)
        //모드 => MODE_PRIVATE (이 앱에서만 사용가능)
        preferences = getSharedPreferences("UserAccount", MODE_PRIVATE);
    }//onCreate

    private String getTime(){
        mNow = System.currentTimeMillis();
        regdate = new Date(mNow);
        return mFormat.format(regdate);
    }

    private void readData() {
        String uid = mFirebaseAuth.getUid();

        databaseReference.getRef().child("loginApp").child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount user = snapshot.getValue(UserAccount.class);
                String name = user.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference.child("loginApp").child("REVIEW").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewItemArrayList.clear();
                keyList.clear();

                String nickname = "";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReviewItem review = dataSnapshot.getValue(ReviewItem.class);
                    String key = dataSnapshot.getKey();
                    UserAccount user = new UserAccount();
                    if(review.getWriter().equals(preferences.getString("nickname",""))){
                        review.setFlag("1");
                    }
                    reviewItemArrayList.add(review);
                    keyList.add(key);
                }

                adapter = new ReviewRecyclerAdapter(ReviewActivity.this, reviewItemArrayList, keyList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public class ViewDialogAdd {
        public void showDialog(Context context) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_add_new_review);

            TextView textwriter = dialog.findViewById(R.id.textwriter);
            TextView textcontent = dialog.findViewById(R.id.textcontent);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonAdd.setText("ADD");
            String uid = mFirebaseAuth.getUid();
            mDatabase.getReference().child("loginApp").child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    String writer = user.getName();
                    textwriter.setText(writer);
                    // Log.d("info","토큰: "+uid);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String writer = textwriter.getText().toString();
                    String content = textcontent.getText().toString();
                    String regdate = getTime()+"";

                    if (content.isEmpty()) {
                        Toast.makeText(context, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    } else {
                        databaseReference.child("loginApp").child("REVIEW").push().setValue(new ReviewItem(writer,content,regdate));
                        Toast.makeText(context, "리뷰 작성 완료", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}
