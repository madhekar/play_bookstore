import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(DockerPlugin)
  .settings(
      scalaVersion := "2.13.3",
      organization := "com.zesha",
      name := "book-app",
      version := "2.8.x",
      libraryDependencies ++= Seq(
         guice,
         jdbc,
         evolutions,
         "com.h2database" % "h2" % "1.4.199",
         "org.postgresql" % "postgresql" % "42.1.4",
         "org.playframework.anorm" %% "anorm" % "2.6.5",
         "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
       ),
       scalacOptions ++= List("-encoding", "utf8", "-deprecation", "-feature", "-unchecked", "-Xfatal-warnings"),
       javacOptions ++= List("-Xlint:unchecked", "-Xlint:deprecation", "-Werror"),

    packageName in Docker := "book-store-app",
    version in Docker := "latest",
    NativePackagerKeys.dockerBaseImage := "adoptopenjdk/openjdk9:x86_64-alpine-jdk-9.0.4.11",
    NativePackagerKeys.dockerExposedPorts := Seq(9000,9443),
    NativePackagerKeys.dockerExposedVolumes := Seq("/opt/docker/logs"),
    dockerCommands ++= Seq(
      Cmd("USER", "root"),
      ExecCmd("RUN", "apk", "add", "--no-cache", "bash")
    )
    // dockerEntrypoint := Seq("-Denv=dev")
  )

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.zesha.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.zesha.binders._"

// https://mvnrepository.com/artifact/org.postgresql/postgresql
//libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1200-jdbc41" -- for 9.4
//libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4" -- for version 10

// https://mvnrepository.com/artifact/mysql/mysql-connector-java
// libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.21"
