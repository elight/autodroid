package com.tiggerpalace.campfire

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget.TextView

import java.net._

import com.tiggerpalace.campfire.http.Http

class MyActivity extends Activity {
  
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
  }
}
