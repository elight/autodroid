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

public class MediaPlayerActivity extends Activity {
  private Uri playlistURI;

  private static final String[] FROM = { _ID, DATA };

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

    // Why the hell is this getting called more than once when the app is running?
    String playlistid = getIntent().getStringExtra("PLAYLIST_ID");
    ArrayList<String> audioFiles = loadMediaFrom(playlistid);
    playFilesInOrderFrom(audioFiles);
  }

  public void onDestroy() {
    Log.d("AutoDroid:MediaPlayerActivity:onDestroy", "");    
    super.onDestroy();
  }
  
  public void onPause() {
    Log.d("AutoDroid:MediaPlayerActivity:onPause", "");    
    super.onPause();    
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
