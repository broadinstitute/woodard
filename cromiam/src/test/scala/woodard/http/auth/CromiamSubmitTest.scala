package woodard.http.auth

import cats.effect.IO
import org.http4s.client.blaze.Http1Client
import org.scalatest.FunSuite
import woodard.http.ServerApi
import woodard.http.auth.GoogleCredentialsBox.Scope

class CromiamSubmitTest extends FunSuite {

  private def getCredentials: GoogleCredentialsBox = {
    val serviceAccountFileOpt = ServiceAccountFile.serviceAccountFileOpt
    val baseCredentials = GoogleCredentialsBox.getCredentials(serviceAccountFileOpt)
    baseCredentials.withScopes(Scope.all)
  }

  test("Try to submit to CromIam server") {
    val client = Http1Client[IO]().unsafeRunSync()
    val workflowId = "9d9d51b5-a2d5-45e2-a2f5-40287fad1eb3"
    val requestIO = ServerApi.caasProd.getWorkflowApi(workflowId).getMetadata
    println(requestIO.map(_.uri.renderString).unsafeRunSync())
    val credentials = getCredentials
    val stringIO = client.expect[String](requestIO.map(credentials.addToRequestOpt(_).get))
    val string = stringIO.unsafeRunSync()
    println("yo!")
    println(string)
    println("wassup?")
  }

}
