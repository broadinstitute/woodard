package woodard.http.auth

import cats.effect.IO
import org.http4s.client.blaze.Http1Client
import org.http4s.headers.Authorization
import org.http4s.{AuthScheme, Credentials, Request}
import org.scalatest.FunSuite
import woodard.http.ServerApi

class CromiamSubmitTest extends FunSuite {

  def authorization(token: String): Authorization = Authorization(Credentials.Token(AuthScheme.Bearer, token))

  def authorize(request: Request[IO], token: String): Request[IO] = {
    request.putHeaders(authorization(token))
  }

  def getBearerToken: String = "open sesame"

  test("Try to submit to CromIam server") {
    val client = Http1Client[IO]().unsafeRunSync()
    val workflowId = "35a817e4-52ca-4e3c-85f0-b4111a9ca3ae"
    val requestIO = ServerApi.caasProd.getWorkflowApi(workflowId).getMetadata
    println(requestIO.map(_.uri.renderString).unsafeRunSync())
    val bearerToken = getBearerToken
    val stringIO = client.expect[String](requestIO.map(authorize(_, bearerToken)))
    val string = stringIO.unsafeRunSync()
    println("yo!")
    println(string)
    println("wassup?")
  }

}
