package com.cookandroid.big9team3movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PasswordResetByEmailActivity<User> extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;             // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;         // 실시간 데이터베이스
    private EditText editEmail;              // 회원가입 입력 필드
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_by_email);
        //파이어베이스 객체
        mFirebaseAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btn_send).setOnClickListener(onClickListener);



    }

    View.OnClickListener onClickListener =(v) ->{
        switch (v.getId()){
            case R.id.btn_send:
                send();
                break;

        }
    };

    private void send(){
        email = ((EditText)findViewById(R.id.et_email)).getText().toString();
        if(email.length()>0){
            mFirebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PasswordResetByEmailActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(PasswordResetByEmailActivity.this, LoginActivity.class);
        startActivity(intent);




    }

}