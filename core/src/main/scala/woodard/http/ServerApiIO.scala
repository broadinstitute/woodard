package woodard.http

import cats.effect.IO
import org.http4s.Request
import org.http4s.client.Client
import org.http4s.client.blaze.Http1Client
import woodard.http.JsonDecoding.{versionResponseDecoder, workflowSubmitResponseDecoder}
import woodard.http.JsonDecoding.{workflowMetadataResponseDecoder, workflowStatusResponseDecoder}
import woodard.model._

class ServerApiIO(val httpRequests: HttpRequests, client: Client[IO]) {

  def withHttpRequests(httpRequests: HttpRequests): ServerApiIO = new ServerApiIO(httpRequests, client)

  def withClient(client: Client[IO]): ServerApiIO = new ServerApiIO(httpRequests, client)

  def map(requestMapper: Request[IO] => Request[IO]): ServerApiIO =
    withHttpRequests(httpRequests.map(requestMapper))

  def engineVersion(request: EngineVersionRequest): IO[VersionResponse] = {
    val httpRequestIO = httpRequests.engineVersion(request)
    client.expect[VersionResponse](httpRequestIO)
  }

  def workflowSubmit(request: WorkflowSubmitRequest): IO[WorkflowSubmitResponse] = {
    val httpRequestIO = httpRequests.workflowSubmit(request)
    client.expect[WorkflowSubmitResponse](httpRequestIO)
  }

  def workflowMetadata(request: WorkflowMetadataRequest): IO[WorkflowMetadataResponse] = {
    val httpRequestIO = httpRequests.workflowMetadata(request)
    client.expect[WorkflowMetadataResponse](httpRequestIO)
  }

  def workflowStatus(request: WorkflowStatusRequest): IO[WorkflowStatusResponse] = {
    val httpRequestIO = httpRequests.workflowStatus(request)
    client.expect[WorkflowStatusResponse](httpRequestIO)
  }

}

object ServerApiIO {
  def apply(httpRequests: HttpRequests, client: Client[IO] = Http1Client[IO]().unsafeRunSync()): ServerApiIO =
    new ServerApiIO(httpRequests, client)
}