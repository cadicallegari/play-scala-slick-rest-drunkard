name := "play-scala-slick-rest-drunkard"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
  "com.byteslounge" %% "slick-repo" % "1.4.4",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.postgresql" % "postgresql" % "42.0.0",
  evolutions,
//  "com.h2database" % "h2" % "1.4.191",
  cache,
  ws,
  specs2 % Test)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
