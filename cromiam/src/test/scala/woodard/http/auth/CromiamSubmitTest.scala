package woodard.http.auth

import cats.effect.IO
import org.http4s.client.blaze.Http1Client
import org.http4s.headers.Authorization
import org.http4s.{AuthScheme, Credentials, Request}
import org.scalatest.FunSuite
import woodard.http.ServerApi
import woodard.http.auth.GoogleCredentialsBox.Scope

class CromiamSubmitTest extends FunSuite {

  def authorization(token: String): Authorization = Authorization(Credentials.Token(AuthScheme.Bearer, token))

  def authorize(request: Request[IO], token: String): Request[IO] = {
    request.putHeaders(authorization(token))
  }

  def getAccessTokenOpt: Option[String] = {
    val serviceAccountFileOpt = ServiceAccountFile.serviceAccountFileOpt
    val baseCredentials = GoogleCredentialsBox.getCredentials(serviceAccountFileOpt)
    val credentials = baseCredentials.withScopes(Scope.email, Scope.openid, Scope.profile)
    credentials.accessTokenOpt.map(_.getTokenValue)
  }

  test("Try to submit to CromIam server") {
    val client = Http1Client[IO]().unsafeRunSync()
    val workflowId = "9d9d51b5-a2d5-45e2-a2f5-40287fad1eb3"
    val requestIO = ServerApi.caasProd.getWorkflowApi(workflowId).getMetadata
    println(requestIO.map(_.uri.renderString).unsafeRunSync())
    val accessTokenOpt = getAccessTokenOpt
    if(accessTokenOpt.isEmpty) cancel("Could not get access token.")
    val accessToken = accessTokenOpt.get
    println(accessToken.toString)
    val stringIO = client.expect[String](requestIO.map(authorize(_, accessToken)))
    val string = stringIO.unsafeRunSync()
    println("yo!")
    println(string)
    println("wassup?")
  }

}
