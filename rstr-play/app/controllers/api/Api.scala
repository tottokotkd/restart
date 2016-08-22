package controllers.api

import javax.inject.Inject

import com.tottokotkd.restart.core.domain.account._
import controllers.util.JsonController
import modules.HasSQLiteDriver
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

class Api  @Inject() (val config: Config, val playSessionStore: PlaySessionStore, val ec: HttpExecutionContext)
  extends JsonController with Security[CommonProfile] with HasAccountManager with HasSQLiteDriver {

  def getTwitterProfile(implicit request: Request[AnyContent]): Try[TwitterProfile] = {
    val webContext = new PlayWebContext(request, playSessionStore)
    val profileManager = new ProfileManager[CommonProfile](webContext)
    Try(profileManager.get(true).get.asInstanceOf[TwitterProfile])
  }

  case class CreateAccountParams(name: String)
  def createAccount = JsonAction(Json.reads[CreateAccountParams]) { json => implicit req =>
    val profile = getTwitterProfile

    if (profile.isFailure) Redirect("/")
    else {
      driver.run(accountManager.createAccount(provider = Twitter, identity = profile.get.getId, name = json.name))
      Redirect("/")
    }
  }

  def test = Action {request =>
    val profile = getTwitterProfile(request).get
    Ok(Json.toJson(Map[String, String]("id" -> profile.getId))).withHeaders("Access-Control-Allow-Origin" -> "*")

  }


}