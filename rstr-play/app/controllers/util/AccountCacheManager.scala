package controllers.util

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import com.tottokotkd.restart.core.domain.account.{AccountId, AccountInfo, HasAccountManager, Twitter}
import org.pac4j.core.config.Config
import org.pac4j.core.profile.{CommonProfile, ProfileManager}
import org.pac4j.oauth.profile.twitter.TwitterProfile
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala.Security
import org.pac4j.play.store.PlaySessionStore
import play.api.cache.CacheApi
import play.api.mvc._
import play.libs.concurrent.HttpExecutionContext

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

/**
  * Created by tottokotkd on 27/08/2016.
  */
class CacheManager @Inject() (val cache: CacheApi) {

  val accountCacheKey = "SessionManager.accountId"
  val expiration = Duration(1, TimeUnit.HOURS)

  def setAccountCache(accountId: AccountInfo) = cache.set(key = accountCacheKey, value = accountId, expiration = expiration)
  def getAccount = cache.get[AccountInfo](accountCacheKey)

}

class AccountModule @Inject()(val config: Config, val playSessionStore: PlaySessionStore, val ec: HttpExecutionContext, val cacheManager: CacheManager)
  extends Controller with Security[CommonProfile] {

  def getTwitterProfile(implicit request: Request[AnyContent]): Try[TwitterProfile] = {
    val webContext = new PlayWebContext(request, playSessionStore)
    val profileManager = new ProfileManager[CommonProfile](webContext)
    Try(profileManager.get(true).get.asInstanceOf[TwitterProfile])
  }

  def TwitterClientSecure(action: Request[AnyContent] => TwitterProfile => Result) = Secure("TwitterClient") { profiles => Action { implicit request =>
    getTwitterProfile match {
      case Success(profile) => action(request)(profile)
      case Failure(_) => InternalServerError
    }
  }}

  def AuthAction(action: Request[AnyContent] => AccountInfo => Result) = Action { implicit request =>
    cacheManager.getAccount match {
      case Some(accountId) => action(request)(accountId)
      case None => Ok("disconnected")
    }
  }

}
