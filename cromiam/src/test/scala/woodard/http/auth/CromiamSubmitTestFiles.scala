package woodard.http.auth

import better.files.File

object CromiamSubmitTestFiles {
  private[auth] val workflowSourceOpt: Option[File] = Some(File("/home/BROAD.MIT.EDU/oliverr/testcaas/testgzip.wdl"))
  private[auth] val workflowInputsOpt: Option[File] =
    Some(File("/home/BROAD.MIT.EDU/oliverr/testcaas/testgzip_inputs.json"))
  private[auth] val workflowOptionsOpt: Option[File] = Some(File("/home/BROAD.MIT.EDU/oliverr/testcaas/options.json"))
  private[auth] val collectionNameOpt: Option[String] = Some("dig-testcaas")
}
