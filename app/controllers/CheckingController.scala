package controllers

import javax.inject.Inject

import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import play.twirl.api.Html

import scala.concurrent.ExecutionContext.Implicits.global
import org.jsoup.Jsoup

import scala.util.Properties

class CheckingController @Inject()(ws: WSClient) extends Controller {

  private val userId = Properties.envOrElse("LOTTERY_USER_ID","")

  def check = Action.async {
    ws.url("http://freepostcodelottery.com/")
      .withHeaders("Cookie" -> s"userId=$userId")
      .get() map { response =>
      println(response.status)
      val body = response.body
      val doc = Jsoup.parse(body)
      val winningPostcode = Option(doc.select("img[alt='The current winning postcode']").first()).map(_.attr("src"))
      winningPostcode match {
        case Some(postcode) =>
          Ok(Html(s"""<img src="http://freepostcodelottery.com$postcode" />"""))
        case None =>
          InternalServerError("User ID has expired")
      }
    }
  }
}
