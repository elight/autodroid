package com.example.myproject

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget.TextView

class MyActivity extends Activity {
  
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val tv = new TextView(this)
    tv.setText("Hello Android, I'm Scala!")
    setContentView(tv)
    //    setContentView(R.layout.main)
  }
}