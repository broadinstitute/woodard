package woodard.http

import cats.effect.IO
import org.http4s.client.Client
import woodard.http.JsonDecoding.{versionResponseDecoder, workflowSubmitResponseDecoder}
import woodard.model.{VersionRequest, VersionResponse, WorkflowSubmitRequest, WorkflowSubmitResponse}

class ServerApi(val httpRequests: HttpRequests, client: Client[IO]) {

  def getVersion(request: VersionRequest): IO[VersionResponse] = {
    val httpRequestIO = httpRequests.version(request)
    client.expect[VersionResponse](httpRequestIO)
  }

  def submit(request: WorkflowSubmitRequest): IO[WorkflowSubmitResponse] = {
    val httpRequestIO = httpRequests.workflowSubmit(request)
    client.expect[WorkflowSubmitResponse](httpRequestIO)
  }

}
