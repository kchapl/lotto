package services

import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Notification {

  def sent(ws: WSClient, key: String, event: String, postcode: String): Future[Int] = {
    Logger.info(s"Sending notification of event $event with postcode $postcode")
    ws.url(s"https://maker.ifttt.com/trigger/$event/with/key/$key")
      .post(
        Map(
          "value1" -> Seq(Lottery.url),
          "value2" -> Seq(postcode)
        )
      ) map { _.status }
  }
}
