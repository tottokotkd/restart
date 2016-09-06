package controllers.api

import javax.inject.Inject

import com.tottokotkd.restart.core.domain.account._
import controllers.util.{AccountModule, JsonControllerExpansion}
import modules.HasSQLiteDriver

class Api @Inject() (val secure: AccountModule)
  extends HasAccountManager
    with HasSQLiteDriver {

  import secure._

  def test = AuthAction { req => accountInfo =>
    Ok(s"user account: ${accountInfo.id}, name: ${accountInfo.name}")
  }

}