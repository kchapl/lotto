package services

import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Ocr {

  def read(ws: WSClient, imageUrl: String, apiKey: String): Future[String] = {
    ws.url("https://api.ocr.space/parse/image")
      .post(
        Map(
          "apikey" -> Seq(apiKey),
          "url"    -> Seq(imageUrl)
        )
      ) map { response =>
      ((response.json \ "ParsedResults").head \ "ParsedText").as[String].trim.toUpperCase
    }
  }
}
