package woodard.cromiam

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

  test("Try to submit to CromIam server") {
    val client = Http1Client[IO]().unsafeRunSync()
    val requestIO = ServerApi.caasProd.getVersion
    println(requestIO.map(_.uri.renderString).unsafeRunSync())
    val token = "open sesame"
    val stringIO = client.expect[String](requestIO.map(authorize(_, token)))
    val string = stringIO.unsafeRunSync()
    println("yo!")
    println(string)
    println("wassup?")
  }

}
