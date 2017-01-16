package controllers

import javax.inject.Inject

import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import play.twirl.api.Html
import services.{Lottery, Notification, Ocr}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Properties

class CheckingController @Inject()(ws: WSClient) extends Controller {

  private val lotteryUserId = Properties.envOrNone("LOTTERY_USER_ID")
  private val ocrApiKey = Properties.envOrNone("OCR_API_KEY")
  private val makerKey = Properties.envOrNone("MAKER_KEY")
  private val expectedPostcode = Properties.envOrNone("POSTCODE")

  def warmUp = Action { NoContent }

  def check = Action.async {
    (lotteryUserId, ocrApiKey, makerKey, expectedPostcode) match {
      case (Some(u), Some(o), Some(m), Some(refPostcode)) =>
        val url = Lottery.postcodeImageUrl(ws, u)
        for {
          imageUrl <- url
          actualPostcode <- Ocr.read(ws, imageUrl, o)
        } yield {
          Notification.sent(ws, m, "postcode_update", actualPostcode)
          if (eq(actualPostcode, refPostcode)) {
            Notification.sent(ws, m, "postcode_win", actualPostcode)
          }
          Ok(Html(s"""<div>$actualPostcode</div><div><img src="$imageUrl"></div>"""))
        }
        for {
          e <- url.failed
        } yield {
          Notification.sent(ws, m, "postcode_failure", e.getMessage)
          InternalServerError(s"Failed to read postcode image: $e")
        }
      case _ =>
        Future.successful(InternalServerError("Missing properties"))
    }
  }

  private def eq(s1: String, s2: String): Boolean = {
    def stem(s: String) = s.toLowerCase.trim.replaceAll("\\s+", "")
    stem(s1) == stem(s2)
  }
}
