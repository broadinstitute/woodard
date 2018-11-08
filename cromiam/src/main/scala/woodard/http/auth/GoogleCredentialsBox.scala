package woodard.http.auth

import java.time.Instant

import better.files.File
import cats.effect.IO
import com.google.auth.oauth2.{AccessToken, GoogleCredentials, ServiceAccountCredentials}
import org.http4s.{AuthScheme, Credentials, Request}
import org.http4s.headers.Authorization
import woodard.http.auth.GoogleCredentialsBox.Scope

import scala.collection.JavaConverters._

class GoogleCredentialsBox(googleCredentials: GoogleCredentials) {

  def withScope(scope: Scope): GoogleCredentialsBox = withScopes(Seq(scope))

  def withScopes(scope: Scope, scopes: Scope*): GoogleCredentialsBox = withScopes(scope +: scopes)

  def withScopes(scopes: Seq[Scope]): GoogleCredentialsBox =
    GoogleCredentialsBox(googleCredentials.createScoped(scopes.map(_.string).asJava))

  def accessTokenOpt: Option[AccessToken] = {
    Option(googleCredentials.getAccessToken).filter { token =>
      token.getExpirationTime.toInstant.isBefore(Instant.now.minusSeconds(1))
    } orElse {
      googleCredentials.refresh()
      Option(googleCredentials.getAccessToken)
    }
  }

  def addToRequestOpt(request: Request[IO]): Option[Request[IO]] = {
    accessTokenOpt.map { accessToken =>
      val authorization = Authorization(Credentials.Token(AuthScheme.Bearer, accessToken.getTokenValue))
      request.putHeaders(authorization)
    }
  }

}

object GoogleCredentialsBox {

  case class Scope(string: String)

  object Scope {
    val profile: Scope = Scope("profile")
    val email: Scope = Scope("email")
    val openid: Scope = Scope("openid")
    val all: Seq[Scope] = Seq(profile, email, openid)
  }

  def apply(googleCredentials: GoogleCredentials): GoogleCredentialsBox = new GoogleCredentialsBox(googleCredentials)

  def appDefault: GoogleCredentialsBox = apply(GoogleCredentials.getApplicationDefault)

  def forServiceAccount(file: File): GoogleCredentialsBox =
    apply(file.inputStream.apply(ServiceAccountCredentials.fromStream))

  def getCredentials(accountCredentialsFileOpt: Option[File]): GoogleCredentialsBox =
    accountCredentialsFileOpt.fold(appDefault)(forServiceAccount)

}
