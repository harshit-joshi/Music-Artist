package com.example.harshitjoshi.musicartist;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArtistList extends ArrayAdapter<Artist> {
    private Activity context;
    private List<Artist> artistList;
    public ArtistList(Activity context,List<Artist> artistList)
    {
        super(context,R.layout.list_layout,artistList);
        this.context=context;
        this.artistList=artistList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View listViewItem=layoutInflater.inflate(R.layout.list_layout,null,true);
        TextView textViewName=listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre=listViewItem.findViewById(R.id.textViewGenre);
        Artist artist=artistList.get(position);
        textViewName.setText(artist.getArtistName());
        textViewGenre.setText(artist.getArtistGenres());
        return listViewItem;
    }
}
