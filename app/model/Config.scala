package model

import scala.util.Properties

object Config {

  private def prop(k: String): String =
    Properties.envOrNone(k) getOrElse {
      throw new RuntimeException(s"No property '$k'")
    }

  object lottery {
    val url: String       = "https://freepostcodelottery.com/"
    val userEmail: String = prop("LOTTERY_USER_EMAIL")
  }

  val ocrApiKey: String        = prop("OCR_API_KEY")
  val makerKey: String         = prop("MAKER_KEY")
  val expectedPostcode: String = prop("POSTCODE")
}
