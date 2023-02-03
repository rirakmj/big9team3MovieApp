package com.cookandroid.big9team3movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

public class BookingActivity extends AppCompatActivity {
    private String str = "";
    int selectYear, selectMonth, selectDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        final String[] mid = {"2시", "3시", "4시", "5시", "6시"};

        Button btn1 = findViewById(R.id.btn1);
        ListView tv1 = findViewById(R.id.lv1);
        CalendarView cv1 = findViewById(R.id.cView);

        //날짜 이벤트처리
        cv1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectYear = year;
                selectMonth = month + 1;
                selectDay = dayOfMonth;
            }
        });

//        datePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        //리스트뷰 이벤트처리
        tv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                str = (String) tv1.getItemAtPosition(position);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, mid);
        tv1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tv1.setAdapter(adapter);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();


            }
        });


    }

    void showDialog() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(BookingActivity.this);

        dlg.setTitle("예약확인");
        dlg.setMessage(selectYear + "년" + selectMonth + "월" + selectDay + "일" + str + "가 맞습니까?");
        dlg.setIcon(R.mipmap.ic_launcher);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(),
                        selectYear + "년" + selectMonth + "월" + selectDay + "일" + str +
                                " 예매가완료되었습니다", Toast.LENGTH_LONG).show();
                finish();
            }

        });
        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dlg.show();

    }
}