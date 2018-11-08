package woodard.http.auth

import java.time.Instant
import java.util.Date

import better.files.File
import com.google.auth.oauth2.{AccessToken, GoogleCredentials, ServiceAccountCredentials}

class GoogleCredentialsBox(val googleCredentials: GoogleCredentials) {

  def accessTokenOpt: Option[AccessToken] = {
    Option(googleCredentials.getAccessToken).filter{ token =>
      token.getExpirationTime.toInstant.isBefore(Instant.now.minusSeconds(1))
    } orElse {
      googleCredentials.refresh()
      Option(googleCredentials.getAccessToken)
    }
  }

}

object GoogleCredentialsBox {

  def apply(googleCredentials: GoogleCredentials): GoogleCredentialsBox = new GoogleCredentialsBox(googleCredentials)

  def appDefault: GoogleCredentialsBox = apply(GoogleCredentials.getApplicationDefault)

  def forServiceAccount(file: File): GoogleCredentialsBox =
    apply(file.inputStream.apply(ServiceAccountCredentials.fromStream))

  def getCredentials(accountCredentialsFileOpt: Option[File]): GoogleCredentialsBox =
    accountCredentialsFileOpt.fold(appDefault)(forServiceAccount)

}
