package com.application.atplexam.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.application.atplexam.R;

public class SettingController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences sharedPreferences = getSharedPreferences("Score", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final Button sound = (Button) findViewById(R.id.play_sound);
        Button reset = (Button) findViewById(R.id.reset);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("Computer", 0);
                editor.putInt("Sports", 0);
                editor.putInt("Inventions", 0);
                editor.putInt("General", 0);
                editor.putInt("Science", 0);
                editor.putInt("English", 0);
                editor.putInt("Books", 0);
                editor.putInt("Maths", 0);
                editor.putInt("Capitals", 0);
                editor.putInt("Currency", 0);
                editor.commit();
                Snackbar.make(v, "Highscore Reseted Successfully", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = getSharedPreferences("Score", Context.MODE_PRIVATE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sp = getSharedPreferences("Score", Context.MODE_PRIVATE);
    }

}

