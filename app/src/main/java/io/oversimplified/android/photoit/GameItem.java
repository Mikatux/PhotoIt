package io.oversimplified.android.photoit;

import android.os.Environment;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;

/**
 * Created by Mika on 12/23/2015.
 */
@ParseClassName("GameItem")
public class GameItem extends ParseObject {

    private ParseUser player1;
    private ParseUser player2;
    private String word;
    private int mLevel;

    public ParseUser getPlayer1() {
        return player1;
    }

    public void setPlayer1(ParseUser user) {
        this.player1 = user;
        put("player1",user);

    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        this.mLevel = level;
        put("level",level);

    }

    public String getImagePath() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        File myDir = new File(root + "/photoIt/");
        myDir.mkdirs();
        return myDir.getAbsolutePath() + "/" + this.getObjectId() + ".photo";
    }

    public ParseUser getPlayer2() {
        return player2;
    }

    public void setPlayer2(ParseUser user) {
        this.player2 = user;
        put("player2",user);
    }

    public boolean imgExist() {
        File file = new File(this.getImagePath());
        return file.exists();
    }
}
