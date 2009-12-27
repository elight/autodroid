package com.tiggerpalace.automan;

import java.util.ArrayList;
import java.util.Iterator;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class PlaylistPlayer implements OnCompletionListener {
  private static PlaylistPlayer instance = null;

  private ArrayList<String> playlist;
  private Thread thread = null;
  private Iterator<String> playlistIterator;
  private PlaylistRunnable runnable;
  
  public static PlaylistPlayer getInstance() {
    if(instance == null) {
      instance = new PlaylistPlayer();
    }
    return instance;
  }
  
  public void setPlaylist(ArrayList<String> list) {
    playlist = list;
    playlistIterator = list.iterator();
  }
  
  public void start() {
    stop();
    runnable = new PlaylistRunnable(this);
    thread = new Thread(runnable);
    thread.start();
  }
  
  public void stop() {
    if(thread != null) {
      runnable.stop();
      thread.interrupt();
      thread = null;
    }
  }

  public void onCompletion(MediaPlayer mp) {
    thread = new Thread(runnable);
    thread.start();
  }  
  
  private class PlaylistRunnable implements Runnable {
    private PlaylistPlayer playlistPlayer;
    private MediaPlayer player;

    PlaylistRunnable(PlaylistPlayer player) {
      this.playlistPlayer = player;
    }
    
    public void run() {
      player = new MediaPlayer();
      player.setLooping(false);
      player.setOnCompletionListener(playlistPlayer);
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
    
    public void stop() {
      player.stop();
    }
  }
  
  private PlaylistPlayer() {
  }
}
