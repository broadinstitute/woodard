package woodard.http

import cats.effect.IO
import org.http4s.client.blaze.Http1Client
import org.scalatest.FunSuite

class HttpTest extends FunSuite{

  test("blub") {
    val httpClient = Http1Client[IO]().unsafeRunSync
    val googleIO = httpClient.expect[String]("https://www.google.com")
    val google = googleIO.unsafeRunSync()
    println(google.substring(0, 100))
  }

}
