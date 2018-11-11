package woodard.http.auth

import cats.effect.IO
import org.http4s.client.blaze.Http1Client
import org.scalatest.FunSuite
import woodard.http.ServerApi

/**
  * woodard
  * Created by oruebenacker on 11/11/18.
  */
class CromiamMetadataTest extends FunSuite {
  test("Metadata request to CromIam server") {
    val client = Http1Client[IO]().unsafeRunSync()
    val credentials = CromiamTestUtils.getCredentials
    val workflowId = "9d9d51b5-a2d5-45e2-a2f5-40287fad1eb3"
    val requestIO = ServerApi.caasProd.getWorkflowApi(workflowId).getMetadata
    println(requestIO.map(_.uri.renderString).unsafeRunSync())
    val stringIO = client.expect[String](requestIO.map(credentials.addToRequestOpt(_).get))
    val string = stringIO.unsafeRunSync()
    client.shutdownNow()
    println("yo!")
    println(string)
    println("wassup?")
  }
}
