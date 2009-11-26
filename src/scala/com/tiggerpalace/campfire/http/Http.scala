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

    /*
    val client = new DefaultHttpClient
    val post = new HttpPost(protocol + "://" + url)
    val params = new BasicHttpParams
    post setParams(params)

    // Dangerous without null check
    val credentials = accounts(subdomain)
    params setParameter("email_address", credentials("emailAddress"))
    params setParameter("password", credentials("password"))

    val responseCode = client.execute(post).getStatusLine.getStatusCode
    responseCode >= 200 && responseCode < 400
    */
