package com.tiggerpalace.automan;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.PlaylistsColumns.NAME;
import static android.provider.MediaStore.Audio.PlaylistsColumns.DATA;

public class PlaylistActivity extends ListActivity {
  private static final Uri PLAYLIST_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

  private static final String[] FROM = { _ID, NAME, DATA };
  private static final int[] TO = { R.id.rowid, R.id.name };
  private static String ORDER_BY = MediaStore.Audio.PlaylistsColumns.NAME + " ASC";

  private int dataColumnIndex;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.playlists);

    // Get a cursor with all people
    Cursor c = getContentResolver().query(PLAYLIST_URI, FROM, null, null, ORDER_BY);
    startManagingCursor(c);
    this.dataColumnIndex = c.getColumnIndex(DATA);

    ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, c, FROM, TO);
    setListAdapter(adapter);
  }

  @Override
  public void onListItemClick(ListView parent, View v, int position, long id) {
  Log.d("onListItemClick", id + "");  
    if (position >= 0) {
      Cursor c = (Cursor) parent.getItemAtPosition(position);
      String m3u = c.getString(this.dataColumnIndex);
      Intent intent = new Intent(this, MediaPlayerActivity.class); 
      intent.putExtra("playlist", m3u); 
      startActivity(intent);
    }
  }
}
