#!/usr/bin/env bash

IFS=':'; read -ra POSTGRES_OTHER_IP   <<< "$POSTGRES_OTHER_IPS"
IFS=':'; read -ra POSTGRES_OTHER_PORT <<< "$POSTGRES_OTHER_PORTS"

if [ -z $POSTGRES_OTHER_IP ]; then
  JOIN=
else
  JOIN=",
  join_using_dsn := 'host=$POSTGRES_OTHER_IP port=$POSTGRES_OTHER_PORT dbname=$POSTGRES_DBNAME password=$POSTGRES_PASSWORD'"
fi

gosu postgres postgres --single <<- EOSQL
  CREATE DATABASE $POSTGRES_DBNAME;
EOSQL

gosu postgres pg_ctl -w start

psql -U postgres $POSTGRES_DBNAME <<- EOSQL
  CREATE EXTENSION btree_gist;
  CREATE EXTENSION bdr;

  SELECT bdr.bdr_group_create(
    local_node_name := '$HOST_IP-$POSTGRES_HOST_PORT',
    node_external_dsn := 'host=$HOST_IP port=$POSTGRES_HOST_PORT dbname=$POSTGRES_DBNAME password=$POSTGRES_PASSWORD'$JOIN
  );
EOSQL

gosu postgres pg_ctl -m fast -w stop
