package controllers.auth

import javax.inject.Inject

import com.tottokotkd.restart.core.domain.account.{AccountId, HasAccountManager, Twitter}
import modules.HasSQLiteDriver
import org.pac4j.core.config.Config
import org.pac4j.core.profile._
import org.pac4j.oauth.profile.twitter.TwitterProfile
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala._
import org.pac4j.play.store.PlaySessionStore
import play.api.libs.json.Json
import play.api.mvc._
import play.api.cache.CacheApi
import play.libs.concurrent.HttpExecutionContext

import scala.util.Try

class Auth @Inject() (val config: Config, val playSessionStore: PlaySessionStore, val ec: HttpExecutionContext, val cache: CacheApi)
  extends Controller
    with Security[CommonProfile]
    with HasAccountManager
    with HasSQLiteDriver {

  import driver.run

  def getTwitterProfile(implicit request: Request[AnyContent]): Try[TwitterProfile] = {
    val webContext = new PlayWebContext(request, playSessionStore)
    val profileManager = new ProfileManager[CommonProfile](webContext)
    Try(profileManager.get(true).get.asInstanceOf[TwitterProfile])
  }

  def twitterLogin = Secure("TwitterClient") { profiles => Action { implicit request =>
    val profile = getTwitterProfile.get
    val twitterId = profile.getId
    val username = profile.getDisplayName

    val accountId: AccountId =
      Try(run(accountManager.getAccount(provider = Twitter, identity = twitterId)))
        .getOrElse(run(accountManager.createAccount(provider = Twitter, identity = twitterId, name = username)))

    cache.set("account_id", accountId)

    Ok(views.html.top.index(s"account id: $accountId"))
  }}

  def getUserData = Action {
    Redirect("/")
  }



}