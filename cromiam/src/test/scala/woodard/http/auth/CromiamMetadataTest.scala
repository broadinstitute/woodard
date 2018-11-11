package woodard.http.auth

import org.scalatest.FunSuite
import woodard.http.ServerApi

/**
  * woodard
  * Created by oruebenacker on 11/11/18.
  */
class CromiamMetadataTest extends FunSuite {

  test("Metadata request to CromIam server") {
    val workflowId = "9d9d51b5-a2d5-45e2-a2f5-40287fad1eb3"
    val requestIO = ServerApi.caasProd.getWorkflowApi(workflowId).getMetadata
    val string = CromiamTestUtils.doRequest[String](requestIO)
    println(requestIO.map(_.uri.renderString).unsafeRunSync())
    println("yo!")
    println(string)
    println("wassup?")
  }
}
