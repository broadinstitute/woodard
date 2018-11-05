
lazy val commonSettings = Seq(
  version := "0.0.1",
  name := "woodard",
  organization := "Broad Institute",
  scalaVersion := "2.12.7",
  resolvers ++= Seq(
    Resolver.mavenLocal
  ),
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-feature"
  ),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )
)

publishArtifact in(Compile, packageDoc) := false

lazy val core = (project in file("core"))
  .settings(commonSettings)
