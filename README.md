# tv-submit project

TimelyDataFlow using materialize.io

```
# schema load
psql -h localhost -p 6875 materialize -f ./load.sql
psql -h localhost -p 6875 materialize -f ./load-auto.sql

# psql -h localhost -p 6875 materialize

# create kafka source
CREATE SOURCE tripvibe
FROM KAFKA BROKER 'localhost:9092' TOPIC 'tripvibe'
FORMAT TEXT;

SHOW COLUMNS FROM tripvibe;

# materialize all data
CREATE MATERIALIZED VIEW all_tripvibe AS
    SELECT CAST((text::JSONB)->'location_lng' as float) as location_lng,
           CAST((text::JSONB)->'location_lat' as float) as location_lat,
           CAST((text::JSONB)->'timestamp_created' AS timestamptz) as timestamp_created,
           CAST((text::JSONB)->'sentiment'->'departure_time' AS timestamptz) as departure_time,
           CAST((text::JSONB)->'sentiment'->'estimated_departure_time' AS timestamptz) as estimated_departure_time,           
           CAST((text::JSONB)->'sentiment'->'vibe' as integer) as vibe,
           CAST((text::JSONB)->'sentiment'->'capacity' as integer) as capacity,
           (text::JSONB)->'sentiment'->'direction' as direction,
           (text::JSONB)->'sentiment'->'direction_id' as direction_id,
           (text::JSONB)->'sentiment'->'route_type' as route_type,
           (text::JSONB)->'sentiment'->'route_number' as route_number,
           (text::JSONB)->'sentiment'->'route_id' as route_id,
           (text::JSONB)->'sentiment'->'stop_name' as stop_name,
           CAST((text::JSONB)->'at_platform' AS timestamptz) as at_platform,
           (text::JSONB)->'sentiment'->'platform_number' as platform_number,
           (text::JSONB)->'sentiment'->'run_id' as run_id,
           (text::JSONB)->'sentiment'->'stop_id' as stop_id,
           (text::JSONB)->'submitter'->'device_id' as device_id
    FROM (SELECT * FROM tripvibe);

SELECT * from all_tripvibe;
SHOW COLUMNS FROM all_tripvibe;

CREATE SOURCE ptv_all_routes
FROM FILE '/work/tv-submit-data/all_routes.json'
FORMAT REGEX '^(?P<data>\{.*)$';

SHOW COLUMNS FROM ptv_all_routes;

CREATE MATERIALIZED VIEW all_routes AS
    SELECT CAST((data::JSONB)->'route_type' as int) as route_type,
           CAST((data::JSONB)->'route_id' as int) as route_id,
           (data::JSONB)->'route_name' as route_name,
           (data::JSONB)->'route_number' as route_number
    FROM ptv_all_routes;


#CREATE MATERIALIZED VIEW all_routes AS
#  SELECT data::jsonb AS values FROM ptv_all_routes;

select * from all_routes LIMIT 10;

DROP VIEW all_routes;
DROP SOURCE ptv_all_routes;

#

SELECT avg(capacity) from ROUTE216
WHERE direction = 'foo'

SELECT avg(capacity) from ROUTE216
WHERE stop_name = 'foo'

SELECT avg(capacity) from ROUTE216
WHERE departure_time between start and end;

# materalize only the 216 bus
CREATE MATERIALIZED VIEW ROUTE216 AS
    SELECT location_lng, location_lat, timestamp_created, departure_time, estimated_departure_time, vibe, capacity, direction, direction_id, route_type, route_number, route_id, stop_name, at_platform, platform_number, run_id, stop_id, device_id
    FROM all_tripvibe
    WHERE route_number = '"216"';

SELECT * from ROUTE216;
SHOW COLUMNS FROM ROUTE216;

#
CREATE MATERIALIZED VIEW ROUTE90 AS
    SELECT location_lng, location_lat, timestamp_created, departure_time, estimated_departure_time, vibe, capacity, direction, direction_id, route_type, route_number, route_id, stop_name, at_platform, platform_number, run_id, stop_id, device_id  
    FROM all_tripvibe
    WHERE route_number = '"90"';

SELECT * from ROUTE90;
SHOW COLUMNS FROM ROUTE90;

# clean up if required
DROP VIEW ROUTE216;
DROP VIEW all_tripvibe;
DROP SOURCE tripvibe;
```

Delete kafka topic
```
# delete topic if you need to reset data (restart materaliaze container as well)
/opt/kafka_2.12-2.2.0/bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic tripvibe
```

## OpenShift

```bash
oc create -f ocp/openshift-deployment.yml
```
