package com.tiggerpalace.automan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class DroidCarDock extends Activity implements OnClickListener {
  public static final String KEY = "DroidCarDock";
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    View musicButton = findViewById(R.id.music_button); 
    musicButton.setOnClickListener((OnClickListener) this);
    View playbackButton = findViewById(R.id.playback_button);    
    playbackButton.setOnClickListener((OnClickListener) this);
  }

  public void onResume() {
    super.onResume();
    View playbackButton = findViewById(R.id.playback_button);
    boolean playingOrPaused = PlaylistPlayer.getInstance().isPlayingOrPaused();
    playbackButton.setEnabled(playingOrPaused);
  }
  
  public void onClick(View v) {
    switch(v.getId()) {
    case R.id.music_button:
      Intent intent = new Intent(this, PlaylistActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      break;
    case R.id.playback_button:
      Intent intent2 = new Intent(this, MediaPlayerActivity.class);
      startActivity(intent2);
      break;
    }
  }
}
