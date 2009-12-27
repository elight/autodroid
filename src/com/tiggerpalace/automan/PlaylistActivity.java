package com.tiggerpalace.automan;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.PlaylistsColumns.NAME;

public class PlaylistActivity extends ListActivity {
  private static final Uri PLAYLIST_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

  private static final String[] FROM = { _ID, NAME };
  private static final int[] TO = { R.id.rowid, R.id.name };
  private static String ORDER_BY = MediaStore.Audio.PlaylistsColumns.NAME + " ASC";

  private int idColumnIndex;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.playlists);

    // Get a cursor with all people
    Cursor c = getContentResolver().query(PLAYLIST_URI, FROM, null, null, ORDER_BY);
    startManagingCursor(c);
    this.idColumnIndex = c.getColumnIndex(_ID);
    
    ListAdapter adapter = new SimpleCursorAdapter(this,
          R.layout.item, c, FROM, TO);
    setListAdapter(adapter);
  }

  @Override
  public void onListItemClick(ListView parent, View v, int position, long id) {
	Log.d("AutoDroid:PlaylistActivity:onListItemClick", id + "");  
    PlaylistPlayer.getInstance().stop();
    if (position >= 0) {
        Cursor c = (Cursor) parent.getItemAtPosition(position);
        String playlistid = c.getString(this.idColumnIndex);
        Intent intent = new Intent(this, MediaPlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("PLAYLIST_ID", playlistid); 
        startActivity(intent);
    }
  }
}
