package woodard.http.auth

import org.scalatest.FunSuite
import woodard.http.HttpRequests
import woodard.model.VersionRequest

class CromiamEngineVersionTest extends FunSuite {

  test("get version") {
    val request = VersionRequest()
    val serverApi = CromiamTestUtils.getServerApi(HttpRequests.caasProd.map(CromiamTestUtils.authorized))
    val response = serverApi.engineVersion(request).unsafeRunSync()
    println(response)
  }

}
