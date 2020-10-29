package com.application.atplexam.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.application.atplexam.R;
import com.zolad.zoominimageview.ZoomInImageView;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

public class FigureViewController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figure_view);

        Intent intent = getIntent();
        String figureName = intent.getStringExtra("figureName");

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ZoomInImageView imgView = (ZoomInImageView) findViewById(R.id.imageView);
        ZoomInImageViewAttacher mIvAttacter = new ZoomInImageViewAttacher();
        mIvAttacter.attachImageView(imgView);

        int idImage = getApplicationContext().getResources().getIdentifier("file_" + figureName, "drawable", getApplicationContext().getPackageName());
        imgView.setImageResource(idImage);

        mIvAttacter.setZoomable(true);
    }
}