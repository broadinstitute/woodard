package woodard.http

import cats.effect.IO
import org.http4s.client.Client
import woodard.http.JsonDecoding.{versionResponseDecoder, workflowSubmitResponseDecoder}
import woodard.http.JsonDecoding.workflowStatusResponseDecoder
import woodard.model._

class ServerApi(val httpRequests: HttpRequests, client: Client[IO]) {

  def engineVersion(request: VersionRequest): IO[VersionResponse] = {
    val httpRequestIO = httpRequests.engineVersion(request)
    client.expect[VersionResponse](httpRequestIO)
  }

  def workflowSubmit(request: WorkflowSubmitRequest): IO[WorkflowSubmitResponse] = {
    val httpRequestIO = httpRequests.workflowSubmit(request)
    client.expect[WorkflowSubmitResponse](httpRequestIO)
  }

  def workflowStatus(request: WorkflowStatusRequest): IO[WorkflowStatusResponse] = {
    val httpRequestIO = httpRequests.workflowStatus(request)
    client.expect[WorkflowStatusResponse](httpRequestIO)
  }

}
