package com.edgesoft.resulthour.edesoft;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ViewResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Result> resultList = new ArrayList<>();
    private FrameLayout loaderFrame;
    private ImageView loaderImage;
    private AnimationDrawable animationDrawable;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        TextView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarTitle.setText("Matched Results");
        appBarLeft.setText("Back");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        allInitializations();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        new GetMatchedResult().execute(url);
    }

    private void allInitializations() {
        recyclerView = findViewById(R.id.view_result_recycler);
        adapter = new ViewResultAdapter(resultList);
        layoutManager = new LinearLayoutManager(this);
        loaderImage = findViewById(R.id.view_result_loader);
        animationDrawable = (AnimationDrawable) loaderImage.getBackground();
        loaderFrame = findViewById(R.id.view_result_loader_frame);
    }

    class GetMatchedResult extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            animationDrawable.start();
        }

        @Override
        protected String doInBackground(String... strings) {
            String link = strings[0];
            try {
                org.jsoup.nodes.Document document = Jsoup.connect(link).get();
                for (org.jsoup.nodes.Element table : document.select("table[class=table table-striped]")) {
                    for (org.jsoup.nodes.Element row : table.select("tbody").select("tr")) {
                        Elements tds = row.select("td");
                        String name = tds.get(1).text();
                        String roll = tds.get(2).text();
                        String url  = tds.get(3).children().select("a").attr("href");
                        Result result = new Result(roll,name,url);
                        resultList.add(result);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            animationDrawable.stop();
            loaderFrame.setVisibility(View.GONE);
            if(resultList.size()>0) {
                loadRecyclerView();
            }
            else {
                showToast(getApplicationContext(),"No Result Found Please Try Again");
            }
        }
    }

    public void loadRecyclerView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}