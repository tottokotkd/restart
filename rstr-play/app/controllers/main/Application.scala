package controllers.main

import javax.inject.Inject

import org.pac4j.core.config.Config
import org.pac4j.core.profile._
import org.pac4j.oauth.profile.twitter.TwitterProfile
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala._
import org.pac4j.play.store.PlaySessionStore
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import play.libs.concurrent.HttpExecutionContext

import scala.util.Try

class Application @Inject() (val config: Config, val playSessionStore: PlaySessionStore, val ec: HttpExecutionContext) extends Controller with Security[CommonProfile] {

  def getTwitterProfile(implicit request: Request[AnyContent]): Try[TwitterProfile] = {
    val webContext = new PlayWebContext(request, playSessionStore)
    val profileManager = new ProfileManager[CommonProfile](webContext)

    Try(Logger.warn(profileManager.get(true).get.getClientName))

    Logger("access").warn("hey!")
    Try{
      Logger("access").warn(s"client is ${profileManager.get(true).get.getClientName}")
    }
    Try(profileManager.get(true).get.asInstanceOf[TwitterProfile])
  }


  def index = Action { implicit request =>
    Ok(views.html.top.index("Your new application is ready."))
  }

  def login = Secure("TwitterClient") { profiles => Action { request =>
    Redirect("/").withHeaders("Access-Control-Allow-Origin" -> "*")
  }}


  def testpage = Secure("TwitterClient") { profiles => Action{ request =>
    Ok(views.html.top.test())
  }}

  def test = Action {request =>
    val profile = getTwitterProfile(request).get
    Ok(Json.toJson(Map[String, String]("id" -> profile.getId)))

  }


}