package controllers

import javax.inject.Inject

import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import play.twirl.api.Html
import services.{LotteryService, OcrService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Properties

class CheckingController @Inject()(ws: WSClient) extends Controller {

  private val userId = Properties.envOrElse("LOTTERY_USER_ID", "")
  private val ocrApiKey = Properties.envOrElse("OCR_API_KEY", "")

  def check = Action.async {
    for {
      imageUrl <- LotteryService.postcodeImageUrl(ws, userId)
      postcode <- OcrService.read(ws, imageUrl, ocrApiKey)
    } yield {
      Ok(Html(s"""<div>$postcode</div><div><img src="$imageUrl"></div>"""))
    }
  }
}
