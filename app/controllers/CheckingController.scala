package controllers

import javax.inject.Inject

import model.Config
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import play.twirl.api.Html
import services.{Lottery, Notification, Ocr}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

class CheckingController @Inject()(ws: WSClient) extends Controller {

  def warmUp = Action { NoContent }

  //noinspection TypeAnnotation
  def check = Action.async {
    val url = Lottery.postcodeImageUrl(ws, Config.lottery.userId)

    url.onFailure {
      case NonFatal(e) =>
        Notification.sent(ws, Config.makerKey, "postcode_failure", e.getMessage)
        InternalServerError(s"Failed to read postcode image: $e")
    }

    for {
      imageUrl <- url
      actualPostcode <- Ocr.read(ws, imageUrl, Config.ocrApiKey)
    } yield {
      Notification.sent(ws, Config.makerKey, "postcode_update", actualPostcode)
      if (eq(actualPostcode, Config.expectedPostcode)) {
        Notification.sent(ws, Config.makerKey, "postcode_win", actualPostcode)
      }
      Ok(Html(s"""<div>$actualPostcode</div><div><img src="$imageUrl"></div>"""))
    }
  }

  private def eq(s1: String, s2: String): Boolean = {
    def stem(s: String) = s.toLowerCase.trim.replaceAll("\\s+", "")
    stem(s1) == stem(s2)
  }
}
