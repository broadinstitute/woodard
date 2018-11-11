package woodard.http.auth

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
}
