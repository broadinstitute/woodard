package woodard.http.auth

import org.scalatest.FunSuite
import woodard.http.HttpRequests
import woodard.model.VersionRequest

class CromiamVersionTest extends FunSuite {

  test("get version") {
    val request = VersionRequest()
    val serverApi = CromiamTestUtils.getServerApi(HttpRequests.caasProd)
    val response = serverApi.getVersion(request).unsafeRunSync()
    println(response)
  }

}
