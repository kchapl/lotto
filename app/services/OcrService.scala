package services

import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object OcrService {

  def read(ws: WSClient, urlToRead: String, apiKey: String): String = {
    val post: Future[WSResponse] = ws.url("https://api.ocr.space/parse/image")
      .post(
        Map(
          "apikey" -> Seq(apiKey),
          "url" -> Seq(urlToRead)
        )
      )
    post map { response =>
      println("*1")
      println(response.status)
      println(response.body)
      println("*2")
    }
    post recover {
      case e: Exception =>
        println("*3")
        println(e)
    }

    "todo"
  }
}
