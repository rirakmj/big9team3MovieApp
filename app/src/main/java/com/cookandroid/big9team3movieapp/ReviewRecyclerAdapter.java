package com.cookandroid.big9team3movieapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<ReviewItem> reviewItemArrayList;
    DatabaseReference databaseReference;
    long mNow;
    Date regdate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mDatabase.getReference();
    private FirebaseAuth mFirebaseAuth;
    int pos;
    List<String> keyList;
    ReviewItem reviewItem ;
    private SharedPreferences preferences;
    private String getTime() {
        mNow = System.currentTimeMillis();
        regdate = new Date(mNow);
        return mFormat.format(regdate);
    }

    public ReviewRecyclerAdapter(Context context, ArrayList<ReviewItem> reviewItemArrayList, List<String> keyList) {
        this.context = context;
        this.reviewItemArrayList = reviewItemArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.keyList = keyList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ReviewItem reviewItem = reviewItemArrayList.get(position);

        holder.tvwriter.setText("작성자 : " + reviewItem.getWriter());
        holder.tvcontent.setText("" + reviewItem.getContent());
        holder.tvregdate.setText("등록일 : " + reviewItem.getRegdate());
        pos = position;
        //작성자 아니면 버튼 안뜨게 하기
        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getUid();
        // String uid = mUser.getUid();
        // String str =  databaseReference.child("loginApp").child("REVIEW").;
        // Log.d("str","str"+str);
        mReference.child("loginApp").child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount user = snapshot.getValue(UserAccount.class);
                String userkey = user.getName();
                //   Log.d("key", "userkey:" + userkey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("loginApp").child("REVIEW").orderByChild("writer").addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //   Log.d("key111", "writerkey:");
                reviewItemArrayList.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    ReviewItem reviewWriter = dataSnapshot1.getValue(ReviewItem.class);
                    //  Log.d("key222", "writerkey:" + reviewWriter.getWriter());
                    reviewItemArrayList.add(reviewWriter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if ( reviewItem.getFlag().equals("1")
            //userkey.equals(writerkey)
            // mDatabase.getReference().child("loginApp").child("UserAccount").child(uid).orderByChild("name")==(mDatabase.getReference().child("loginApp").child("REVIEW").orderByChild("writer"))
        ) {
            holder.buttonUpdate.setVisibility(View.VISIBLE);
            holder.buttonDelete.setVisibility(View.VISIBLE);
            //Log.d("visible", "UserAccount: " + userkey);
            // Log.d("visible", "writer: " + writerkey);
        } else {
            holder.buttonUpdate.setVisibility(View.INVISIBLE);
            holder.buttonDelete.setVisibility(View.INVISIBLE);
            // Log.d("invisible", "UserAccount: " + userkey);
            // Log.d("invisible", "writer: " + writerkey);
        }

        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, reviewItem.getWriter(), reviewItem.getContent(), reviewItem.getRegdate());
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, reviewItem.getWriter());
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviewItemArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvwriter;
        TextView tvcontent;
        TextView tvregdate;

        Button buttonDelete;
        Button buttonUpdate;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvwriter = itemView.findViewById(R.id.tvwriter);
            tvcontent = itemView.findViewById(R.id.tvcontent);
            tvregdate = itemView.findViewById(R.id.tvregdate);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }

    public class ViewDialogUpdate {

        public void showDialog(Context context, String writer, String content, String regdate) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_add_new_review);

            TextView tvwriter = dialog.findViewById(R.id.textwriter);
            TextView tvcontent = dialog.findViewById(R.id.textcontent);

            tvwriter.setText(writer);
            tvcontent.setText(content);


            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonUpdate.setText("UPDATE");

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonUpdate.setOnClickListener(new View.OnClickListener() {

                String key = keyList.get(pos);

                @Override
                public void onClick(View view) {
                    String Id = tvwriter.getText().toString();
                    String Writer = tvwriter.getText().toString();
                    String Content = tvcontent.getText().toString();
                    String Regdate = getTime() + "";

                    if (writer.isEmpty() || content.isEmpty() || regdate.isEmpty()) {
                        Toast.makeText(context, "글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    } else {

                        if (Writer.equals(writer) && Content.equals(content) && Regdate.equals(regdate)) {
                            Toast.makeText(context, "변경 사항이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child("loginApp").child("REVIEW").child(key).child("content").setValue(Content);
                            Toast.makeText(context, "리뷰 수정이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }

    public class ViewDialogConfirmDelete {
        public void showDialog(Context context, String writer) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.view_dialog_confirm_delete);

            Button buttonDelete = dialog.findViewById(R.id.buttonDelete);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                String key = keyList.get(pos);
                @Override
                public void onClick(View view) {
                    databaseReference.child("loginApp").child("REVIEW").child(key).removeValue();
                    Toast.makeText(context, "삭제 완료 되었습니다!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Log.d("writer","writer"+writer);
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }

    }
}