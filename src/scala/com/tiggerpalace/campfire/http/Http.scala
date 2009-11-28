package com.tiggerpalace.campfire.http

import java.net._

object Http {
  def post(urlStr: String, paramsMap: Map[String, String] ) = {
    HttpURLConnection.setFollowRedirects(false)

    val url = new URL(urlStr)
    var conn = url.openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("POST")
    conn.setRequestProperty("email_address", paramsMap("emailAddress"))
    conn.setRequestProperty("password", paramsMap("password"))
    conn.connect 
    conn
  }
}
