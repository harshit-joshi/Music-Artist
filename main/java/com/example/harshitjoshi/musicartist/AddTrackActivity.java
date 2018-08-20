package com.example.harshitjoshi.musicartist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity
{
    TextView textArtistName;
    EditText editTrackName;
    SeekBar seekBarRating;
    Button buttonAddTrack;
    ListView listViewTrack;
    DatabaseReference databaseTracks;

    List<Track> tracks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
        textArtistName=findViewById(R.id.textViewArtistName);
        editTrackName=findViewById(R.id.editTextTrackName);
        seekBarRating=findViewById(R.id.seekBarRating);
        buttonAddTrack=findViewById(R.id.addTrackButton);
        listViewTrack=findViewById(R.id.listViewTrack);
        tracks=new ArrayList<>();
        Intent intent=getIntent();
        String id=intent.getStringExtra(MainActivity.ARTIST_ID);
        String name=intent.getStringExtra(MainActivity.ARTIST_NAME);
        textArtistName.setText(name);
        databaseTracks= FirebaseDatabase.getInstance().getReference("Tracks").child(id);
        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrack();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tracks.clear();
                for(DataSnapshot trackSnaphsot:dataSnapshot.getChildren())
                {
                    Track track=trackSnaphsot.getValue(Track.class);
                    tracks.add(track);
                }
                TrackList trackListAdapter=new TrackList(AddTrackActivity.this,tracks);
                listViewTrack.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveTrack()
    {
        String trackName=editTrackName.getText().toString().trim();
        int ratting=seekBarRating.getProgress();
        if(!TextUtils.isEmpty(trackName))
        {
            String id=databaseTracks.push().getKey();
            Track track=new Track(id,trackName,ratting);
            databaseTracks.child(id).setValue(track);
            Toast.makeText(this,"Track added",Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(this,"You should enter track",Toast.LENGTH_SHORT).show();
        }
    }
}
