package woodard.http

import better.files.File
import cats.effect.IO
import org.http4s.MediaType.`application/json`
import org.http4s.Method.{GET, POST}
import org.http4s.client.dsl.io._
import org.http4s.headers.Accept
import org.http4s.multipart.{Multipart, Part}
import org.http4s.{Charset, MediaType, Request, Uri}
import org.http4s.headers.{`Content-Encoding`, `Content-Type`}

case class ServerApi(uri: Uri, version: String = "v1") {

  def getVersion: IO[Request[IO]] = GET(
    uri / "engine" / version / "version",
    Accept(`application/json`)
  )

  def getWorkflowApi(id: String): ServerWorkflowApi = ServerWorkflowApi(this, id)

  def submit(workflowSource: File, workflowInputs: File, workflowOptions: File): IO[Request[IO]] = {
    val workflowSourcePart: Part[IO] =
      Part.fileData[IO]("workflowSource", workflowSource.toJava,
        `Content-Type`(MediaType.`text/plain`, Charset.`UTF-8`))
    val workflowInputsPart: Part[IO] =
      Part.fileData[IO]("workflowInputs", workflowInputs.toJava,
        `Content-Type`(MediaType.`application/json`, Charset.`UTF-8`))
    val workflowOptionsPart: Part[IO] =
      Part.fileData[IO]("workflowOptions", workflowOptions.toJava,
        `Content-Type`(MediaType.`application/json`, Charset.`UTF-8`))
    val parts = Vector(
      workflowSourcePart,
      workflowInputsPart,
      workflowOptionsPart
    )
    val multipart = Multipart(parts)
    POST(
      uri / "api" / "workflows" / version, multipart).map {
      _.withHeaders(multipart.headers)
        .putHeaders(`Accept`(MediaType.`application/json`))
    }
  }

}

object ServerApi {

  val caasProd: ServerApi = ServerApi(Uri.uri("https://cromwell.caas-prod.broadinstitute.org"))
  val local: ServerApi = ServerApi(Uri.uri("http://localhost:8000"))
}
