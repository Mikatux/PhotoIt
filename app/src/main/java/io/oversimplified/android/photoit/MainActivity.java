package io.oversimplified.android.photoit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int TAKE_PHOTO_CODE = 4242;
    public static int count = 0;
    String imgPath = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        verifyStoragePermissions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOutInBackground();
                Snackbar.make(view, "You're logout", Snackbar.LENGTH_LONG)
                        .setAction("Logout", null).show();

            }
        });


        Button draww = (Button) findViewById(R.id.drawbutton);
        draww.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });


        Button capture = (Button) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                // here,counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
                count++;

                String state = Environment.getExternalStorageState();
                if(state.equals("mounted")){
                    Log.v("mounted", "ok");

                }
                else{
                    Log.v("mounted", "not ok");

                }
                String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                File myDir = new File(root + "/saved_images");
                myDir.mkdirs();
                String fname = "Image-" + count + ".jpg";
                File file = new File(root, fname);
                if (file.exists ()) file.delete ();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Log.v("file error", e.toString());
                }

                Uri outputFileUri = Uri.fromFile(file);
                imgPath = file.getAbsolutePath();

                Log.v("path",imgPath);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 42: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.v("permission","ok");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.v("permission","not ok");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent drawIntent = new Intent(MainActivity.this, DrawActivity.class);
                    drawIntent.putExtra("imgPath", imgPath); //Optional parameters
                    MainActivity.this.startActivity(drawIntent);
                    //finish();
                }
            });

        } else {
            Toast.makeText(this, "Fail to Take picture", Toast.LENGTH_LONG);
            Log.d("Code", resultCode + "!=" + RESULT_OK + data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent drawIntent = new Intent(MainActivity.this, DrawActivity.class);
                    drawIntent.putExtra("imgPath", imgPath); //Optional parameters
                    MainActivity.this.startActivity(drawIntent);
                    //finish();
                }
            });
        }
    }


    public void verifyStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},42);

            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


