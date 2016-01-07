package io.oversimplified.android.photoit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseQuery;

public class DrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BrushView view = new BrushView(this);

        setContentView(view);
        Intent intent = getIntent();
        String currentGameId = intent.getStringExtra("GameId");
        ParseQuery<PhotoItem> query = ParseQuery.getQuery(PhotoItem.class);
        Bitmap bitmap = null;
        try {
            PhotoItem test = query.get(currentGameId);
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
