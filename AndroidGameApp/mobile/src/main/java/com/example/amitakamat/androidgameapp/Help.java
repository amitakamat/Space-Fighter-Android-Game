package com.example.amitakamat.androidgameapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Help extends AppCompatActivity implements View.OnClickListener {
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        buttonBack = (ImageButton) findViewById(R.id.buttonBack);

        //adding a click listener
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Help.this, MainActivity.class));
    }

    @Override
    public void onClick(View v) {

        if (v == buttonBack) {
            startActivity(new Intent(Help.this, MainActivity.class));
        }
    }
}
