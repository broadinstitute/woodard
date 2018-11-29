package woodard.http

import cats.effect.IO
import org.http4s.Request
import org.http4s.client.Client
import org.http4s.client.blaze.Http1Client
import woodard.model.{EngineVersionRequest, VersionResponse, WorkflowMetadataRequest, WorkflowMetadataResponse, WorkflowStatusRequest, WorkflowStatusResponse, WorkflowSubmitRequest, WorkflowSubmitResponse}

class ServerApi(val serverApiIO: ServerApiIO) {

  def withHttpRequests(httpRequests: HttpRequests): ServerApi =
    new ServerApi(serverApiIO.withHttpRequests(httpRequests))

  def withClient(client: Client[IO]): ServerApi = new ServerApi(serverApiIO.withClient(client))

  def map(requestMapper: Request[IO] => Request[IO]): ServerApi =
    new ServerApi(serverApiIO.map(requestMapper))

  private def deIO[T](itemIO: IO[T]): T = itemIO.unsafeRunSync()

  def engineVersion(request: EngineVersionRequest): VersionResponse =
    deIO(serverApiIO.engineVersion(request))

  def workflowSubmit(request: WorkflowSubmitRequest): WorkflowSubmitResponse =
    deIO(serverApiIO.workflowSubmit(request))

  def workflowMetadata(request: WorkflowMetadataRequest): WorkflowMetadataResponse =
    deIO(serverApiIO.workflowMetadata(request))

  def workflowStatus(request: WorkflowStatusRequest): WorkflowStatusResponse =
    deIO(serverApiIO.workflowStatus(request))

}

object ServerApi {
  def apply(httpRequests: HttpRequests, client: Client[IO] = Http1Client[IO]().unsafeRunSync()): ServerApi =
    new ServerApi(new ServerApiIO(httpRequests, client))
}
