package com.example.exampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import today.created.photog.ViewerActivityFragment;

public class ViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        if ( savedInstanceState == null) {
            ViewerActivityFragment fragment = new ViewerActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putString("baseUrl", BuildConfig.BASE_URL);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.main_view, fragment).commit();
        }
    }
}
