package com.tiggerpalace.campfire

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget.TextView

import java.net._

import com.tiggerpalace.campfire.http.Http

class MyActivity extends Activity {
  
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    
    val connection = Http.post(
      "https://wearetitans.campfirenow.com/login", 
      Map(
        "emailAddress" -> "evan@tiggerpalace.com",
        "password" -> "s1dewind3r",
      )
    )

    val tv = new TextView(this)
    tv.setText("Camp Stove received a " + connection.getResponseCode + " from Campfire")
    tv.setText("Hello, world from Scala")
    setContentView(tv)
  }
}
