package controllers.util

import play.api.libs.json.Reads
import play.api.mvc._

import scala.util._

/**
  * Created by tottokotkd on 21/08/2016.
  */
trait JsonControllerExpansion { this: Controller =>

  def JsonAction[T](r: Reads[T])(act: T => Request[AnyContent] => Result): Action[AnyContent] = Action { request =>
    request.body.asJson.flatMap(_.asOpt[T](r)) match {
      case None => BadRequest
      case Some(apiValue) => act(apiValue)(request)
    }
  }

}