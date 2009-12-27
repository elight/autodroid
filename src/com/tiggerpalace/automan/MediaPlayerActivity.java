package com.tiggerpalace.automan;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MediaPlayerActivity extends Activity implements OnClickListener {
  private Uri playlistURI;

  private static final String[] FROM = { _ID, DATA };
  private static final String PLAY = "PLAY";
  private static final String PAUSE = "PAUSE";

  private int fileNameColumnIndex;

  private static Thread playerThread;

  private static void setPlayerThread(Thread playerThread) {
    MediaPlayerActivity.playerThread = playerThread;
  }

  private static Thread getPlayerThread() {
    return playerThread;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d("AutoDroid:MediaPlayerActivity:onCreate", "");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mediaplayer);

    String playlistid = getIntent().getStringExtra("PLAYLIST_ID");
    // Handle case where we're creating a MediaPlayerActivity but the player is or was already playing 
    if( playlistid != null && !playlistid.equals("") ) {
      ArrayList<String> audioFiles = loadMediaFrom(playlistid);
      playFilesInOrderFrom(audioFiles);
    }

    Button playPauseButton = (Button) findViewById(R.id.play_pause_button); 
    playPauseButton.setOnClickListener(this);
    View rwButton = findViewById(R.id.rw_button); 
    rwButton.setOnClickListener(this);
    View ffButton = findViewById(R.id.ff_button); 
    ffButton.setOnClickListener(this); 
    
    if( ! PlaylistPlayer.getInstance().isPlaying() ) {
      playPauseButton.setText("PLAY");
    }
  }

  public void onDestroy() {
    Log.d("AutoDroid:MediaPlayerActivity:onDestroy", "");    
    super.onDestroy();
  }
  
  public void onPause() {
    Log.d("AutoDroid:MediaPlayerActivity:onPause", "");    
    super.onPause();    
  }

  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.play_pause_button:
        String label = PlaylistPlayer.getInstance().isPlaying() ? PLAY : PAUSE;        
        ((Button) v).setText(label);           
        PlaylistPlayer.getInstance().togglePlayPause();
        break;
      case R.id.rw_button:
        PlaylistPlayer.getInstance().previousTrack();
        break;
      case R.id.ff_button:
        PlaylistPlayer.getInstance().nextTrack();        
        break;
    }
  }

  private void playFilesInOrderFrom(ArrayList<String> fileList) {    
    Log.d("AutoDroid:MediaPlayerActivity:playFilesInOrderFrom", fileList.toString());
    PlaylistPlayer player = PlaylistPlayer.getInstance();
    player.setPlaylist(fileList);
    player.start();
  }

  private ArrayList<String> loadMediaFrom(String playlistid) {
    playlistURI = MediaStore.Audio.Playlists.Members.getContentUri("external",
        Long.parseLong(playlistid));
    Cursor c = managedQuery(playlistURI, FROM, null, null, null);
    if (!c.moveToFirst()) {
      Log.w("AutoDroid:MediaPlayerActivity:loadMediaFrom",
          "Got an empty cursor back WTF?");
    }
    fileNameColumnIndex = c.getColumnIndex(DATA);

    ArrayList<String> fileList = new ArrayList<String>();
    String audioFileName;
    do {
      audioFileName = c.getString(fileNameColumnIndex);
      fileList.add(audioFileName);
    } while (c.moveToNext());

    c.close();

    return fileList;
  }
}
