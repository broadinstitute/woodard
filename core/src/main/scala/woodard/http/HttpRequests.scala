package woodard.http

import better.files.File
import cats.effect.IO
import org.http4s.MediaType.`application/json`
import org.http4s.Method.{GET, POST}
import org.http4s.client.dsl.io._
import org.http4s.headers.{Accept, `Content-Type`}
import org.http4s.multipart.{Multipart, Part}
import org.http4s.{Charset, MediaType, Request, Uri}
import woodard.model.{MetadataRequest, VersionRequest, WorkflowSubmitRequest}

case class HttpRequests(uri: Uri) {

  def getVersion(versionRequest: VersionRequest): IO[Request[IO]] = GET(
    uri / "engine" / versionRequest.version / "version",
    Accept(`application/json`)
  )

  def getMetadata(request: MetadataRequest): IO[Request[IO]] = GET(
    uri / "api" / "workflows" / request.version / request.id / "metadata",
    Accept(`application/json`)
  )

  def submit(request: WorkflowSubmitRequest): IO[Request[IO]] = {
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
    }
  }

}

object HttpRequests {

  val caasProd: HttpRequests = HttpRequests(Uri.uri("https://cromwell.caas-prod.broadinstitute.org"))
  val local: HttpRequests = HttpRequests(Uri.uri("http://localhost:8000"))
}
