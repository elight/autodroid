package com.tiggerpalace.automan;

import java.util.ArrayList;
import java.util.Iterator;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class PlaylistPlayer implements Runnable, OnCompletionListener {
  private ArrayList<String> playlist;
  private Iterator<String> playlistIterator;
  
  public PlaylistPlayer(ArrayList<String> playlist) {
    this.playlist = playlist;
    this.playlistIterator = playlist.iterator();
  }
  
  public void run() {
    MediaPlayer player = new MediaPlayer();
    player.setLooping(false);
    player.setOnCompletionListener(this);
    if(playlistIterator.hasNext()) {
      try {
        String filename = playlistIterator.next();
        player.setDataSource(filename);
        player.prepare();
        player.start();
        Log.d("AutoDroid:PlaylistPlayer:run", new StringBuffer().append("Now playing: '").append(filename).append("'").toString());
      } catch(Exception e) {
        Log.e("AutoDroid:PlaylistPlayer:run", e.getMessage());
      }
    }
  }

  public void onCompletion(MediaPlayer mp) {
    this.run();
  }
}
