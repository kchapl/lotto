package services

import org.jsoup.Jsoup
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Lottery {

  val url = "http://freepostcodelottery.com/"

  def postcodeImageUrl(ws: WSClient, userId: String): Future[String] = {
    ws.url(url)
      .withHeaders("Cookie" -> s"userId=$userId")
      .get() map { response =>
      val body = response.body
      val doc = Jsoup.parse(body)
      val path = doc.select("img[alt='The current winning postcode']").first().attr("src")
      s"${url.stripSuffix("/")}$path"
    }
  }
}
