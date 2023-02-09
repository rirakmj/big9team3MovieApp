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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswordResetActivity<User> extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;             // 파이어베이스 인증
    private String currentPwd, newPassword, newPwdCheck;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //데이터베이스 객체
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    EditText currentPassword, newPwd, newPasswordCheck;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        //파이어베이스 객체
        mFirebaseAuth = FirebaseAuth.getInstance();
        //EditText 들
        currentPassword = findViewById(R.id.current_pwd);
        newPwd = findViewById(R.id.new_pwd);
        newPasswordCheck = findViewById(R.id.new_pwd_check);
        findViewById(R.id.btn_update).setOnClickListener(onClickListener);
        findViewById(R.id.btn_passwordCheck).setOnClickListener(onClickListener);


        // EditText들을 string으로
        currentPwd = ((EditText) findViewById(R.id.current_pwd)).getText().toString();
        newPassword = ((EditText) findViewById(R.id.new_pwd)).getText().toString();
        newPwdCheck = ((EditText) findViewById(R.id.new_pwd_check)).getText().toString();

    }

    View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()) {
            case R.id.btn_passwordCheck:
                check();
                break;

            case R.id.btn_update:
                update();
                break;

        }
    };

    private void check() {
        currentPwd = currentPassword.getText().toString();
        if (user != null) {
            String uid = mFirebaseAuth.getUid();
            databaseReference.getRef().child("loginApp").child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    String password = user.getPassword();
                    if (password.equals(currentPwd)) {
                        Toast.makeText(PasswordResetActivity.this, "현재 비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
                        findViewById(R.id.btn_passwordCheck).setClickable(false);
                        currentPassword.setEnabled(false);
                        newPwd.requestFocus();
                    } else {
                        Toast.makeText(PasswordResetActivity.this, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        currentPassword.setText("");
                        currentPassword.requestFocus();
                        findViewById(R.id.btn_passwordCheck).setClickable(true);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void update() {
        currentPwd = ((EditText) findViewById(R.id.current_pwd)).getText().toString();
        newPassword = ((EditText) findViewById(R.id.new_pwd)).getText().toString();
        newPwdCheck = ((EditText) findViewById(R.id.new_pwd_check)).getText().toString();
//        Toast.makeText(PasswordResetActivity.this, currentPwd.length()+"", Toast.LENGTH_SHORT).show();
        if (currentPwd.length() == 0) {
            Toast.makeText(PasswordResetActivity.this, "현재 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            currentPassword.requestFocus();
            return;
        }

        if (findViewById(R.id.btn_passwordCheck).getVisibility() == View.VISIBLE) {
            Toast.makeText(PasswordResetActivity.this, "중복확인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
            return;

        }

        if (newPassword.length() == 0) {
            Toast.makeText(PasswordResetActivity.this, "새로운 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            newPwd.requestFocus();
            return;
        }

        if (newPwdCheck.length() == 0) {
            Toast.makeText(PasswordResetActivity.this, "확인 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            newPasswordCheck.requestFocus();
            return;
        }


        //editText에 작성한 비밀번호들
        if (!newPassword.equals(newPwdCheck)) {
            Toast.makeText(PasswordResetActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            newPasswordCheck.setText("");
            newPasswordCheck.requestFocus();
            return;
        }
        if (newPassword.equals(newPwdCheck)) {
            newPassword = ((EditText) findViewById(R.id.new_pwd)).getText().toString();

            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordResetActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                signOut();
                                Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        }

    }

    private void signOut() {
        mFirebaseAuth.getInstance().signOut();
    }
}

