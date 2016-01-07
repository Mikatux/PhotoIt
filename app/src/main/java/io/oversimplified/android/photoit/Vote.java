package io.oversimplified.android.photoit;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Mika on 1/6/2016.
 */
@ParseClassName("Vote")

public class Vote extends ParseObject {
    private ParseUser author;
    private PhotoItem photoItem;
    private boolean like;
    private Date date;

    public ParseUser getAuthor() {
        return author;
    }

    public Vote(){

    }
    public Vote(PhotoItem photoItem, ParseUser user, Boolean like) {
        setAuthor(user);
        setPhotoItem(photoItem);
        setLike(like);
        this.date = new Date();
    }

    public void setAuthor(ParseUser author) {
        this.author = author;
        put("author", author);
    }

    public PhotoItem getPhotoItem() {
        return photoItem;
    }

    public void setPhotoItem(PhotoItem photoItem) {
        this.photoItem = photoItem;
        put("photoItem", photoItem);

    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        put("like", like);
        this.like = like;
    }
}
