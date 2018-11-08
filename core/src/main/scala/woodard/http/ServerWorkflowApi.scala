package woodard.http

import cats.effect.IO
import org.http4s.MediaType.`application/json`
import org.http4s.Method.GET
import org.http4s.client.dsl.io._
import org.http4s.headers.Accept
import org.http4s.{Request, Uri}

case class ServerWorkflowApi(serverApi: ServerApi, id: String) {

  val version: String = serverApi.version
  val uri: Uri = serverApi.uri / "api" / "workflows" / version / id

  def getMetadata: IO[Request[IO]] = GET(
    uri / "metadata",
    Accept(`application/json`)
  )

}
