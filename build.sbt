version := "0.0.1"
name := "woodard"
organization := "Broad Institute"
scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

resolvers ++= Seq(
  Resolver.mavenLocal
)

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

publishArtifact in (Compile, packageDoc) := false

