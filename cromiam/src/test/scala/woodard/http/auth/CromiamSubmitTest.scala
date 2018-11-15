package woodard.http.auth

import better.files.File
import org.scalatest.FunSuite
import woodard.http.ServerHttpRequests
import woodard.model.WorkflowSubmitRequest

class CromiamSubmitTest extends FunSuite {

  test("Workflow submission to CromIam server") {
    val workflowSource: File =
      CromiamSubmitTestFiles.workflowSourceOpt.fold(
        cancel("No workflow file has been provided."))(identity)
    val cromwellServer = ServerHttpRequests.caasProd
    import CromiamSubmitTestFiles.{workflowInputsOpt, workflowOptionsOpt, collectionNameOpt}
    val request = WorkflowSubmitRequest(workflowSource, workflowInputsOpt, workflowOptionsOpt, collectionNameOpt)
    val httpRequestIO = cromwellServer.submit(request)
    println(httpRequestIO.map(_.toString).unsafeRunSync())
    val result = CromiamTestUtils.doHttpRequest[String](httpRequestIO)
    println(result)
  }

}
