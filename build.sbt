import ByteConversions._
import com.typesafe.sbt.packager.docker._

lazy val commonSettings: Seq[Setting[_]] = Seq(
  version in ThisBuild := "9.4"
  )

lazy val root = (project in file(".")).
  enablePlugins(JavaAppPackaging).
  settings(
    commonSettings,
    name := "postgres-bdr",
    dockerCommands := Seq(
      Cmd("FROM", "agios/postgres-bdr"),
      Cmd("ADD", "/opt/docker/bin/init-database.sh /docker-entrypoint-initdb.d/")
    ),
    scriptClasspathOrdering := Seq.empty,
    BundleKeys.system := "postgres94",
    BundleKeys.bundleType := Docker,
    BundleKeys.nrOfCpus := 2.0,
    BundleKeys.memory := 2.GB,
    BundleKeys.diskSpace := 10.GB,
    BundleKeys.roles := Set("postgres94"),
    BundleKeys.endpoints := Map(
      "postgres" -> Endpoint("tcp", 5432, RequestAcl(Tcp(5432)))
    ),
    BundleKeys.startCommand := Seq.empty,
    BundleKeys.checks := Seq(uri("docker+$POSTGRES_HOST"))
  )

