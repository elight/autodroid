package com.tiggerpalace.automan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class DroidCarDock extends Activity implements OnClickListener {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    View musicButton = findViewById(R.id.music_button); 
    musicButton.setOnClickListener((OnClickListener) this);
  }

  public void onClick(View v) {
    Intent playlists = new Intent(this, PlaylistActivity.class);
    startActivity(playlists);
  }
}
