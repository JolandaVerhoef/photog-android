package com.example.exampleapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import today.created.photog.ViewerActivityFragment;

public class ViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        startFullscreen();
        if ( savedInstanceState == null) {
            initializeFragment();
        }
    }

    private void initializeFragment() {
        ViewerActivityFragment fragment = new ViewerActivityFragment();
        Bundle                 bundle = new Bundle();
        bundle.putString("baseUrl", BuildConfig.BASE_URL);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.main_view, fragment).commit();
    }

    private void startFullscreen() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
