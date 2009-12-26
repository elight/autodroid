package com.tiggerpalace.automan;

import java.io.DataInputStream;

import android.app.ListActivity;
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
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.PlaylistsColumns.NAME;
import static android.provider.MediaStore.Audio.PlaylistsColumns.DATA;

public class PlaylistActivity extends ListActivity implements OnItemSelectedListener {
  private static final Uri PLAYLIST_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

  private static final String[] FROM = { _ID, NAME };
  private static final int[] TO = { R.id.rowid, R.id.name };
  private static String ORDER_BY = MediaStore.Audio.PlaylistsColumns.NAME + " ASC";

  private int dataColumnIndex;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.playlists);
    getListView().setOnItemSelectedListener(this);

    // Get a cursor with all people
    Cursor c = getContentResolver().query(PLAYLIST_URI, FROM, null, null, ORDER_BY);
    startManagingCursor(c);
    dataColumnIndex= c.getColumnIndex(DATA);    
    
    ListAdapter adapter = new SimpleCursorAdapter(this,
          R.layout.item, c, FROM, TO);
    setListAdapter(adapter);
  }


  public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
	Log.d("callback", position + "");
    if (position >= 0) {
        Cursor c = (Cursor) parent.getItemAtPosition(position);
        MediaPlayer mp = new MediaPlayer();
        try {
	        mp.setDataSource(c.getString(dataColumnIndex));
	        mp.prepare();
        } catch(Exception e) {
        	Log.e("playing playlist", e.getMessage());
        }
        mp.start();
    }
  }


  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
  }
}
