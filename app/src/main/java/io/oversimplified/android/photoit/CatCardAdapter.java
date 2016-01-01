package io.oversimplified.android.photoit;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mika on 12/23/2015.
 */
public class CatCardAdapter extends RecyclerView.Adapter<CatCardAdapter.ViewHolder> {

    List<CatItem> mItems;

    public CatCardAdapter(List<CatItem> catsItems) {
        super();
        mItems = catsItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_cat, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CatItem cat = mItems.get(i);
        viewHolder.tvName.setText(cat.getDate());
        Bitmap bitmap = cat.getBitmap();

        if(bitmap != null)
            viewHolder.imgGame.setImageBitmap(bitmap);
        else
        {
            viewHolder.imgGame.setImageBitmap(null);
            viewHolder.imgGame.setBackgroundColor(Color.parseColor("#d32f2f"));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgGame;
        public TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgGame = (ImageView) itemView.findViewById(R.id.img_cat);
            tvName = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}