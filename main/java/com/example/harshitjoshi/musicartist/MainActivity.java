package com.example.harshitjoshi.musicartist;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ARTIST_NAME="artistname";
    public static final String ARTIST_ID="artistid";
    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;
    DatabaseReference databaseArtist;
    List<Artist> artistList;
    ListView listViewArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextName=findViewById(R.id.editTextName);
        buttonAdd=findViewById(R.id.addArtistButton);
        spinnerGenres=findViewById(R.id.spinnerGenres);
        listViewArtist=findViewById(R.id.listViewArtist);
        //Firebase
        databaseArtist= FirebaseDatabase.getInstance().getReference("Artist");
        artistList=new ArrayList<>();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addArtist();
            }
        });
        listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist=artistList.get(position);
                Intent intent=new Intent(getApplicationContext(),AddTrackActivity.class);
                intent.putExtra(ARTIST_ID,artist.getArtistId());
                intent.putExtra(ARTIST_NAME,artist.getArtistName());
                startActivity(intent);
            }
        });
        listViewArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist=artistList.get(position);
                showUpdateDialog(artist.getArtistId(),artist.getArtistName());
                return true;
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artistList.clear();
                for(DataSnapshot artistSnapshot:dataSnapshot.getChildren())
                {

                    Artist artist=artistSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }
                ArtistList adapter=new ArtistList(MainActivity.this,artistList);
                listViewArtist.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void showUpdateDialog(final String artistId, String artistName)
    {
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogView);
       // final TextView textViewName=dialogView.findViewById(R.id.dialogTextViewName);
        final EditText editTextName=dialogView.findViewById(R.id.dialogEditTextName);
        final Button buttonUpdate=dialogView.findViewById(R.id.dialogUpdateButton);
        final Spinner spinnerGenres=dialogView.findViewById(R.id.dialogGenres);
        final Button buttonDelete=dialogView.findViewById(R.id.dialogDeleteButton);
        dialogBuilder.setTitle("Updating Artist: " +artistName.toUpperCase());
        final AlertDialog alertDialog=dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editTextName.getText().toString().trim();
                String genre=spinnerGenres.getSelectedItem().toString();
                if(TextUtils.isEmpty(name))
                {
                    editTextName.setError("Name Required");
                    return;
                }
                updateArtist(artistId,name,genre);
                alertDialog.dismiss();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(artistId);
            }
        });


    }

    private void deleteArtist(String artistId)
    {
        DatabaseReference databaseReferenceArtist=FirebaseDatabase.getInstance().getReference("Artist").child(artistId);
        DatabaseReference databaseReferenceTrack=FirebaseDatabase.getInstance().getReference("Tracks").child(artistId);
        databaseReferenceArtist.removeValue();
        databaseReferenceTrack.removeValue();
        Toast.makeText(this,"Artist is deleted",Toast.LENGTH_SHORT).show();
    }

    private boolean updateArtist(String id,String name,String genre)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Artist").child(id);
        Artist artist=new Artist(id,name,genre);
        databaseReference.setValue(artist);
        Toast.makeText(this,"Artist updated",Toast.LENGTH_SHORT).show();
        return true;
    }
    public void addArtist()
    {
        String name=editTextName.getText().toString().trim();
        String genre=spinnerGenres.getSelectedItem().toString();
        if(!TextUtils.isEmpty(name))
        {
            String id=databaseArtist.push().getKey();
            Artist artist=new Artist(id,name,genre);
            databaseArtist.child(id).setValue(artist);
            Toast.makeText(this,"Artist added",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"You should enter name",Toast.LENGTH_SHORT).show();
        }
    }
}
