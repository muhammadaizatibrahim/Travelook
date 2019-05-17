package com.example.aizat.travelook_v1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aizat.travelook_v1.FavouriteModel.FavouriteAttr;

import java.util.List;

public class FavoritePlacesAdapter extends RecyclerView.Adapter<FavoritePlacesAdapter.FavoriteViewHolder> {

    private List<FavouriteAttr> favouriteAttrList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) { mListener = listener; }


    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView placeName;
        TextView placeAddress;

        public FavoriteViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeAddress = (TextView) itemView.findViewById(R.id.placeAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }

                    }
                }
            });

        }
    }

    public FavoritePlacesAdapter (List<FavouriteAttr> fav){
        favouriteAttrList = fav;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favorite_layout,viewGroup,false);
        FavoriteViewHolder favoriteViewHolder = new FavoriteViewHolder(view,mListener);
        return favoriteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {

        FavouriteAttr favorite = favouriteAttrList.get(i);

        favoriteViewHolder.placeName.setText(favorite.name);
        favoriteViewHolder.placeAddress.setText(favorite.addr);

    }

    @Override
    public int getItemCount() {
        return favouriteAttrList.size();
    }

}
