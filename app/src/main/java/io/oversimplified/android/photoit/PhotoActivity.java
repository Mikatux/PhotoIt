package io.oversimplified.android.photoit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    int TAKE_PHOTO_CODE = 4242;
    String currentGameId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_photo);
        setLoading();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.v("long click", "long");

                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(" click", "click");

                final PhotoItem newGame = new PhotoItem();
                newGame.setAuthor(ParseUser.getCurrentUser());
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


        ParseQuery<PhotoItem> query = ParseQuery.getQuery(PhotoItem.class);
        query.whereNotEqualTo("author", ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<PhotoItem>() {
            public void done(final List<PhotoItem> photoList, ParseException e) {
                // commentList now has the comments for myPost
                if (e == null) {
                    Log.d("games", "Retrieved " + photoList.size() + " photos");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setPhotos(photoList);

                        }
                    });
                } else {
                    Log.e("gamesfail", " " + e.toString());

                }
            }
        });

    }

    public void setPhotos(List photoList) {

        ProgressBar loading = (ProgressBar) findViewById(R.id.pb_loaging);
        loading.setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PhotoCardAdapter(photoList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setLoading() {
        setPhotos(new ArrayList());

        ProgressBar loading = (ProgressBar) findViewById(R.id.pb_loaging);
        loading.setVisibility(View.VISIBLE);
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
        setLoading();

        if (id == R.id.nav_my_photos) {
            ParseQuery<PhotoItem> query = ParseQuery.getQuery(PhotoItem.class);
            query.whereEqualTo("author", ParseUser.getCurrentUser());

            query.findInBackground(new FindCallback<PhotoItem>() {
                public void done(final List<PhotoItem> photoList, ParseException e) {
                    // commentList now has the comments for myPost
                    if (e == null) {
                        Log.d("games", "Retrieved " + photoList.size() + " photos");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setPhotos(photoList);

                            }
                        });
                    } else {
                        Log.e("gamesfail", " " + e.toString());

                    }
                }
            });

        } else if (id == R.id.nav_best_photos) {

            ParseQuery<Vote> query = ParseQuery.getQuery(Vote.class);
            query.whereEqualTo("like", true);

            query.findInBackground(new FindCallback<Vote>() {
                public void done(final List<Vote> voteList, ParseException e) {
                    // commentList now has the comments for myPost
                    List<PhotoItem> photoList1 = new ArrayList<PhotoItem>();
                    if (e == null) {
                        for (Vote vote : voteList) {
                            if (!photoList1.contains((PhotoItem) vote.getParseObject("photoItem")))
                                photoList1.add((PhotoItem) vote.getParseObject("photoItem"));

                        }
                        final List<PhotoItem> photoList = photoList1;

                        Log.d("games", "Retrieved " + photoList.size() + " photos");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setPhotos(photoList);

                            }
                        });
                    } else {
                        Log.e("gamesfail", " " + e.toString());

                    }
                }
            });


        } else if (id == R.id.nav_my_liked) {

            ParseQuery<Vote> query = ParseQuery.getQuery(Vote.class);
            query.whereEqualTo("author", ParseUser.getCurrentUser());
            query.whereEqualTo("like", true);

            query.findInBackground(new FindCallback<Vote>() {
                public void done(final List<Vote> voteList, ParseException e) {
                    // commentList now has the comments for myPost
                    List<PhotoItem> photoList1 = new ArrayList<PhotoItem>();
                    if (e == null) {
                        for (Vote vote : voteList) {
                            photoList1.add((PhotoItem) vote.getParseObject("photoItem"));

                        }
                        final List<PhotoItem> photoList = photoList1;

                        Log.d("games", "Retrieved " + photoList.size() + " photos");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setPhotos(photoList);

                            }
                        });
                    } else {
                        Log.e("gamesfail", " " + e.toString());

                    }
                }
            });


        } else if (id == R.id.nav_not_rated) {
            ParseQuery<PhotoItem> query = ParseQuery.getQuery(PhotoItem.class);
            query.whereNotEqualTo("author", ParseUser.getCurrentUser());

            query.findInBackground(new FindCallback<PhotoItem>() {
                public void done(final List<PhotoItem> photoList, final ParseException e) {


                    ParseQuery<Vote> query1 = ParseQuery.getQuery(Vote.class);
                    query1.whereEqualTo("author", ParseUser.getCurrentUser());

                    query1.findInBackground(new FindCallback<Vote>() {
                        public void done(final List<Vote> voteList, ParseException e1) {
                            // commentList now has the comments for myPost
                            List<PhotoItem> photoList1 = new ArrayList<PhotoItem>();
                            if (e1 == null) {
                                for (Vote vote : voteList) {
                                    photoList1.add((PhotoItem) vote.getParseObject("photoItem"));

                                }
                                if (e == null) {
                                    for (PhotoItem photo : photoList
                                            ) {
                                        if (photoList1.contains(photo))
                                            photoList.remove(photo);

                                    }
                                    Log.d("games", "Retrieved " + photoList.size() + " photos");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setPhotos(photoList);

                                        }
                                    });
                                } else {
                                    Log.e("gamesfail", " " + e.toString());

                                }
                            } else {
                                Log.e("gamesfail", " " + e1.toString());

                            }
                        }
                    });


                    // commentList now has the comments for myPost

                }
            });

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            ParseUser.logOutInBackground();
            finish();
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

                ParseQuery<PhotoItem> query = ParseQuery.getQuery(PhotoItem.class);
                try {
                    PhotoItem test = query.get(currentGameId);
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
                        Log.v("img", "saving img");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
/*
                Intent drawIntent = new Intent(PhotoActivity.this, DrawActivity.class);
                drawIntent.putExtra("GameId", currentGameId); //Optional parameters
                PhotoActivity.this.startActivityForResult(drawIntent, DRAW_PHOTO_CODE);
*/
            } else {
                Toast.makeText(this, "Fail to Take picture", Toast.LENGTH_LONG);
                Log.d("Fail take pic Code", resultCode + "!=" + RESULT_OK + data);
            }

        } else
            Log.d("Fail other Code", resultCode + "!=" + RESULT_OK + data);

    }
}
