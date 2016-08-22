package modules

import com.google.inject.AbstractModule
import org.pac4j.core.client.Clients
import org.pac4j.core.config.Config
import org.pac4j.oauth.client.TwitterClient
import org.pac4j.play.http.DefaultHttpActionAdapter
import org.pac4j.play.{ApplicationLogoutController, CallbackController}
import org.pac4j.play.store.{PlayCacheStore, PlaySessionStore}
import play.api.{Configuration, Environment}

/**
  * Created by tottokotkd on 17/08/2016.
  */

class SecurityModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {

    val baseUrl = configuration.getString("baseUrl").get
    val twitterKey = configuration.getString("twitterAPI.key").get
    val twitterSecret = configuration.getString("twitterAPI.secret").get

    val twitterClient = new TwitterClient(twitterKey, twitterSecret)


    val clients = new Clients(baseUrl + "/callback", twitterClient)
    val config = new Config(clients)
    config.setHttpActionAdapter(new DefaultHttpActionAdapter)
    bind(classOf[Config]).toInstance(config)

    bind(classOf[PlaySessionStore]).to(classOf[PlayCacheStore])

    // callback
    val callbackController = new CallbackController()
    callbackController.setMultiProfile(true)
    bind(classOf[CallbackController]).toInstance(callbackController)

    // logout
    val logoutController = new ApplicationLogoutController()
    logoutController.setDefaultUrl("/")
    bind(classOf[ApplicationLogoutController]).toInstance(logoutController)
  }
}