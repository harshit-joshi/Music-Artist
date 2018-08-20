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

public class TrackList extends ArrayAdapter
{
    private Activity context;
    private List<Track> trackList;
    public TrackList(Activity context,List<Track> trackList)
    {
        super(context,R.layout.list_layout,trackList);
        this.context=context;
        this.trackList=trackList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View listViewItem=layoutInflater.inflate(R.layout.list_layout,null,true);
        TextView textViewName=listViewItem.findViewById(R.id.textViewName);
        TextView textViewRating=listViewItem.findViewById(R.id.textViewGenre);
        Track track=trackList.get(position);
        textViewName.setText(track.getTrackName());
        textViewRating.setText(String.valueOf(track.getTrackRating()));
        return listViewItem;
    }
}
