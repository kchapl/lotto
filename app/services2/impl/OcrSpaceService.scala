package services2.impl

import model.Postcode
import play.api.libs.ws.WSClient
import services2.OcrService

import scala.concurrent.{ExecutionContext, Future}

class OcrSpaceService(ws: WSClient, apiKey: String) extends OcrService {

  // image/png
  override def read(imageUrl: String)(implicit executor: ExecutionContext) = {
    ws.url("https://api.ocr.space/parse/imageurl")
      .withQueryString(
        "apikey" -> apiKey,
        "url"    -> imageUrl
      )
      .get() flatMap { response =>
      println("*2")
      println(response.json)

      val failure = for {
        code        <- (response.json \ "OCRExitCode").asOpt[Int]
        description <- (response.json \ "ErrorMessage").asOpt[Seq[String]]
      } yield Future.failed(new RuntimeException(s"OCR error $code: ${description.mkString(", ")}"))

      failure getOrElse {
        Future.successful(
          Postcode(((response.json \ "ParsedResults").head \ "ParsedText").as[String].trim.toUpperCase, "")
        )
      }
    }
  }
}
