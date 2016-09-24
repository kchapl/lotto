package controllers

import javax.inject.Inject

import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import play.twirl.api.Html
import services.{Lottery, Notification, Ocr}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Properties

class CheckingController @Inject()(ws: WSClient) extends Controller {

  private val lotteryUserId = Properties.envOrElse("LOTTERY_USER_ID", "")
  private val ocrApiKey = Properties.envOrElse("OCR_API_KEY", "")
  private val makerKey = Properties.envOrElse("MAKER_KEY", "")

  def warmUp = Action { Ok }

  def check = Action.async {
    for {
      imageUrl <- Lottery.postcodeImageUrl(ws, lotteryUserId)
      postcode <- Ocr.read(ws, imageUrl, ocrApiKey)
    } yield {
      Notification.sent(ws, makerKey, postcode)
      Ok(Html(s"""<div>$postcode</div><div><img src="$imageUrl"></div>"""))
    }
  }
}
