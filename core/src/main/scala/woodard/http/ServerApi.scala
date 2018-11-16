package woodard.http

import cats.effect.IO
import org.http4s.Request
import org.http4s.client.Client
import woodard.http.JsonDecoding.versionResponseDecoder
import woodard.model.{VersionRequest, VersionResponse}

class ServerApi(val httpRequests: HttpRequests, client: Client[IO],
                requestMapper: Request[IO] => Request[IO] = identity) {

  def getVersion(request: VersionRequest): IO[VersionResponse] = {
    val httpRequestIO = httpRequests.getVersion(request).map(requestMapper)
    client.expect[VersionResponse](httpRequestIO)
  }

}
