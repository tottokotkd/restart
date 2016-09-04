package com.tottokotkd.restart.core.domain

import com.tottokotkd.restart.core.util.RstrRuntimeException

/**
  * Created by tottokotkd on 21/08/2016.
  */
package object account {

  type AccountId = Int

  sealed class AuthProviderType
  object Twitter extends AuthProviderType

  /***
    *  for test
    */
  object NotImplementedProvider extends AuthProviderType

  sealed class AccountManagerException(message: String = null, cause: Throwable = null) extends RstrRuntimeException(message, cause)
  val AuthProviderNotFoundException = new AccountManagerException(message = "auth provider is not found.")
  val AccountNotFoundException = new AccountManagerException(message = "account for auth identity is not found.")
  val AlreadyUsedAuthIdException = new AccountManagerException(message = "this auth identity is already used in the specified component.")
  val InvalidAuthIdException = new AccountManagerException(message = "this auth identity is invalid.")

}
