import ByteConversions._
import com.typesafe.sbt.packager.docker._

lazy val commonSettings: Seq[Setting[_]] = Seq(
  version in ThisBuild := "0.1.0-SNAPSHOT"
  )

lazy val root = (project in file(".")).
  enablePlugins(ConductRPlugin, JavaAppPackaging).
  settings(
    commonSettings,
    name := "postgres-bdr",
    dockerCommands := Seq(
      Cmd("FROM", "agios/postgres-bdr"),
      Cmd("ADD", "/opt/docker/bin/init-database.sh /docker-entrypoint-initdb.d/")
    ),
    scriptClasspathOrdering := Seq.empty,
    BundleKeys.bundleType := Docker,
    BundleKeys.nrOfCpus := 4.0,
    BundleKeys.memory := 2.GB,
    BundleKeys.diskSpace := 10.GB,
    BundleKeys.endpoints := Map(
      "postgres" -> Endpoint("tcp", 5432, services = Set(uri("tcp://:5432")))
    ),
    BundleKeys.startCommand := Seq.empty,
    BundleKeys.checks := Seq(uri("docker+$POSTGRES_HOST"))
  )

