package com.tiggerpalace.campfire

import org.apache.http.impl.client._
import org.apache.http.client.methods._
import org.apache.http.params._

object Campfire {
  val accounts = scala.collection.mutable.Map[String, LoginCredentials]()
  var rooms = List[String]()
  var protocol = "https"
  val domain = "campfirenow.com"

  def loginTo(subdomain: String) = {
    val client = new DefaultHttpClient
    val post = new HttpPost(protocol + "://" + subdomain + "." + domain)
    val params = new BasicHttpParams
    post.setParams(params)

    // Dangerous without null check
    val credentials = accounts(subdomain)
    params.setParameter("email_address", credentials.emailAddress)
    params.setParameter("password", credentials.password)

    val responseCode = client.execute(post).getStatusLine.getStatusCode
    responseCode >= 200 && responseCode < 400
  }

  def setAccountInfo(subdomain: String, credentials: LoginCredentials) {
    accounts(subdomain) = credentials
  }

  private def listFires = {
    Array("wearetitans", "entryway")
  }
}
