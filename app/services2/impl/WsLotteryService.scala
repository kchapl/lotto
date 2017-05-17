package services2.impl

import play.api.libs.ws.WSClient
import services2.LotteryService

import scala.concurrent.ExecutionContext.Implicits.global

class WsLotteryService(ws: WSClient, lotteryUrl: String, postcode: String, email: String) extends LotteryService {

  override def fetchBody() =
    ws.url(lotteryUrl)
      .post(
        Map(
          "register-ticket" -> Seq(postcode),
          "register-email"  -> Seq(email),
          "login"           -> Seq("")
        )
      ) map (_.body)
}
