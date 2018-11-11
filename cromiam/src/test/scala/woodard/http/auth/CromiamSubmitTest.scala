package woodard.http.auth

import better.files.File
import org.scalatest.FunSuite
import woodard.http.ServerApi

class CromiamSubmitTest extends FunSuite {

  test("Workflow submission to CromIam server") {
    val workflowSource: File =
      CromiamSubmitTestFiles.workflowSourceOpt.fold(
        cancel("No workflow file has been provided."))(identity)
    val workflowInputs: File =
      CromiamSubmitTestFiles.workflowInputsOpt.fold(
        cancel("No inputs file has been provided."))(identity)
    val requestIO = ServerApi.caasProd.submit(workflowSource, workflowInputs)
    println(requestIO.map(_.toString).unsafeRunSync())
    val result = CromiamTestUtils.doRequest[String](requestIO)
    println(result)
  }

}
