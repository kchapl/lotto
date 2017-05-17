package services2

import model.Postcode

import scala.concurrent.{ExecutionContext, Future}

trait OcrService {
  def read(imageUrl: String)(implicit executor: ExecutionContext): Future[Postcode]
}
