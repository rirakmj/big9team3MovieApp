package com.cookandroid.big9team3movieapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ReviewActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    RecyclerView recyclerView;

    ArrayList<ReviewItem> reviewItemArrayList;
    ReviewRecyclerAdapter adapter;
    Button buttonAdd;
    ArrayList<UserAccount> userAccountArrayList;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();


    long mNow;
    Date regdate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");


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

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                viewDialogAdd.showDialog(ReviewActivity.this);
            }
        });
        readData();
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        regdate = new Date(mNow);
        return mFormat.format(regdate);
    }

    private void readData() {
        String uid = mFirebaseAuth.getUid();
        databaseReference.child("loginApp").child("REVIEW").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewItemArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReviewItem review = dataSnapshot.getValue(ReviewItem.class);
                    reviewItemArrayList.add(review);
                }
                adapter = new ReviewRecyclerAdapter(ReviewActivity.this, reviewItemArrayList);
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
                    // TextView textwriter = findViewById(R.id.textwriter);
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    String writer = user.getName();
                    textwriter.setText(writer);
                    Log.d("info","토큰: "+uid);
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
