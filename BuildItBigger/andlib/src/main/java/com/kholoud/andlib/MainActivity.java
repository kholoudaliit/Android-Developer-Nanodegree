package com.kholoud.andlib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public TextView jokeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jokeactivity_main);

        String jokeStr = getIntent().getStringExtra("joke");
        jokeView = findViewById(R.id.joktext);
        jokeView.setText(jokeStr + "");

    }
}
