package controllers.auth

import javax.inject.Inject

import com.tottokotkd.restart.core.domain.account.{AccountId, AccountInfo, HasAccountManager, Twitter}
import controllers.util.AccountModule
import modules.HasPsqlDriver
import play.api.mvc._

import scala.util.Try

class Auth @Inject() (val secure: AccountModule)
  extends Controller
    with HasAccountManager
    with HasPsqlDriver {

  import driver.run

  def twitterLogin = secure.TwitterClientSecure { implicit request => profile =>

    val twitterId = profile.getId
    val username = profile.getDisplayName

    val accountInfo: AccountInfo =
      Try(run(accountManager.getAccount(provider = Twitter, identity = twitterId)))
        .getOrElse(run(accountManager.createAccount(provider = Twitter, identity = twitterId, name = username)))

    secure.cacheManager.setAccountCache(accountInfo)

    Redirect("/")
  }

  def disconnected = Action {
    Ok(views.html.auth.disconnected())
  }


}