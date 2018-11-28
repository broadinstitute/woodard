package woodard.http

import cats.effect.IO
import org.http4s.client.Client
import org.http4s.client.blaze.Http1Client
import woodard.model.{EngineVersionRequest, VersionResponse, WorkflowMetadataRequest, WorkflowMetadataResponse, WorkflowStatusRequest, WorkflowStatusResponse, WorkflowSubmitRequest, WorkflowSubmitResponse}

class ServerApi(val serverApiIO: ServerApiIO) {

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
