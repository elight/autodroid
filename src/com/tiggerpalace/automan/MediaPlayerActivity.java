package com.tiggerpalace.automan;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.concurrent.ExecutorService;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MediaPlayerActivity extends Activity {
    private Uri playlistURI;

    private static final String[] FROM = { _ID, DATA };
	
	private ArrayList<String> audioFiles = new ArrayList<String>();

	private int fileNameColumnIndex;
	
	private static Thread playerThread;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mediaplayer);

    String playlistid = getIntent().getStringExtra("PLAYLIST_ID");
    
    audioFiles = loadMediaFrom(playlistid);
    playFilesInOrderFrom(audioFiles);
  }
  
  private void playFilesInOrderFrom(ArrayList<String> fileList) {
	Log.d("AutoDroid:MediaPlayerActivity:playFilesInOrderFrom", fileList.toString());
	if(playerThread != null) {
      Log.d("AutoDroid:MediaPlayerActivity:playFilesInOrderFrom", "playerThread already running! interrupting!");	  
	  playerThread.interrupt();
	  playerThread = null;
	}
	playerThread = new Thread(new PlaylistPlayer(fileList));
	playerThread.start();
  }

  private ArrayList<String> loadMediaFrom(String playlistid) {
	playlistURI = MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(playlistid));
	Cursor c = managedQuery(playlistURI, FROM, null, null, null);
	if(!c.moveToFirst()) {
	  Log.w("AutoDroid:MediaPlayerActivity:loadMediaFrom", "Got an empty cursor back WTF?");
	}
	fileNameColumnIndex = c.getColumnIndex(DATA);

	String audioFileName;
	do {
	  audioFileName = c.getString(fileNameColumnIndex);
	  Log.d("AutoDroid:MediaPlayerActivity:loadMediaFrom", audioFileName);
	  this.audioFiles.add(audioFileName);
	} while(c.moveToNext());
	
	c.close();
	
	return audioFiles;
  }
}

