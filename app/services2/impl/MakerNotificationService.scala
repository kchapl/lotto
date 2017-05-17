package services2.impl

import model.Postcode
import play.api.libs.ws.{WSClient, WSResponse}
import services2.NotificationService

import scala.concurrent.Future

class MakerNotificationService(ws: WSClient, apiKey: String) extends NotificationService {

  override def notifyFailure(e: Throwable) = notify("postcode_failure", "???", e.getMessage)

  override def notifyUpdate(postcode: Postcode) = notify("postcode_update", "???", postcode.asText)

  override def notifyWin(postcode: Postcode) = notify("postcode_win", "???", postcode.asText)

  private def notify(event: String, values: String*): Future[WSResponse] =
    ws.url(s"https://maker.ifttt.com/trigger/$event/with/key/$apiKey")
      .post(
        values.zipWithIndex.map { case (v, i) => s"value$i" -> Seq(v) }.toMap
      )
}
