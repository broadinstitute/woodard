

val scalaV = "2.12.7"
val scalatestVersion = "3.0.5"
val http4sVersion = "0.18.21"
val GoogleAuthHttpVersion = "0.10.0"

lazy val commonSettings = Seq(
  version := "0.0.1",
  organization := "org.broadinstitute",
  scalaVersion := scalaV,
  resolvers ++= Seq(
    Resolver.mavenLocal
  ),
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Ypartial-unification"
  ),
  libraryDependencies ++= Seq(
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  )
)

lazy val coreSettings = Seq(
  name := "woodard-core"
)

lazy val cromiamSettings = Seq(
  name := "woodard-cromiam",
  libraryDependencies ++= Seq(
    "com.google.auth" % "google-auth-library-oauth2-http" % GoogleAuthHttpVersion
  )
)

publishArtifact in(Compile, packageDoc) := false

lazy val core = (project in file("core"))
  .settings(commonSettings)

lazy val cromiam = (project in file("cromiam"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(cromiamSettings)
  
