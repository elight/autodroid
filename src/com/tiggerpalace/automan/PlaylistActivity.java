package com.tiggerpalace.automan;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.PlaylistsColumns.NAME;

public class PlaylistActivity extends ListActivity implements OnItemSelectedListener {
  private static final Uri PLAYLIST_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

  private static final String[] FROM = { _ID, NAME };
  private static final int[] TO = { R.id.rowid, R.id.name };
  private static String ORDER_BY = MediaStore.Audio.PlaylistsColumns.NAME + " ASC";

  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.playlists);
    getListView().setOnItemSelectedListener(this);

    Cursor playlistsCursor = managedQuery(PLAYLIST_URI, FROM, null, null, ORDER_BY);
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
          R.layout.item, playlistsCursor, FROM, TO);
    setListAdapter(adapter);
  }


  public void onItemSelected(AdapterView parent, View v, int position, long id) {
    if (position >= 0) {
        Cursor c = (Cursor) parent.getItemAtPosition(position);
        throw new RuntimeException("This is where you should be querying for the playlist and then playing it!");
    }
  }


  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
  }
}
