package io.oversimplified.android.photoit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by Mika on 12/23/2015.
 */
public class CatItem {

    private String date ;
    private String imgUrl;
    private Bitmap imgBitmap;
    private Context ctx;
    private Boolean loading = false;

    public CatItem(String imgUrl, String date, Context ctx){

        this.date = date;
        this.imgUrl = imgUrl;
        this.ctx = ctx;
    }
    public CatItem(JSONObject json, Context ctx){

        try {
            this.date = json.getString("date");
            this.imgUrl = "http://thecatapi.com/api/images/get?id="+ json.getString("id");
            this.ctx = ctx;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getImgUrl(){

        return imgUrl;
    }
    public String getDate(){

        return date;
    }



    public Bitmap getBitmap() {
        if(this.imgBitmap != null){
            return imgBitmap;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(loading){
                    return;
                }
                loading =true;

                try {
                    Log.v("load","loading ");

                    URL url = new URL(getImgUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    imgBitmap = BitmapFactory.decodeStream(input);
                    ((HolidayActivity)ctx).reloadCard();
                    Log.v("load", "loading succes " + imgBitmap.toString());

                } catch (IOException e) {
                    // Log exception
                    Log.v("load","loading fail");
                }
                loading =false;

            }
        }).start();

        return null;
    }


}
