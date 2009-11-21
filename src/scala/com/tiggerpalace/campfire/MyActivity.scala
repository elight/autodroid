package com.tiggerpalace.campfire

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget.TextView

import java.net._

class MyActivity extends Activity {
  
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    
    /*var url = new URL("https://wearetitans.campfirenow.com/login")

    HttpURLConnection.setFollowRedirects(false)

    var connection = url.openConnection.asInstanceOf[HttpURLConnection]
    connection.setRequestMethod("POST")
    connection.setRequestProperty("email_address", "evan@tiggerpalace.com")
    connection.setRequestProperty("password", "s1dewind3r")
    connection.connect */

    val tv = new TextView(this)
    //tv.setText("Camp Stove received a " + connection.getResponseCode + " from Campfire")
    tv.setText("Hello, world from Scala")
    setContentView(tv)
  }
}
