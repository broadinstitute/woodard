package woodard.model

import better.files.File
import org.http4s.Uri

final case class WorkflowSubmitRequest(version: String, body: WorkflowSubmitRequest.Body) {

}

object WorkflowSubmitRequest {

  def apply(body: Body): WorkflowSubmitRequest = WorkflowSubmitRequest("v1", body)

  def apply(workflowSource: File, workflowInputs: Option[File], workflowOptions: Option[File],
            collectionName: Option[String]): WorkflowSubmitRequest =
    WorkflowSubmitRequest("v1", BodyForFile(workflowSource, workflowInputs, workflowOptions, collectionName))

  def apply(workflowUrl: Uri, workflowInputs: Option[File], workflowOptions: Option[File],
            collectionName: Option[String]): WorkflowSubmitRequest =
    WorkflowSubmitRequest("v1", BodyForUrl(workflowUrl, workflowInputs, workflowOptions, collectionName))

  sealed trait Body {
    def workflowSourceOrUrl: Either[Uri, File]

    def workflowSourceOpt: Option[File]

    def workflowUrlOpt: Option[Uri]

    def workflowInputs: Option[File]

    def workflowOptions: Option[File]

    def collectionName: Option[String]
  }

  final case class BodyForFile(workflowSource: File, override val workflowInputs: Option[File],
                         override val workflowOptions: Option[File],
                         override val collectionName: Option[String]) extends Body {
    override def workflowSourceOrUrl: Either[Uri, File] = Right(workflowSource)

    override def workflowSourceOpt: Option[File] = Some(workflowSource)

    override def workflowUrlOpt: Option[Uri] = None
  }

  final case class BodyForUrl(workflowUrl: Uri, override val workflowInputs: Option[File],
                        override val workflowOptions: Option[File],
                        override val collectionName: Option[String]) extends Body {
    override def workflowSourceOrUrl: Either[Uri, File] = Left(workflowUrl)

    override def workflowSourceOpt: Option[File] = None

    override def workflowUrlOpt: Option[Uri] = Some(workflowUrl)
  }


}
