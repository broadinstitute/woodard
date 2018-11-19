package woodard.model

final case class WorkflowMetadataRequest(version: String, id: String) {

}

object WorkflowMetadataRequest {
  def apply(id: String): WorkflowMetadataRequest = WorkflowMetadataRequest("v1", id)
}
