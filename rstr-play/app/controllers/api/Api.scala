package controllers.api

import java.time.ZonedDateTime
import javax.inject.Inject

import com.tottokotkd.restart.core.domain.account._
import com.tottokotkd.restart.core.domain.resource.HasResourceManager
import controllers.util.{AccountModule, JsonControllerExpansion}
import modules.HasPsqlDriver

class Api @Inject() (val secure: AccountModule)
  extends HasAccountManager
    with HasResourceManager
    with HasPsqlDriver {

  import secure._

  def test = AuthAction { req => implicit accountInfo =>
    val resource = driver.run(resourceManager.defaultUpdate(target = ZonedDateTime.now))
    Ok(s"user account: ${accountInfo.id}, name: ${accountInfo.name}, timezone: ${accountInfo.zoneId.getId}: resouce ${resource}")
  }

}