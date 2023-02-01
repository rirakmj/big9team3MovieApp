package com.cookandroid.big9team3movieapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Movie> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);

        new Description().execute();
    }

    private class Description extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://movie.naver.com/movie/running/current.naver").get();
                Elements mElementDataSize = doc.select("ul[class=lst_detail_t1]").select("li");
                int mElementSize = mElementDataSize.size();

                for(Element elem : mElementDataSize) {
                    String myTitle = elem.select("li dt[class=tit] a").text();
                    String myLink = elem.select("li div[class=thumb] a").attr("href");
                    String myImgUrl = elem.select("li div[class=thumb] a img").attr("src");
                    Element rElem = elem.select("dl[class=info_txt1] dt").next().first();
                    String myRelease = rElem.select("dd").text();
                    Element dElem = elem.select("dt[class=tit_t2]").next().first();
                    String myDirector = "감독: " + dElem.select("a").text();

                    mList.add(new Movie(myTitle, myImgUrl, myLink, myRelease, myDirector));
                }
                Log.d("debug: ", "mList " + mElementDataSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            MovieAdapter movieAdapter = new MovieAdapter(mList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(movieAdapter);

            progressDialog.dismiss();

        }
    }
}
