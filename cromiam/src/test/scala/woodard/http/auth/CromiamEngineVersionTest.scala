package woodard.http.auth

import org.scalatest.FunSuite
import woodard.http.HttpRequests
import woodard.model.EngineVersionRequest

class CromiamEngineVersionTest extends FunSuite {

  test("get version") {
    val request = EngineVersionRequest()
    val serverApi = CromiamTestUtils.getServerApi(HttpRequests.caasProd.map(CromiamTestUtils.authorized))
    val response = serverApi.engineVersion(request).unsafeRunSync()
    println(response)
  }

}
