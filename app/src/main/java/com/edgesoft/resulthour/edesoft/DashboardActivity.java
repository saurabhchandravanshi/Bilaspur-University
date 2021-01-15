package com.edgesoft.resulthour.edesoft;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    private CardView viewResultCard,officialWebCard,facebookCard,instagramCard
            ,moreAppsCard,devWebCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        TextView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarTitle.setText("Dashboard");
        appBarLeft.setText("Exit");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        allInitialization();

        viewResultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,HomeActivity.class));
            }
        });
        officialWebCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchChromeTab("https://www.bilaspuruniversity.ac.in/");
            }
        });

        facebookCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchChromeTab("https://facebook.com/i3developer");
            }
        });
        instagramCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchChromeTab("https://instagram.com/i3developer");
            }
        });
        moreAppsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchChromeTab("https://play.google.com/store/apps/dev?id=9018600825061407450");
            }
        });
        devWebCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchChromeTab("https://i3developer.com");
            }
        });
    }

    private void allInitialization() {
        viewResultCard = findViewById(R.id.dashboard_results);
        officialWebCard = findViewById(R.id.dashboard_official_website);
        facebookCard = findViewById(R.id.dashboard_facebook);
        instagramCard = findViewById(R.id.dashboard_instagram);
        moreAppsCard = findViewById(R.id.dashboard_more_apps);
        devWebCard = findViewById(R.id.dashboard_dev_website);
    }

    private void launchChromeTab(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
        builder.setToolbarColor(getResources().getColor(android.R.color.white));
    }
}
