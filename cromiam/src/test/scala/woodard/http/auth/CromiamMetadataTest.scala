package woodard.http.auth

import org.scalatest.FunSuite
import woodard.http.HttpRequests
import woodard.model.MetadataRequest

/**
  * woodard
  * Created by oruebenacker on 11/11/18.
  */
class CromiamMetadataTest extends FunSuite {

  test("Metadata request to CromIam server") {
    val workflowId = "9d9d51b5-a2d5-45e2-a2f5-40287fad1eb3"
    val server = HttpRequests.caasProd
    val request = MetadataRequest(workflowId)
    val requestIO = server.getMetadata(request)
    val string = CromiamTestUtils.doHttpRequest[String](requestIO)
    println(requestIO.map(_.uri.renderString).unsafeRunSync())
    println("yo!")
    println(string)
    println("wassup?")
  }
}
