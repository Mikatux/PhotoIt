package io.oversimplified.android.photoit;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mika on 12/23/2015.
 */
public class GameCardAdapter extends RecyclerView.Adapter<GameCardAdapter.ViewHolder> {

    List<GameItem> mItems;

    public GameCardAdapter(List<GameItem> gamesItems) {
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        GameItem nature = mItems.get(i);
        viewHolder.tvName.setText(nature.getParseUser("player1").getUsername());
        viewHolder.tvLevel.setText("lvl " + nature.getLevel());
        viewHolder.imgGame.setImageBitmap(BitmapFactory.decodeFile(nature.getImagePath()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgGame;
        public TextView tvName;
        public TextView tvLevel;

        public ViewHolder(View itemView) {
            super(itemView);
            imgGame = (ImageView) itemView.findViewById(R.id.img_game);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvLevel = (TextView) itemView.findViewById(R.id.tv_level);
        }
    }
}