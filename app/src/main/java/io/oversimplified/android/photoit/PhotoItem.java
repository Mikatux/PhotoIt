package io.oversimplified.android.photoit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.parse.GetFileCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Mika on 12/23/2015.
 */
@ParseClassName("PhotoItem")
public class PhotoItem extends ParseObject {

    static int width = 300;
    private ParseUser author;
    private String date;
    private Bitmap img;

    public PhotoItem() {

    }

    public ParseUser getAuthor() {
        return author;
    }

    public void setAuthor(ParseUser user) {
        this.author = user;
        put("author", user);

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        put("date", date);

    }

    public String getImagePath() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        File myDir = new File(root + "/photoIt/");
        myDir.mkdirs();
        return myDir.getAbsolutePath() + "/" + this.getObjectId() + ".photo";
    }

    public boolean imgExist() {
        File file = new File(this.getImagePath());
        return file.exists();
    }

    public Bitmap getImgBitmap() {

        if (imgExist()) {
            img = BitmapFactory.decodeFile(getImagePath());
            double ratio = (double) PhotoItem.width / (double) img.getWidth();
            img = Bitmap.createScaledBitmap(img, PhotoItem.width, (int) (img.getHeight() * ratio), false);
        } else {
            img = null;
            // parse dl img
            Log.v("download", "image");
            this.getParseFile("image").getFileInBackground(new GetFileCallback() {
                @Override
                public void done(File file, ParseException e) {
                    Log.v("download", "image finish"+file.getAbsolutePath());

                    File to = new File(getImagePath());

                    if (file.exists()) {
                        moveFile(file.getAbsolutePath(),to.getAbsolutePath());
                        Log.v("download", "");
                    }
                    img = BitmapFactory.decodeFile(getImagePath());
                    double ratio = (double) PhotoItem.width / (double) img.getWidth();
                    img = Bitmap.createScaledBitmap(img, PhotoItem.width, (int) (img.getHeight() * ratio), false);

                }
            });
        }
        return img;
    }

    private void moveFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
}
