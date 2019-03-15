package woodard.http

import better.files.File
import cats.effect.IO
import org.http4s.MediaType.`application/json`
import org.http4s.Method.{GET, POST}
import org.http4s.client.dsl.io._
import org.http4s.headers.{Accept, `Content-Type`}
import org.http4s.multipart.{Multipart, Part}
import org.http4s.{Charset, MediaType, Request, Uri}
import woodard.model.{WorkflowMetadataRequest, EngineVersionRequest, WorkflowStatusRequest, WorkflowSubmitRequest}

case class HttpRequests(uri: Uri, requestMapper: Request[IO] => Request[IO] = identity) {

  def map(requestMapper: Request[IO] => Request[IO]): HttpRequests = copy(requestMapper = requestMapper)

  def engineVersion(versionRequest: EngineVersionRequest): IO[Request[IO]] = GET(
    uri / "engine" / versionRequest.version / "version",
    Accept(`application/json`)
  ).map(requestMapper)

  def workflowSubmit(request: WorkflowSubmitRequest): IO[Request[IO]] = {
    val urlToPart: Uri => Part[IO] = { url =>
      Part.formData[IO]("workflowUrl", url.renderString, `Content-Type`(MediaType.`text/plain`, Charset.`UTF-8`))
    }
    val body = request.body
    val wdlSourceToPart: File => Part[IO] = { file =>
      Part.fileData[IO]("workflowSource", file.toJava,
        `Content-Type`(MediaType.`text/plain`, Charset.`UTF-8`))
    }
    val workflowSourceOrUrlPartOpt: Option[Part[IO]] =
      Some(body.workflowSourceOrUrl.fold(urlToPart, wdlSourceToPart))
    val workflowInputsPartOpt: Option[Part[IO]] = body.workflowInputs.map { workflowInputs =>
      Part.fileData[IO]("workflowInputs", workflowInputs.toJava,
        `Content-Type`(MediaType.`application/json`, Charset.`UTF-8`))
    }
    val workflowOptionsPartOpt: Option[Part[IO]] = body.workflowOptions.map { workflowOptions =>
      Part.fileData[IO]("workflowOptions", workflowOptions.toJava,
        `Content-Type`(MediaType.`application/json`, Charset.`UTF-8`))
    }
    val collectionNamePartOpt: Option[Part[IO]] = request.body.collectionName.map { collectionName =>
      Part.formData[IO]("collectionName", collectionName, `Content-Type`(MediaType.`text/plain`, Charset.`UTF-8`))
    }
    val parts = Vector(
      workflowSourceOrUrlPartOpt,
      workflowInputsPartOpt,
      workflowOptionsPartOpt,
      collectionNamePartOpt
    ).flatten
    val multipart = Multipart(parts)
    POST(
      uri / "api" / "workflows" / request.version, multipart).map {
      _.withHeaders(multipart.headers)
        .putHeaders(`Accept`(MediaType.`application/json`))
    }.map(requestMapper)
  }

  def workflowMetadata(request: WorkflowMetadataRequest): IO[Request[IO]] = GET(
    uri / "api" / "workflows" / request.version / request.id / "metadata",
    Accept(`application/json`)
  ).map(requestMapper)

  def workflowStatus(request: WorkflowStatusRequest): IO[Request[IO]] = GET(
    uri / "api" / "workflows" / request.version / request.id / "status",
    Accept(`application/json`)
  ).map(requestMapper)

}

object HttpRequests {

  // https://cromwell.dsde-dev.broadinstitute.org

  val terraProd: HttpRequests = HttpRequests(Uri.uri("https://cromwell.dsde-prod.broadinstitute.org"))
  val terraAlpha: HttpRequests = HttpRequests(Uri.uri("https://cromwell.dsde-alpha.broadinstitute.org"))
  val terraDev: HttpRequests = HttpRequests(Uri.uri("https://cromwell.dsde-dev.broadinstitute.org"))
  val caasProd: HttpRequests = HttpRequests(Uri.uri("https://cromwell.caas-prod.broadinstitute.org"))
  val local: HttpRequests = HttpRequests(Uri.uri("http://localhost:8000"))
}
