package com.edgesoft.resulthour.edesoft;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<Result> resultList = new ArrayList<>();
    private ImageView loaderImage;
    private AnimationDrawable animationDrawable;
    private FrameLayout loaderFrame;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        TextView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarTitle.setText("Results");
        appBarLeft.setText("Back");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        allInitialization();
        new GetResults().execute("https://www.resulthour.com/cg/bilaspur-university");
    }

    private void allInitialization() {
        loaderImage = findViewById(R.id.home_loader);
        animationDrawable = (AnimationDrawable)loaderImage.getBackground();
        loaderFrame = findViewById(R.id.home_loader_frame);
        recyclerView = findViewById(R.id.home_recycler);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ResultListAdapter(resultList);
    }


    class GetResults extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
           animationDrawable.start();
        }

        @Override
        protected String doInBackground(String... strings) {
            String link = strings[0];
            try {
                org.jsoup.nodes.Document document = Jsoup.connect(link).get();
                for (org.jsoup.nodes.Element div : document.select("div[class=list-group]")) {
                    for (org.jsoup.nodes.Element row : div.select("a[title]")) {
                        String date = row.children().select("b[class=date]").text();
                        String title = row.ownText();
                        String url = row.attr("href");
                        int lastIndex = url.lastIndexOf(".");
                        int startIndex = url.lastIndexOf("/");
                        String result_id = url.substring(startIndex+1,lastIndex);
                        Result result = new Result(result_id,title,date);
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
        protected void onPostExecute(final String result) {
            animationDrawable.stop();
            loaderFrame.setVisibility(View.GONE);
            if(resultList.size() > 0) {
                loadRecyclerView();
            } else {
                showToast(getApplicationContext(),"Failed to Get Results Please Try Again");
            }
        }
    }

    private void loadRecyclerView() {
       recyclerView.setAdapter(adapter);
       recyclerView.setLayoutManager(layoutManager);
    }
    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}