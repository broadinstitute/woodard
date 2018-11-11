package woodard.http.auth

import cats.effect.IO
import org.http4s.client.blaze.Http1Client
import org.scalatest.FunSuite
import woodard.http.ServerApi
import woodard.http.auth.GoogleCredentialsBox.Scope

class CromiamSubmitTest extends FunSuite {

  test("Workflow submission to CromIam server") {
    val client = Http1Client[IO]().unsafeRunSync()
    // TODO
    client.shutdownNow()
  }

}
