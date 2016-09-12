package controllers.api

import javax.inject.Inject

import com.tottokotkd.restart.core.domain.account._
import controllers.util.{AccountModule, JsonControllerExpansion}
import modules.HasPsqlDriver

class Api @Inject() (val secure: AccountModule)
  extends HasAccountManager
    with HasPsqlDriver {

  import secure._

  def test = AuthAction { req => accountInfo =>
    Ok(s"user account: ${accountInfo.id}, name: ${accountInfo.name}, timezone: ${accountInfo.zoneId.getId}")
  }

}