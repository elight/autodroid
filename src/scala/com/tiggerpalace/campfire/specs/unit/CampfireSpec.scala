package com.tiggerpalace.campfire.specs.unit

import com.tiggerpalace.campfire.Campfire

import org.specs._
import org.mockito.Mockito._

object CampfireSpec extends Specification {
  val accountInfo = new LoginCredentials("evan@tiggerpalace.com", "s1dewind3r")
  val subdomain = "wearetitans"

  "should allow adding login credentials for a Campfire subdomain" in {
    Campfire.setAccountInfo(subdomain, accountInfo)
  }

  "Given a subdomain and matching user credentials" should {
    Campfire.setAccountInfo(subdomain, accountInfo)

    "be able to list the rooms for a given campfire account" in {
      Campfire.loginTo(subdomain)
      Campfire.rooms.size must_== 1
    }

    "allow login via HTTP" in {
      Campfire.protocol = "http"
      Campfire.loginTo(subdomain) must_== true
    }

    "allow login via HTTPS" in {
      Campfire.protocol = "https"
      Campfire.loginTo(subdomain) must_== true
    }
  }
}

