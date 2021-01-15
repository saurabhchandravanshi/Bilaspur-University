package com.edgesoft.resulthour.edesoft;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;

public class ResultWebViewActivity extends AppCompatActivity {
    private String url;
    private WebView webView;
    private String html;
    private FrameLayout loaderFrame;
    private ImageView loaderImage;
    private AnimationDrawable animationDrawable;
    private FloatingActionButton dlFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_web_view);

        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        TextView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarTitle.setText("Result");
        appBarLeft.setText("Back");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        Intent intent = getIntent();
        url = "http://www.resulthour.com"+intent.getStringExtra("url");
        webView = findViewById(R.id.result_web_view);
        webView.setWebViewClient(new MyWebViewClient());
        loaderFrame = findViewById(R.id.result_web_view_loader_frame);
        loaderImage = findViewById(R.id.result_web_view_loader);
        dlFab = findViewById(R.id.result_web_view_dl_fab);
        animationDrawable = (AnimationDrawable)loaderImage.getBackground();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        new GetResultTask().execute(url);

        dlFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printDocumentFromWebView(webView);
            }
        });
    }
    class GetResultTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            animationDrawable.start();
        }

        @Override
        protected String doInBackground(String... strings) {
            String link = strings[0];
            try {
                org.jsoup.nodes.Document document = Jsoup.connect(link).get();

                document.getElementsByTag("tr").last().remove();
                String coreHtml = document.select("div[id=resultdiv]").html().replaceAll("&amp;"," AND ").replaceAll("amp;","");
                html = "<html><style>" +"img {display:none;}"+"td {color:black;font-family:verdana;text-transform:uppercase}"
                        +"h1,h4 {color:dodgerblue;font-family:verdana;text-align:center;text-transform:uppercase}" +
                        "" + "td {border:1px solid darkslategray;padding:5px} table {width:100%;} b,small {font-family:verdana; color:red}"+
                        "</style><body>"+coreHtml+"</body></html>";
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            animationDrawable.stop();
            loaderFrame.setVisibility(View.GONE);
            if(html==null) {
                showToast(getApplicationContext(),"No Internet Connection.");
                return;
            }
            webView.loadData(html+"<br>"+
                    "<small><b>DISCLAIMER: PLEASE CONTACT WITH EXAMINATION AUTHORITY IN CASE OF ANY PROBLEM.</b></small>", "text/html", "UTF-8");
        }
    }
    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading (WebView webView, String url){

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loaderFrame.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

    }
    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void printDocumentFromWebView(final WebView webView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter documentAdapter = webView.createPrintDocumentAdapter();
            String jonName = "Result_From_Bilaspur_University";
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
            PrintJob printJob = printManager.print(jonName,documentAdapter,builder.build());
            if(printJob.isCompleted()) {
                showToast(getApplicationContext(),"Document saved in Documents.");
            }
            else if(printJob.isFailed()) {
                showToast(getApplicationContext(),"Failed to Print document.");
            }
        }
        else {
            showToast(getApplicationContext(),"Operation Cancelled.");
        }
    }
}

