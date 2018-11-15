package woodard.model

final case class MetadataRequest(version: String, id: String) {

}

object MetadataRequest {
  def apply(id: String): MetadataRequest = MetadataRequest("v1", id)
}
