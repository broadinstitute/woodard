package woodard.http

import cats.effect.IO
import org.http4s.MediaType.`application/json`
import org.http4s.Method.{GET, POST}
import org.http4s.client.dsl.io._
import org.http4s.headers.Accept
import org.http4s.multipart.{Multipart, Part}
import org.http4s.{Request, Uri}
import better.files.File

case class ServerApi(uri: Uri, version: String = "v1") {

  def getVersion: IO[Request[IO]] = GET(
    uri / "engine" / version / "version",
    Accept(`application/json`)
  )

  def getWorkflowApi(id: String): ServerWorkflowApi = ServerWorkflowApi(this, id)

  def submit(workflowSource: File, workflowInputs: File): IO[Request[IO]] = POST(
    uri / "api" / "workflows" / version,
    {
      val versionPart = Part.formData("version", version)
      val workflowSourcePart = Part.fileData("workflowSource", workflowSource.toJava)
      val workflowInputsPart = Part.fileData("workflowInputs", workflowInputs.toJava)
      val parts =  Vector(
        versionPart,
        workflowSourcePart,
        workflowInputsPart
      )
      Multipart(parts)
    }
  )

}

object ServerApi {

  val caasProd: ServerApi = ServerApi(Uri.uri("https://cromwell.caas-prod.broadinstitute.org"))
}
