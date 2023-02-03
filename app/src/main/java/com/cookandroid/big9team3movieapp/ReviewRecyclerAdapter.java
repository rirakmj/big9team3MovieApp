package com.cookandroid.big9team3movieapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<ReviewItem> reviewItemArrayList;
    DatabaseReference databaseReference;
    long mNow;
    Date regdate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String getTime() {
        mNow = System.currentTimeMillis();
        regdate = new Date(mNow);
        return mFormat.format(regdate);
    }

    public ReviewRecyclerAdapter(Context context, ArrayList<ReviewItem> reviewItemArrayList) {
        this.context = context;
        this.reviewItemArrayList = reviewItemArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ReviewItem reviewItem = reviewItemArrayList.get(position);

        holder.tvwriter.setText("작성자 : " + reviewItem.getWriter());
        holder.tvcontent.setText("" + reviewItem.getContent());
        holder.tvregdate.setText("등록일 : " + reviewItem.getRegdate());

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

                @Override
                public void onClick(View view) {

                    String Writer = tvwriter.getText().toString();
                    String Content = tvcontent.getText().toString();
                    String Regdate = getTime() + "";

                    if (writer.isEmpty() || content.isEmpty() || regdate.isEmpty()) {
                        Toast.makeText(context, "Please Enter All data...", Toast.LENGTH_SHORT).show();
                    } else {

                        if (Writer.equals(writer) && Content.equals(content) && Regdate.equals(regdate)) {
                            Toast.makeText(context, "you don't change anything", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child("REVIEW").child(writer).setValue(new ReviewItem(Writer, Content, Regdate));
                            Toast.makeText(context, "Review Updated successfully!", Toast.LENGTH_SHORT).show();
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
                @Override
                public void onClick(View view) {
                    //작성자 수정 시 삭제 안됨!! pk값을 writer로 잡아놓음
                    databaseReference.child("REVIEW").child(writer).removeValue();
                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}