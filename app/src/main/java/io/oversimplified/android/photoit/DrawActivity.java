package io.oversimplified.android.photoit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class DrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BrushView view = new BrushView(this);

        setContentView(view);
        Intent intent = getIntent();
        String path = intent.getStringExtra("imgPath");
        Drawable d = Drawable.createFromPath(path);
        view.setBackground(d);

        addContentView(view.btnEraseAll, view.params);
    }

    @Override
    protected void onPause() {

        super.onPause();

        finish();

    }

}
