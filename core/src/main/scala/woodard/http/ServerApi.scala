package woodard.http

import cats.effect.IO
import org.http4s.MediaType.`application/json`
import org.http4s.Method.{GET, POST}
import org.http4s.client.dsl.io._
import org.http4s.headers.Accept
import org.http4s.multipart.Multipart
import org.http4s.{Request, Uri}

case class ServerApi(uri: Uri, version: String = "v1") {

  def getVersion: IO[Request[IO]] = GET(
    uri / "engine" / version / "version",
    Accept(`application/json`)
  )

  def getWorkflowApi(id: String): ServerWorkflowApi = ServerWorkflowApi(this, id)

  def submit: IO[Request[IO]] = POST(
    uri / "api" / "workflows" / version,
    Multipart(Vector(

    ))
  )

}

object ServerApi {

  val caasProd: ServerApi = ServerApi(Uri.uri("https://cromwell.caas-prod.broadinstitute.org"))
}
