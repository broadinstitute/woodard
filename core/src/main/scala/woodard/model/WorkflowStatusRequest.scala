package woodard.model

/**
  * woodard
  * Created by oruebenacker on 11/17/18.
  */
case class WorkflowStatusRequest(version: String, id: String) {

}

object WorkflowStatusRequest {
  def apply(id: String): WorkflowStatusRequest = WorkflowStatusRequest(Common.apiDefaultVersion, id)
}
