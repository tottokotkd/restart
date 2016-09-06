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

class Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.top.index("Your new application is ready."))
  }

}