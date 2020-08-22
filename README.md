# tv-submit project

Sentiment submission for tripvibe.

## Locally

Build and Run
```bash
mvn quarkus:dev
```

TimelyDataFlow using materialize.io
```
# schema load
psql -h localhost -p 6875 materialize -f ./load.sql
psql -h localhost -p 6875 materialize -f ./load-auto.sql
```

Delete kafka topic
```
# delete topic if you need to reset data (restart materialize container as well)
/opt/kafka_2.12-2.2.0/bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic tripvibe
```

## OpenShift

```bash
oc create -f ocp/openshift-deployment.yaml
```

