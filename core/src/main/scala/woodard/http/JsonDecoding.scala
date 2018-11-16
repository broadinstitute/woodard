package woodard.http

import cats.effect.IO
import woodard.model.VersionResponse
import io.circe.generic.auto._

import org.http4s._
import org.http4s.circe.jsonOf

object JsonDecoding {

  implicit val versionResponseDecoder: EntityDecoder[IO, VersionResponse] = jsonOf[IO, VersionResponse]

}
