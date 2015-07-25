package com.example.exampleapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import today.created.photog.PhotogFragment;

public class ViewerActivity extends AppCompatActivity {

    private PhotogFragment photogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        startFullscreen();
        photogFragment = (PhotogFragment) getSupportFragmentManager().findFragmentByTag("photog");
        if ( photogFragment == null) {
            initializeFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if(! photogFragment.onBackPressed()) {
            finish();
        }
    }

    private void initializeFragment() {
        photogFragment = new PhotogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("baseUrl", BuildConfig.BASE_URL);
        photogFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.main_view, photogFragment, "photog").commit();
    }

    private void startFullscreen() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
