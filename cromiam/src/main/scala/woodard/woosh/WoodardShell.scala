package woodard.woosh

import java.util.Date

import better.files.File
import woodard.http.auth.GoogleCredentialsBox
import woodard.http.{HttpRequests, ServerApi}
import woodard.model.{WorkflowMetadataRequest, WorkflowMetadataResponse, WorkflowStatusRequest, WorkflowStatusResponse, WorkflowSubmitRequest, WorkflowSubmitResponse}

class WoodardShell(var serverApi: ServerApi) {

  var sourceFileOpt: Option[File] = None
  var inputsFileOpt: Option[File] = None
  var optionsFileOpt: Option[File] = None
  var collectionNameOpt: Option[String] = None

  private def timeString: String = new Date(System.currentTimeMillis()).toString

  private def log(message: String): Unit = println(timeString + " : " + message)

  private def error(message: String): Unit = log("ERROR: " + message)

  private def checkFileExists(file: File, isOptional: Boolean = false): Unit = {
    if (!file.exists) {
      val message = s"File $file does not exist."
      if (isOptional) {
        log(message)
      } else {
        error(message)
      }
    }
  }

  def httpRequests: HttpRequests = serverApi.serverApiIO.httpRequests

  def httpRequests_=(httpRequests: HttpRequests): Unit = {
    serverApi = serverApi.withHttpRequests(httpRequests)
  }

  def loadAuthFile(authFile: File): Unit = {
    if (authFile.exists) {
      val credentials = GoogleCredentialsBox.forServiceAccount(authFile).withAllScopes
      serverApi = serverApi.map(credentials.addToRequest)
    } else {
      error(s"File $authFile does not exist.")
    }
  }

  def source: File = sourceFileOpt.get

  def source_=(file: File): Unit = {
    checkFileExists(file)
    sourceFileOpt = Some(file)
  }

  def inputs: File = inputsFileOpt.get

  def inputs_=(file: File): Unit = {
    checkFileExists(file)
    inputsFileOpt = Some(file)
  }

  def options: File = optionsFileOpt.get

  def options_=(file: File): Unit = {
    checkFileExists(file)
    optionsFileOpt = Some(file)
  }

  def collectionName: String = collectionNameOpt.get

  def collectionName_=(name: String): Unit = {
    collectionNameOpt = Some(name)
  }

  var lastWorkflowIdOpt: Option[String] = None

  def submit(): Either[String, WorkflowSubmitResponse] = {
    sourceFileOpt.foreach(checkFileExists(_))
    inputsFileOpt.foreach(checkFileExists(_))
    optionsFileOpt.foreach(checkFileExists(_, isOptional = true))
    sourceFileOpt match {
      case Some(sourceFile) =>
        val request = WorkflowSubmitRequest(sourceFile, inputsFileOpt, optionsFileOpt, collectionNameOpt)
        log(s"Submitting $request")
        val response = serverApi.workflowSubmit(request)
        lastWorkflowIdOpt = Some(response.id)
        Right(response)
      case None =>
        Left("No workflow source file specified.")
    }
  }

  def getWorkflowId(idTemplate: String): String = {
    if (idTemplate.isEmpty && lastWorkflowIdOpt.nonEmpty) {
      lastWorkflowIdOpt.get
    } else {
      idTemplate
    }
  }

  def status(idTemplate: String = ""): Either[String, WorkflowStatusResponse] = {
    val id = getWorkflowId(idTemplate)
    val request = WorkflowStatusRequest(id)
    val response = serverApi.workflowStatus(request)
    Right(response)
  }

  def metadata(idTemplate: String = ""): Either[String, WorkflowMetadataResponse] = {
    val id = getWorkflowId(idTemplate)
    val request = WorkflowMetadataRequest(id)
    val response = serverApi.workflowMetadata(request)
    Right(response)
  }

}

object WoodardShell {

  val caasProd: WoodardShell = new WoodardShell(ServerApi(HttpRequests.caasProd))

}


