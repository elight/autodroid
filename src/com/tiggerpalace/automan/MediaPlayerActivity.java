package com.tiggerpalace.automan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MediaPlayerActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mediaplayer);

    String m3u = getIntent().getStringExtra("playlist");

    // TODO: Now play the playlist!
    Log.d("Play playlist", m3u);

    TextView textView = (TextView) findViewById(R.id.playlist); 
    textView.setText(m3u);
  }
}

