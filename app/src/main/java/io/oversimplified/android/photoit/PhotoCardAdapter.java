package io.oversimplified.android.photoit;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Mika on 12/23/2015.
 */
public class PhotoCardAdapter extends RecyclerView.Adapter<PhotoCardAdapter.ViewHolder> {

    List<PhotoItem> mItems;

    public PhotoCardAdapter(List<PhotoItem> gamesItems) {
        super();
        mItems = gamesItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final PhotoItem photo = mItems.get(i);

        try {
            viewHolder.tvName.setText(photo.fetchIfNeeded().getParseUser("author").fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            // e.printStackTrace();
        }
        viewHolder.tvDate.setText(photo.getDate());
        Bitmap bitmap = null;

        bitmap = photo.getImgBitmap();

        viewHolder.img.setImageBitmap(bitmap);

        if (ParseUser.getCurrentUser().equals((photo.getParseUser("author")))) {
            viewHolder.ll_disLike.setVisibility(View.GONE);
        } else {
            viewHolder.ll_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vote vote = new Vote(photo, ParseUser.getCurrentUser(), true);
                    vote.saveInBackground();
                    viewHolder.ll_disLike.setVisibility(View.GONE);
                    v.setOnClickListener(null);
                    viewHolder.ll_disLike.setOnClickListener(null);
                }
            });
            viewHolder.ll_disLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vote vote = new Vote(photo, ParseUser.getCurrentUser(), false);
                    vote.saveInBackground();

                    viewHolder.ll_like.setVisibility(View.GONE);
                    v.setOnClickListener(null);
                    viewHolder.ll_like.setOnClickListener(null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView tvName;
        public TextView tvDate;
        public LinearLayout ll_disLike;
        public LinearLayout ll_like;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_photo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            ll_disLike = (LinearLayout) itemView.findViewById(R.id.view_dislike);
            ll_like = (LinearLayout) itemView.findViewById(R.id.view_like);

        }
    }
}