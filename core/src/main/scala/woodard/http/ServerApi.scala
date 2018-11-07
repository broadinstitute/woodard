package woodard.http

import cats.effect.IO
import org.http4s.MediaType.`application/json`
import org.http4s.Method.GET
import org.http4s.client.dsl.io._
import org.http4s.headers.Accept
import org.http4s.{Request, Uri}

case class ServerApi(uri: Uri, version: String = "v1") {

  def getVersion: IO[Request[IO]] = GET(
    uri / "engine" / version / "version",
    Accept(`application/json`)
  )


}

object ServerApi {

  val caasProd: ServerApi = ServerApi(Uri.uri("https://cromwell.caas-prod.broadinstitute.org"))
}
