package controllers.game

import javax.inject.Inject

import controllers.util.AccountModule

/**
  * Created by tottokotkd on 27/08/2016.
  */
class GameApp @Inject() (val secure: AccountModule) {

  import secure._

  def play = AuthAction { req => accountInfo =>

    Ok(s"user account: ${accountInfo.id}, name: ${accountInfo.name}")
  }

}
