version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"
val akkaVersion = "2.4.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,

  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % "test"
)