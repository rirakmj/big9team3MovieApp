package com.cookandroid.big9team3movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private String key = "65f7606d63ef23b56c0e34f0eb4d1270";
    private String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    private ListView listViewMain;
    ArrayAdapter adapter;
    ArrayList<String> title = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listViewMain = findViewById(R.id.listViewMain);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, title);
        listViewMain.setAdapter(adapter);

        new Thread() {
            @Override
            public void run() {
                title.clear();
                Date date = new Date();
                date.setTime(date.getTime() - (1000 * 60 * 60 * 24));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dateStr = sdf.format(date);
                String urlAddr = url + "?key=" + key + "&targetDt=" + dateStr;

                try {
                    URL url = new URL(urlAddr);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while (line != null) {
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }
                    String jsonData = buffer.toString();
                    JSONObject obj = new JSONObject(jsonData);
                    JSONObject boxOfficeResult = (JSONObject) obj.get("boxOfficeResult");
                    JSONArray dailyBoxOfficeList = (JSONArray) boxOfficeResult.get("dailyBoxOfficeList");
                    for (int i = 0; i < dailyBoxOfficeList.length(); i++) {
                        JSONObject temp = dailyBoxOfficeList.getJSONObject(i);
                        String movieNm = temp.getString("movieNm");
                        title.add(movieNm);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (
                        MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (
                        IOException ex) {
                    ex.printStackTrace();
                } catch (
                        JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
}