package io.oversimplified.android.photoit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    int TAKE_PHOTO_CODE = 4242;
    int DRAW_PHOTO_CODE = 4243;
    String currentGameId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final GameItem newGame = new GameItem();
                newGame.setLevel(1);
                newGame.setPlayer1(ParseUser.getCurrentUser());
                newGame.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            currentGameId = newGame.getObjectId();
                            File file = new File(newGame.getImagePath());
                            if (file.exists()) file.delete();

                            Uri outputFileUri = Uri.fromFile(file);

                            Log.v("path", newGame.getImagePath());
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                        }
                    }
                });


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ParseQuery<GameItem> query = ParseQuery.getQuery(GameItem.class);
        // query.whereEqualTo("player1", ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<GameItem>() {
            public void done(final List<GameItem> gameList, ParseException e) {
                // commentList now has the comments for myPost
                if (e == null) {
                    Log.d("games", "Retrieved " + gameList.size() + " games");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setGames(gameList);

                        }
                    });
                } else {
                    Log.e("gamesfail", " " + e.toString());

                }
            }
        });

    }

    public void setGames(List gameList) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GameCardAdapter(gameList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE) {
            if (resultCode == RESULT_OK) {

                ParseQuery<GameItem> query = ParseQuery.getQuery(GameItem.class);
                try {
                    GameItem test = query.get(currentGameId);
                    if (test.imgExist()) {
                        File img = new File(test.getImagePath());
                        int size = (int) img.length();
                        byte[] imgBytes = new byte[size];
                        try {
                            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(img));
                            buf.read(imgBytes, 0, imgBytes.length);
                            buf.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        ParseFile file = new ParseFile(test.getObjectId() + ".photo", imgBytes);
                        test.put("image", file);
                        test.saveInBackground();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent drawIntent = new Intent(GameActivity.this, DrawActivity.class);
                drawIntent.putExtra("imgId", currentGameId); //Optional parameters
                GameActivity.this.startActivityForResult(drawIntent, DRAW_PHOTO_CODE);

            } else {
                Toast.makeText(this, "Fail to Take picture", Toast.LENGTH_LONG);
                Log.d("Fail take pic Code", resultCode + "!=" + RESULT_OK + data);
            }

        } else if (requestCode == DRAW_PHOTO_CODE) {
            if (resultCode == RESULT_OK) {
                //send draw

                Toast.makeText(this, "Sending drawing", Toast.LENGTH_LONG);
                Log.d("Sending pic Code", resultCode + "!=" + RESULT_OK + data);

            } else {
                Toast.makeText(this, "Fail to Draw", Toast.LENGTH_LONG);
                Log.d("Fail draw Code", resultCode + "!=" + RESULT_OK + data);
            }
        } else
            Log.d("Fail other Code", resultCode + "!=" + RESULT_OK + data);

    }
}
