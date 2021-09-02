name := "ConfigReader"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies += "com.typesafe" % "config" % "1.4.1"
libraryDependencies += "com.h2database" % "h2" % "1.4.196"
libraryDependencies += "com.datastax.oss" % "java-driver-core" % "4.13.0"
// https://mvnrepository.com/artifact/com.dimafeng/testcontainers-scala-scalatest
libraryDependencies += "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.39.7" % Test
// https://mvnrepository.com/artifact/com.dimafeng/testcontainers-scala-cassandra
libraryDependencies += "com.dimafeng" %% "testcontainers-scala-cassandra" % "0.39.7"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test"

Test / fork := true