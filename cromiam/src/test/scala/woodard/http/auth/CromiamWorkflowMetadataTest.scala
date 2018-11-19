package woodard.http.auth

import org.scalatest.FunSuite
import woodard.http.HttpRequests
import woodard.model.WorkflowMetadataRequest

/**
  * woodard
  * Created by oruebenacker on 11/17/18.
  */
class CromiamWorkflowMetadataTest extends FunSuite{

  test("metadata") {
    val apiVersion = "v1"
    val workflowId = "3e373635-4a3a-4982-8773-accd1a9bd6e4"
    val request = WorkflowMetadataRequest(apiVersion, workflowId)
    val serverApi = CromiamTestUtils.getServerApi(HttpRequests.caasProd.map(CromiamTestUtils.authorized))
    val response = serverApi.workflowMetadata(request).unsafeRunSync()
    println(response)
  }

}
