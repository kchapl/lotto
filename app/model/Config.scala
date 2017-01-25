package model

import scala.util.Properties

object Config {

  private def prop(k: String): String =
    Properties.envOrNone(k).getOrElse(throw new RuntimeException(s"No property '$k'"))

  object lottery {
    val url: String = "http://freepostcodelottery.com/"
    val userId: String = prop("LOTTERY_USER_ID")
  }

  val ocrApiKey: String = prop("OCR_API_KEY")
  val makerKey: String = prop("MAKER_KEY")
  val expectedPostcode: String = prop("POSTCODE")
}
