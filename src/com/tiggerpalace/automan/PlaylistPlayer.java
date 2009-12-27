package com.tiggerpalace.automan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class PlaylistPlayer implements OnCompletionListener {
  private static PlaylistPlayer instance = null;

  private ArrayList<String> playlist;
  private Thread thread = null;
  private ListIterator<String> playlistIterator;
  private PlaylistRunnable runnable;
  
  public static PlaylistPlayer getInstance() {
    if(instance == null) {
      instance = new PlaylistPlayer();
    }
    return instance;
  }
  
  public void setPlaylist(ArrayList<String> list) {
    playlist = list;
    playlistIterator = list.listIterator();
  }
  
  public void start() {
    Log.d("AutoDroid:PlaylistPlayer.start", "-");    
    stop();
    runnable = new PlaylistRunnable(this);
    thread = new Thread(runnable);
    thread.start();
  }
  
  public void stop() {
    Log.d("AutoDroid:PlaylistPlayer.stop", "-");
    if(thread != null) {
      runnable.stop();
      thread.interrupt();
      thread = null;
    }
  }
  
  public void previousTrack() {
    Log.d("AutoDroid:PlaylistPlayer.previousTrack", "-");
    if(thread != null && playlistIterator.hasPrevious()) {
      boolean isPlaying = runnable.isPlaying();
      stop();
      playlistIterator.previous();
      if(playlistIterator.hasPrevious()) {
        playlistIterator.previous();
      }
      if(isPlaying) {
        thread = new Thread(runnable);
        thread.start();
      }
    }
  }
  
  public void nextTrack() {
    Log.d("AutoDroid:PlaylistPlayer.nextTrack", "-");    
    if(thread != null && playlistIterator.hasNext()) {
      boolean isPlaying = runnable.isPlaying();
      stop();
      if(isPlaying) {
        thread = new Thread(runnable);
        thread.start();
      } 
    }
  }
  
  public void togglePlayPause() {
    Log.d("AutoDroid:PlaylistPlayer.togglePlayPause", "-");
    if(runnable != null) {
      if(runnable.isPlaying()) {
        runnable.pause();
      } else {
        runnable.start();
      }
    }
  }
  
  public boolean isPlaying() {
    return runnable.isPlaying();
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
    
    public boolean isPlaying() {
      return player.isPlaying();
    }
    
    public void pause() {
      Log.d("AutoDroid:PlaylistPlayer:PlaylistRunnable.pause", "-");
      player.pause();
    }

    public void start() {
      Log.d("AutoDroid:PlaylistPlayer:PlaylistRunnable.start", "-");      
      player.start();
    }

    public void stop() {
      Log.d("AutoDroid:PlaylistPlayer:PlaylistRunnable.stop", "-");      
      player.stop();
      player.release();
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
  }
  
  private PlaylistPlayer() {
  }
}
