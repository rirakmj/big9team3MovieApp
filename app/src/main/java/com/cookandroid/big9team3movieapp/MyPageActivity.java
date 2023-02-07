package com.cookandroid.big9team3movieapp;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressWarnings("deprecation")
public class MyPageActivity extends TabActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();

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
        intent = new Intent().setClass(this, InformationActivity.class);
        //객체 생성
        spec = tabHost.newTabSpec("Info");
        spec.setIndicator("Info");
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성(review)
        intent = new Intent().setClass(this, RegisterActivity.class);
        //객체 생성
        spec = tabHost.newTabSpec("Review");
        spec.setIndicator("Review");
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성(booking)
        intent = new Intent().setClass(this, BookingActivity.class);
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

        String uid = mFirebaseAuth.getUid();
        mDatabase.getReference().child("loginApp").child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView infotv = findViewById(R.id.infotv);
                UserAccount user = snapshot.getValue(UserAccount.class);
                String name = user.getName();
                infotv.setText(name);
                Log.d("info", "토큰: " + uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }//onCreate

    public void DialogClick(View view) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        view = inflater.inflate(R.layout.update_info_dlg, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("닉네임 변경하기");
        builder.setView(view);
        String uid = mFirebaseAuth.getUid();
        View finalView = view;
        mDatabase.getReference().child("loginApp").child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("info", "수정: " + uid);
                TextView updateName = finalView.findViewById(R.id.updateName);
                UserAccount user = snapshot.getValue(UserAccount.class);
                String name = user.getName();
                updateName.setText(name);
                Button UpdateInfoBtn = finalView.findViewById(R.id.UpdateInfoBtn);
                UpdateInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // String newname = updateName.getText().toString();
                        // mDatabase.getReference().child("loginApp").child("UserAccount").child(uid).setValue(new UserAccount(newname));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Yeah!!", Toast.LENGTH_LONG).show();

            }
        });

        builder.setNeutralButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void signOut() {
        mFirebaseAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {

                });
        //gsa = null;
    }
}