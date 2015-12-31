package io.oversimplified.android.photoit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BrushView view = new BrushView(this);

        setContentView(view);
        Intent intent = getIntent();
        String currentGameId = intent.getStringExtra("GameId");
        ParseQuery<GameItem> query = ParseQuery.getQuery(GameItem.class);
        Bitmap bitmap = null;
        try {
            GameItem test = query.get(currentGameId);
            if (test.imgExist()) {
                bitmap = test.getImgBitmap();
            } else
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_camera);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        view.setBackground(d);

        addContentView(view.btnEraseAll, view.params);
        addContentView(view.btnDone, view.params);
    }

    @Override
    protected void onPause() {

        super.onPause();

        finish();

    }

}
