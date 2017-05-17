package services2

import scala.concurrent.Future

trait LotteryService {
  def fetchBody(): Future[String]
}
