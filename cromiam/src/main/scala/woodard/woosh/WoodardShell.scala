package woodard.woosh

import better.files.File
import woodard.http.auth.GoogleCredentialsBox
import woodard.http.{HttpRequests, ServerApi}

class WoodardShell(var serverApi: ServerApi) {

  var sourceFileOpt: Option[File] = None
  var inputFileOpt: Option[File] = None
  var optionsFileOpt: Option[File] = None
  var collectionNameOpt: Option[String] = None

  def httpRequests_=(httpRequests: HttpRequests): Unit = {
    serverApi = serverApi.withHttpRequests(httpRequests)
  }

  def loadAuthFile(authFile: File): Unit = {
    if (authFile.exists) {
      val credentials = GoogleCredentialsBox.forServiceAccount(authFile).withAllScopes
      serverApi = serverApi.map(credentials.addToRequest)
    } else {
      println(s"ERROR: File $authFile does not exist.")
    }
  }

  private def checkExists(file: File): Unit = {
    if (!file.exists) {
      println(s"WARNING: File $file does not exist.")
    }
  }

  def source_=(file: File): Unit = {
    checkExists(file)
    sourceFileOpt = Some(file)
  }

  def inputs_=(file: File): Unit = {
    checkExists(file)
    inputFileOpt = Some(file)
  }

  def options_=(file: File): Unit = {
    checkExists(file)
    optionsFileOpt = Some(file)
  }

  def collectionName_=(name: String): Unit = {
    collectionNameOpt = Some(name)
  }

  def submit(): Unit = {
    ??? // TODO
  }

}

object WoodardShell {

}


