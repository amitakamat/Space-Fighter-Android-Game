package com.example.amitakamat.androidgameapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HighScore extends AppCompatActivity implements View.OnClickListener {
    TextView textView,textView2,textView3,textView4;
    private ImageButton buttonBack;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //setting the values to the textViews
        textView.setText("1.  "+sharedPreferences.getInt("score1",0));
        textView2.setText("2.  "+sharedPreferences.getInt("score2",0));
        textView3.setText("3.  "+sharedPreferences.getInt("score3",0));
        textView4.setText("4.  "+sharedPreferences.getInt("score4",0));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        buttonBack = (ImageButton) findViewById(R.id.buttonBack);

        //adding a click listener
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HighScore.this, MainActivity.class));
    }

    @Override
    public void onClick(View v) {

        if (v == buttonBack) {
            startActivity(new Intent(HighScore.this, MainActivity.class));
        }
    }
}
