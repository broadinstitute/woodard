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
    val workflowOptions: File =
      CromiamSubmitTestFiles.workflowOptionsOpt.fold(
        cancel("No options file has been provided."))(identity)
//    val cromwellServer = ServerApi.caasProd
    val cromwellServer = ServerApi.caasProd
    val requestIO = cromwellServer.submit(workflowSource, workflowInputs, workflowOptions)
    println(requestIO.map(_.toString).unsafeRunSync())
    val result = CromiamTestUtils.doRequest[String](requestIO)
    println(result)
  }

}
