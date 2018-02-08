name := """RoCaWeb User Interface"""
organization := "com.rocaweb"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"


resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.rocaweb" % "commons" % "1.0.0-SNAPSHOT"
libraryDependencies += "com.rocaweb" % "learning" % "1.0.0-SNAPSHOT"
libraryDependencies += "com.google.code.gson" % "gson" % "2.5"
