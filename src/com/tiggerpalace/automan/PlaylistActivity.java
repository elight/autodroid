package com.tiggerpalace.automan;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.SimpleCursorAdapter;

public class PlaylistActivity extends ListActivity {
  private static final Uri PLAYLIST_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

  private static final String[] FROM = { MediaStore.Audio.PlaylistsColumns.NAME };
  private static final int[] TO = { R.id.name };
  private static String ORDER_BY = MediaStore.Audio.PlaylistsColumns.NAME + " DESC";

  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.playlists);

    Cursor playlistsCursor = managedQuery(PLAYLIST_URI, FROM, null, null, ORDER_BY);
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
          R.layout.item, playlistsCursor, FROM, TO);
    setListAdapter(adapter);

  }
}
