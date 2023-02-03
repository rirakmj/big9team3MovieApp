package com.cookandroid.big9team3movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;                                 //파이어베이스 인증
    private DatabaseReference mDatabaseRef;                             //실시간 데이터베이스
    private EditText editEmail, editPwd, editPwdCheck, editNickname, editPhone;  // 회원가입 입력 필드
    private Button btn_register, btn_return, btn_emailCheck, btn_nickNameCheck;                              // 회원가입 버튼
    String email, pwd, pwdCheck, nickname, phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //파이어베이스 auth 객체 생성
        mFirebaseAuth = FirebaseAuth.getInstance();
        //파이어베이스 데이터베이스 객체 생성
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("loginApp");
        editEmail = findViewById(R.id.et_email);
        editPwd = findViewById(R.id.et_pwd);
        editPwdCheck = findViewById(R.id.et_pwdCheck);
        editNickname = findViewById(R.id.et_nickname);
        editPhone = findViewById(R.id.et_phone);
        btn_register = findViewById(R.id.btn_register);
        btn_return = findViewById(R.id.btn_return);
        btn_emailCheck = findViewById(R.id.btn_emailCheck);
        btn_nickNameCheck = findViewById(R.id.btn_nickNameCheck);

        //등록 버튼 리스너
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        //돌아가기 버튼 리스너
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 화면으로 다시 이동
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        //이메일 중복 확인 버튼 리스너
        btn_emailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailCheck();

            }
        });

        //닉네임 중복 확인 버튼 리스너
        btn_nickNameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nicknameCheck();
            }
        });
    }

    private void signUp() {
        email = editEmail.getText().toString();
        pwd = editPwd.getText().toString();
        pwdCheck = editPwdCheck.getText().toString();
        nickname = editNickname.getText().toString();
        phone = editPhone.getText().toString();

        //입력사항 체크
        if (nickname.length() == 0) {
            Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
            editNickname.requestFocus();
            return;
        }
        if (phone.length() == 0) {
            Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            editPhone.requestFocus();
            return;
        }
        if (email.length() == 0) {
            Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
            editEmail.requestFocus();
            return;
        }
        if (pwd.length() < 6) {
            Toast.makeText(this, "비밀번호는 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
            editPwd.setText("");
            editPwd.requestFocus();
            return;
        }
        if (pwdCheck.length() < 6) {
            Toast.makeText(this, "확인용 비밀번호는 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
            editPwdCheck.setText("");
            editPwdCheck.requestFocus();
            return;
        }
        if (!pwd.equals(pwdCheck)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            editPwdCheck.setText("");
            editPwdCheck.requestFocus();
            return;
        }
        //Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다!!!!.", Toast.LENGTH_SHORT).show();

        //이메일과 비밀번호를 이용한 회원 객체 생성
        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다!!!!.", Toast.LENGTH_SHORT).show();
                if (pwd.equals(pwdCheck)) {
                    //비밀번호가 일치시 회원가입 가능
                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    UserAccount account = new UserAccount();
                    account.setIdToken(firebaseUser.getUid());
                    account.setEmailId(firebaseUser.getEmail());
                    account.setPassword(pwd);
                    account.setName(nickname);
                    account.setPhone(phone);
                    //setValue :database에 insert 처리
                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                    //회원가입 성공시 로그인 페이지로 이동
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //회원가입 실패 시 뜨는 오류 메세지
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //이메일 중복 확인
    private void emailCheck() {
        email = editEmail.getText().toString();
        if (email.length() == 0) {
            Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
            editEmail.requestFocus();
            return;
        }

        mDatabaseRef.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    UserAccount user = item.getValue(UserAccount.class);

                    if (user.getEmailId().equals(email)) {
                        Toast.makeText(RegisterActivity.this, "중복된 이메일입니다.", Toast.LENGTH_SHORT).show();
                        editEmail.setText("");
                        editEmail.requestFocus();
                        return;

                    } else {
                        Toast.makeText(RegisterActivity.this, "사용가능한 이메일입니다.", Toast.LENGTH_SHORT).show();
                        editPwd.requestFocus();
                        btn_register.setEnabled(true);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void nicknameCheck() {
        nickname = editNickname.getText().toString();
        if (nickname.length() == 0) {
            Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
            editNickname.requestFocus();
            return;
        }

        mDatabaseRef.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    UserAccount user = item.getValue(UserAccount.class);

                    if (user.getName().equals(nickname)) {
                        Toast.makeText(RegisterActivity.this, "중복된 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        editNickname.setText("");
                        editNickname.requestFocus();
                        return;

                    } else {
                        Toast.makeText(RegisterActivity.this, "사용가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        editPhone.requestFocus();
                        btn_register.setEnabled(true);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}