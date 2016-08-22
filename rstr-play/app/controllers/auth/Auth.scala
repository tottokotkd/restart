package controllers.auth

import javax.inject.Inject

import org.pac4j.core.config.Config
import org.pac4j.core.profile._
import org.pac4j.oauth.profile.twitter.TwitterProfile
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala._
import org.pac4j.play.store.PlaySessionStore
import play.api.libs.json.Json
import play.api.mvc._
import play.libs.concurrent.HttpExecutionContext

import scala.util.Try

class Auth @Inject() (val config: Config, val playSessionStore: PlaySessionStore, val ec: HttpExecutionContext) extends Controller with Security[CommonProfile] {

  def getTwitterProfile(request: Request[AnyContent]): Try[TwitterProfile] = {
    val webContext = new PlayWebContext(request, playSessionStore)
    val profileManager = new ProfileManager[CommonProfile](webContext)
    Try(profileManager.get(true).get.asInstanceOf[TwitterProfile])
  }

  def twitterLogin = Secure("TwitterClient") { profiles => Action { request =>
    Redirect("/")
  }}

  def getUserData = Action {
    Redirect("/")
  }



}