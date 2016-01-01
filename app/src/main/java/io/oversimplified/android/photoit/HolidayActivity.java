package io.oversimplified.android.photoit;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HolidayActivity extends AppCompatActivity {

    static RecyclerView mRecyclerView;
    static RecyclerView.LayoutManager mLayoutManager;
    static RecyclerView.Adapter mAdapter;
    static int today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_holiday);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


                List<CatItem> catList = new ArrayList<>();

                String catsOfTheDays = new String();

                try {
                    // Create a URL for the desired page
                    URL url = new URL("http://photoit.oversimplified.io/cat.json");

                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line;
                    StringBuilder total = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        total.append(line);
                    }
                    catsOfTheDays = total.toString();
                    in.close();
                } catch (MalformedURLException e) {
                    Log.e("coucou",e.toString());
                } catch (IOException e) {
                    Log.e("coucou2",e.toString());

                }


                boolean scrolled = false;
                try {
                    JSONArray jsonArray = new JSONArray(catsOfTheDays);
                    Log.v("coucou", jsonArray.length() + "");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        catList.add(new CatItem(jsonObject,this));
                        if( scrolled == false &&jsonObject.getString("id") != "null"){
                            scrolled = true;
                            today=i;
                            scrollTo(today);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mAdapter = new CatCardAdapter(catList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });


    }
    public void reloadCard(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setAdapter(mAdapter);
                scrollTo(today);

            }
        });

    }
    public void scrollTo(final int i ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                 mRecyclerView.scrollToPosition(7);
            }
        });
             }

         }
