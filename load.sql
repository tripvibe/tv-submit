CREATE SOURCE tripvibe
FROM KAFKA BROKER 'localhost:9092' TOPIC 'tripvibe'
FORMAT TEXT;

CREATE MATERIALIZED VIEW all_tripvibe AS
    SELECT (text::JSONB)->'id' as id,
           CAST((text::JSONB)->'location_lng' as float) as location_lng,
           CAST((text::JSONB)->'location_lat' as float) as location_lat,
           CAST(CAST((text::JSONB)->>'timestamp_created' as text) as timestamptz) as timestamp_created,
           CAST(CAST((text::JSONB)->'sentiment'->>'departure_time' as text) as timestamptz) as departure_time,
           CAST(CAST((text::JSONB)->'sentiment'->>'estimated_departure_time' as text) as timestamptz) as estimated_departure_time,
           CAST((text::JSONB)->'sentiment'->'vibe' as integer) as vibe,
           CAST((text::JSONB)->'sentiment'->'capacity' as integer) as capacity,
           (text::JSONB)->'sentiment'->'direction' as direction,
           (text::JSONB)->'sentiment'->'direction_id' as direction_id,
           (text::JSONB)->'sentiment'->'route_type' as route_type,
           (text::JSONB)->'sentiment'->'route_number' as route_number,
           (text::JSONB)->'sentiment'->'route_id' as route_id,
           (text::JSONB)->'sentiment'->'stop_name' as stop_name,
           CAST(CAST((text::JSONB)->>'at_platform' as text) as timestamptz) as at_platform,
           (text::JSONB)->'sentiment'->'platform_number' as platform_number,
           (text::JSONB)->'sentiment'->'run_id' as run_id,
           (text::JSONB)->'sentiment'->'stop_id' as stop_id,
           (text::JSONB)->'submitter'->'device_id' as device_id
    FROM (SELECT * FROM tripvibe);

CREATE MATERIALIZED VIEW ROUTEALL AS
    SELECT * FROM all_tripvibe;

CREATE MATERIALIZED VIEW ROUTE216 AS
    SELECT *
    FROM all_tripvibe
    WHERE route_id = '"216"';

CREATE MATERIALIZED VIEW ROUTE90 AS
    SELECT *
    FROM all_tripvibe
    WHERE route_id = '"90"';
