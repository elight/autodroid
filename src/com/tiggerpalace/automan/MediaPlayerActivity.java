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

  public ArrayList<String> getAudioFiles() {
    return audioFiles;
  }

  public void setAudioFiles(ArrayList<String> audioFiles) {
    setAudioFiles(audioFiles);
  }

  private int fileNameColumnIndex;

  private static Thread playerThread;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mediaplayer);

    if(getPlayerThread() == null) {    
      // Why the hell is this getting called more than once when the app is running?
      String playlistid = getIntent().getStringExtra("PLAYLIST_ID");
      this.audioFiles = loadMediaFrom(playlistid);
      playFilesInOrderFrom(getAudioFiles());
    }
  }

  private void playFilesInOrderFrom(ArrayList<String> fileList) {
    Log.d("AutoDroid:MediaPlayerActivity:playFilesInOrderFrom", fileList
        .toString());
    if (getPlayerThread() != null) {
      Log.d("AutoDroid:MediaPlayerActivity:playFilesInOrderFrom",
          "playerThread already running! interrupting!");
      getPlayerThread().interrupt();
      setPlayerThread(null);
    }
    setPlayerThread(new Thread(new PlaylistPlayer(fileList)));
    getPlayerThread().start();
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

    String audioFileName;
    do {
      audioFileName = c.getString(fileNameColumnIndex);
      Log.d("AutoDroid:MediaPlayerActivity:loadMediaFrom", audioFileName);
      this.audioFiles.add(audioFileName);
    } while (c.moveToNext());

    c.close();

    return audioFiles;
  }

  private static void setPlayerThread(Thread playerThread) {
    MediaPlayerActivity.playerThread = playerThread;
  }

  private static Thread getPlayerThread() {
    return playerThread;
  }
}
