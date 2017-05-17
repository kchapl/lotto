package model

import model.Config.lottery
import org.jsoup.Jsoup
import services2.{LotteryService, OcrService}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Lottery {

  def currentPostcode(lotteryService: LotteryService, ocrService: OcrService)(
    implicit executor: ExecutionContext): Future[Postcode] = {
    lotteryService.fetchBody() map postcodeImageUrl flatMap {
      case Failure(e) => Future.failed(e)
      case Success(u) => ocrService.read(u)
    }
  }

  private def postcodeImageUrl(pageBody: String): Try[String] = {

    val postcodeImagePath =
      Option(Jsoup.parse(pageBody).select("img[alt='The current winning postcode']").first()) map (r =>
        Success(r.attr("src"))) getOrElse Failure(
        new RuntimeException(s"Failed to find postcode image in:\n$pageBody")
      )

    def postcodeImageUrl(path: String): String = s"${lottery.url.stripSuffix("/")}$path"

    postcodeImagePath map postcodeImageUrl
  }
}
