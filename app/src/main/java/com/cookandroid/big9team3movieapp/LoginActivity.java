package com.cookandroid.big9team3movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity<User> extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;             // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;         // 실시간 데이터베이스
    private EditText editEmail, editPwd;              // 회원가입 입력 필드
    private GoogleSignInClient mGoogleSignInClient; // 구글 api클라이언트
    private GoogleSignInAccount gsa;                // 구글 계정
    private SignInButton btn_google;                      // 구글 로그인 버튼
    private static int RC_SIGN_IN = 9001;           // 임의로 설정 가능
    private String email, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //파이어베이스 객체
        mFirebaseAuth = FirebaseAuth.getInstance();
        //데이터베이스 객체
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("loginApp");
        editEmail = findViewById(R.id.et_email);
        editPwd = findViewById(R.id.et_pwd);
        btn_google = (SignInButton)findViewById(R.id.btn_google);

        //1. 이메일과 비밀번호를 이용한 로그인 방법
        //로그인 버튼
        Button btn_login = findViewById(R.id.btn_login);
        //로그인 버튼 리스너:
        //로그인을 요청할 시 이메일과 비밀번호 요구
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 요청 시 이메일과 비밀번호 필요
                email = editEmail.getText().toString();
                pwd = editPwd.getText().toString();

                if(email.length()==0 || pwd.length()==0){
                    Toast.makeText(LoginActivity.this, "로그인하세요", Toast.LENGTH_SHORT).show();
                    editEmail.requestFocus();
                    return;
                }


                //파이어베이스 객체에 signInWithEmailAndPassword method를 써서 입력한 이메일과 패스워드를 넣는다..?
                mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //로그인 성공 시 메인 페이지로 이동
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();//현재 액티비티 파괴
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        Button btn_myPage = findViewById(R.id.btn_myPage);
        btn_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //회원가입 버튼
        Button btn_register = findViewById(R.id.btn_register);
        //회원가입 버튼 리스너
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //2. google 계정으로 이용한 로그인
        //Login 화면 호출: 사용자 id, 이메일 주소 및 기본을 요청하도록 로그인 구성
        //사용자 ID와 프로필 정보를 요청하기 위해서 GoogleSignInOptions 객체를 DEFAULT_SIGN_IN 인자와 함께 생성한다
        //gsa(GoogleSignInAccount)객체를 인자로 전달하여 GoogleSignInClient 객체를 생성함
        //R.string.server_client_id => res-string.xml에 내용 추가
        //server_client_id 값은 firebase=>Authentication=>SignInMethod=>Google=>웹SDK구성에서 웹 클라이언트 ID
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id)).requestEmail().build();

        //build a GoogleSignInClient with the options specified by googleSignInOption
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //마지막에 로그인한 객체 저장
                gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                //이미 구글 계정으로 로그인이 되어있는 상태
                if(gsa != null)
                {   //바로 메인화면으로 이동
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);

                }
                else {
                    //아니라면 signIn함수 호출
                    signIn();
                };
            }
        });
    }

    //SignIn 함수
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    //Login 결과값 받기: 사용자가 액티비티에서 실행한 결과를 onActivityResult에서 data로 받을 수 있다.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            //로그인을 했을 때 구글에서 넘겨주는 결과값(실행한 결과)를 받아와서 저장한다.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                //result가 성공했을 때, 이 값을 firebase에 넘겨주기 위해서 GoogleSignInAccount 객체를 생성한다.
                //SignInAccount: 사용자 로그인 후 return 받은 구글 계정에 대한 정보가 담긴 객체
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Tag","firebaseAuthWithGoogle:" +account.getId());
                //firebaseAuthWithGoogle에 account값을 전달한다.
                firebaseAuthWithGoogle(account.getIdToken());
            }catch(ApiException e){
                //로그인 실패시
                Log.w("TAG","Google sign in failed",e);
            }
        }


    }

    //사용자가 성공적으로 로그인하여 해당 메소드로 account(googleSignInAccount 객체)를 인자로 받게 될 때,
    //여기서 ID 토큰을 가져와서 firebase 사용자 인증정보로 교환한다.
    private void firebaseAuthWithGoogle(String idToken){
        //signInWithCredential 메소드를 통해서 firebase에 인증한다.
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser(); //USER 사용자 받아오면
                            //파이어베이스에 넣기 위해 구글에서 가져온 정보를 담은 파이어베이스유저 객체 생성
                            Toast.makeText(getApplicationContext(), "로그인" +user.getProviderData().get(1).getEmail(), Toast.LENGTH_SHORT).show();
                            UserAccount userAccount = new UserAccount();
                            userAccount.setIdToken(user.getUid());
                            userAccount.setName(user.getDisplayName());
                            userAccount.setPhone(user.getPhoneNumber());
                            userAccount.setEmailId(user.getEmail());

                            //만든 유저를 유저어카운트 테이블에 넣기
                            //리스너가 있어야 동작
                            mDatabaseRef.child("UserAccount").child(user.getUid()).setValue(userAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("FirebaseDatabase ::: ","회원가입성공 : " + user.getEmail());
                                }

                            });
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Google 로그인 성공!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    //로그아웃
//    private void signOut(){
//        mFirebaseAuth.signOut();
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this,task->{
//
//                });
//        gsa = null;
//    }




}