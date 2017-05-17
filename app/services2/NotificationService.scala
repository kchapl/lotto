package services2

import model.Postcode
import play.api.libs.ws.WSResponse

import scala.concurrent.Future

trait NotificationService {
  def notifyFailure(e: Throwable): Future[WSResponse]
  def notifyUpdate(postcode: Postcode): Future[WSResponse]
  def notifyWin(postcode: Postcode): Future[WSResponse]
}
