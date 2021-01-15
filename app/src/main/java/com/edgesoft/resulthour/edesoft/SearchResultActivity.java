package com.edgesoft.resulthour.edesoft;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class SearchResultActivity extends AppCompatActivity {

    private TextInputEditText nameEdt,rollNoEdt;
    private Button searchByNameBtn,searchByRollNoBtn;
    private String exam_id;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        TextView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarTitle.setText("Search Result");
        appBarLeft.setText("Back");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        allInitializations();

        searchByNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(nameEdt.getText())) {
                    nameEdt.setError("Please Enter Name");
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, ViewResultActivity.class);
                    intent.putExtra("url", "http://www.resulthour.com/Home/byname?exam=" + exam_id + "&uid="+uid+"&name=" + nameEdt.getText());
                    startActivity(intent);
                }
            }
        });

        searchByRollNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(rollNoEdt.getText())) {
                    rollNoEdt.setError("Please Enter Roll Number");
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, ViewResultActivity.class);
                    intent.putExtra("url", "http://www.resulthour.com/Home/byroll?exam=" + exam_id + "&uid="+uid+"&rollno=" + rollNoEdt.getText());
                    startActivity(intent);
                }
            }
        });
    }

    private void allInitializations() {
        nameEdt = findViewById(R.id.search_result_name);
        rollNoEdt = findViewById(R.id.search_result_roll_no);
        searchByNameBtn = findViewById(R.id.search_result_submit_name);
        searchByRollNoBtn = findViewById(R.id.search_result_submit_roll_no);
        Intent intent = getIntent();
        exam_id = intent.getStringExtra("exam_id");
        uid = intent.getStringExtra("uid");
    }
}