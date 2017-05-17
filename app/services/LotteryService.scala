package services

import model.Config.lottery
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object LotteryService {

  def pageBody(ws: WSClient, userId: String): Future[String] = {
    ws.url(lottery.url)
      .withHeaders("Cookie" -> s"userId=$userId")
      .get() map { _.body }
  }
}
