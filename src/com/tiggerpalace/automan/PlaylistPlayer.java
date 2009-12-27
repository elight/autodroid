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
  }

  public void run() {
    MediaPlayer player = new MediaPlayer();
    player.setLooping(false);
    player.setOnCompletionListener(this);
    playlistIterator = playlist.iterator();
    if(playlistIterator.hasNext()) {
      try {
        player.setDataSource(playlistIterator.next());
        player.prepare();
        player.start();
      } catch(Exception e) {
        Log.e("playFirstFileIn", e.getMessage());
      }
    }
  }

  public void onCompletion(MediaPlayer mp) {
    this.run();
  }
}
