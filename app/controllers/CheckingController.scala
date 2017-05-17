package controllers

import javax.inject.Inject

import model.{Config, Lottery}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import play.twirl.api.Html
import services2.impl.{MakerNotificationService, OcrSpaceService, WsLotteryService}
import services2.{LotteryService, NotificationService, OcrService}
import util.StringUtil.normalisedEqual

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

class CheckingController @Inject()(ws: WSClient) extends Controller {

  private lazy val lottery: LotteryService = new WsLotteryService(
    ws,
    Config.lottery.url,
    Config.expectedPostcode,
    Config.lottery.userEmail
  )
  private lazy val ocr: OcrService               = new OcrSpaceService(ws, Config.ocrApiKey)
  private lazy val notifier: NotificationService = new MakerNotificationService(ws, Config.makerKey)

  def warmUp = Action { NoContent }

  def check = Action.async {

    val eventualPostcode = Lottery.currentPostcode(lottery, ocr)

    eventualPostcode onFailure {
      case NonFatal(e) => notifier.notifyFailure(e)
    }

    eventualPostcode onSuccess {
      case postcode =>
        notifier.notifyUpdate(postcode)
        if (normalisedEqual(postcode.asText, Config.expectedPostcode)) {
          notifier.notifyWin(postcode)
        }
    }

    eventualPostcode map { postcode =>
      Ok(Html(s"""<div>${postcode.asText}</div><div><img src="${postcode.imageUrl}"></div>"""))
    } recover {
      case NonFatal(e) =>
        InternalServerError(s"Failed to read postcode image: $e")
    }
  }
}
