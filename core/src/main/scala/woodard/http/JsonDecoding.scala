package woodard.http

import java.util.Date

import cats.effect.IO
import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import woodard.model.{VersionResponse, WorkflowMetadataResponse, WorkflowStatusResponse, WorkflowSubmitResponse}
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.jsonOf

import scala.util.Try

object JsonDecoding {

  val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")

  implicit val dateDecoder: Decoder[Date] = Decoder.decodeString.emap { string =>
    Try {
      dateFormat.parse(string)
    }.toEither.left.map(_.getMessage)
  }

  implicit val versionResponseDecoder: EntityDecoder[IO, VersionResponse] = jsonOf[IO, VersionResponse]
  implicit val workflowSubmitResponseDecoder: EntityDecoder[IO, WorkflowSubmitResponse] =
    jsonOf[IO, WorkflowSubmitResponse]
  implicit val workflowMetadataResponseDecoder: EntityDecoder[IO, WorkflowMetadataResponse] =
    jsonOf[IO, WorkflowMetadataResponse]
  implicit val workflowStatusResponseDecoder: EntityDecoder[IO, WorkflowStatusResponse] =
    jsonOf[IO, WorkflowStatusResponse]
}
