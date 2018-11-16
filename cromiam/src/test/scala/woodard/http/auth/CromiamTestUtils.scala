package woodard.http.auth

import cats.effect.IO
import org.http4s.client.Client
import org.http4s.client.blaze.Http1Client
import org.http4s.client.middleware.{RequestLogger, ResponseLogger}
import org.http4s.{EntityDecoder, Request}
import woodard.http.{ServerApi, HttpRequests}
import woodard.http.auth.GoogleCredentialsBox.Scope

/**
  * woodard
  * Created by oruebenacker on 11/11/18.
  */
object CromiamTestUtils {
  private[auth] def getCredentials: GoogleCredentialsBox = {
    val serviceAccountFileOpt = ServiceAccountFile.serviceAccountFileOpt
    val baseCredentials = GoogleCredentialsBox.getCredentials(serviceAccountFileOpt)
    baseCredentials.withScopes(Scope.all)
  }

  private[auth] val credentials = getCredentials

  private[auth] def authorized(request: Request[IO]): Request[IO] = credentials.addToRequestOpt(request).get

  private[auth] def getClient: Client[IO] = ResponseLogger.apply0(logHeaders = true, logBody = true)(
    RequestLogger.apply0(logHeaders = true, logBody = true)(
      Http1Client[IO]().unsafeRunSync()
    )
  )

  private[auth] def doHttpRequest[R](requestIO: IO[Request[IO]]
                                    )(implicit entityDecoder: EntityDecoder[IO, R]): R = {
    val client = getClient
    val authorizedRequestIO = requestIO.map(credentials.addToRequestOpt(_).get)
    val resultIO = client.expect[R](authorizedRequestIO)
    val result = resultIO.unsafeRunSync()
    client.shutdownNow()
    result
  }

  private[auth] def getServerApi(httpRequests: HttpRequests): ServerApi =
    new ServerApi(httpRequests, getClient, authorized)
}
