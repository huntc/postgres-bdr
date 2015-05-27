# Postgres 9.4 BDR

This project declares a bundle that hosts the PostgresSQL [Bi-Directional Replication (BDR)](http://2ndquadrant.com/en/resources/bdr/) project. BDR provides PostgresSQL as a fully resilient SQL database with the ability to scale a master/master approach to 48 nodes.

Docker is used within the bundle to depend on the [Postgres BDR image on Dockerhub](https://registry.hub.docker.com/u/agios/postgres-bdr/). In addition a small script is provided that will configure BDR to either form a cluster or join a BDR cluster given the ConductR provided IPs and ports of other instances of the same bundle (if any).

To use the bundle you must supply a configuration when loading it into ConductR. A sample configuration is supplied in `src/bundle-configuration/example/bdrdemo/runtime-config.sh`. The configuration declares the database name and password that will be used. The following shows the database name as `bdrdemo` and the password as `mysecretpassword`. You should probably choose an appropriate database name and a more secure password. :-)

```
export POSTGRES_DBNAME=bdrdemo
export POSTGRES_PASSWORD=mysecretpassword
```

There's also a `bundle.conf` file alongside `runtime-config.sh`. This contains configuration that will override that supplied within the bundle's `bundle.conf`. In particular the name for looking up the endpoint is provided as `bdrdemo1`. This then allows clients to lookup the `bdrdemo1` and resolve that to a port number in much the same way that `/etc/services` operates for most Unix systems. The convention used here is to have the database name followed by some version style of string. That version should strong relate to the version of Postgres being used and the schema of your database. We're just using the major ident of `1`.

The bundle and configuration can be built and loaded into ConductR directly from the sbt console:

```
set configurationName := "example"
config:dist
bundle:dist
conduct load <hit tab><hit space><hit tab>
```
