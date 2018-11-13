

val scalaV = "2.12.7"
val scalatestVersion = "3.0.5"
val http4sVersion = "0.18.21"
val googleAuthHttpVersion = "0.10.0"
val betterFilesV = "3.6.0"
val logbackClassicV = "1.2.3"

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
    "com.github.pathikrit" %% "better-files" % betterFilesV,
    "ch.qos.logback" % "logback-classic" % logbackClassicV,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  )
)

lazy val coreOnlySettings = Seq(
  name := "woodard-core"
)

lazy val cromiamOnlySettings = Seq(
  name := "woodard-cromiam",
  libraryDependencies ++= Seq(
    "com.google.auth" % "google-auth-library-oauth2-http" % googleAuthHttpVersion
  )
)

publishArtifact in(Compile, packageDoc) := false

lazy val core = (project in file("core"))
  .settings(commonSettings)

lazy val cromiam = (project in file("cromiam"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(cromiamOnlySettings)
  
