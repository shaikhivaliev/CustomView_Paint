package com.elegion.a5_11paint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomView;
    private PaintView mPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPaintView = findViewById(R.id.paint_view);
        bottomView = findViewById(R.id.bottom_bar);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mPaintView.init(metrics);

        bottomView = findViewById(R.id.bottom_bar);
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.clear:
                        mPaintView.clear();
                        return true;
                    case R.id.brush:
                        mPaintView.brush();
                        return true;
                    case R.id.color:
                        mPaintView.color();
                        return true;
                    case R.id.back:
                        mPaintView.back();
                        return true;
                }
                return false;
            }
        });

    }
}
