package services

import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Notification {

  def sent(ws: WSClient, key: String, postcode: String): Future[Int] = {
    ws.url(s"https://maker.ifttt.com/trigger/winning_postcode/with/key/$key")
      .post(Map("value1" -> Seq(postcode))) map {
      _.status
    }
  }
}
