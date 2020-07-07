package org.acme.submission;

import org.acme.data.Route;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/query")
public class QueryResource {

    private final Logger log = LoggerFactory.getLogger(QueryResource.class);

    @Inject
    EntityManager entityManager;

    @GET
    @Path("/submissions/route/{route_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getRoutes",
            summary = "get submissions",
            description = "This operation returns all submissions for a route_id",
            deprecated = false,
            hidden = false)
    public List<Route> getRoutes(@PathParam String route_id) {
        return entityManager.createQuery("select r from ROUTE" + route_id + " r", Route.class).getResultList();
    }

    @GET
    @Path("/count/{route_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "count",
            summary = "count submissions",
            description = "This operation returns a count of all submissions for a route_id",
            deprecated = false,
            hidden = false)
    public Long count(@PathParam String route_id) {
        return entityManager.createQuery("select count(*) from ROUTE" + route_id, Long.class).getSingleResult();
    }

    @GET
    @Path("/count/{route_id}/{direction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "countDirected",
            summary = "query submissions by route and direction",
            description = "This operation returns all submissions for a route_id and direction_id",
            deprecated = false,
            hidden = false)
    public Long countDirected(@PathParam String route_id, @PathParam String direction_id) {
        return entityManager.createQuery("select count(*) from ROUTE" + route_id + " where direction_id = '\"" + direction_id + "\"'", Long.class).getSingleResult();
    }

    @GET
    @Path("/submissions/device/{device_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "queryDeviceID",
            summary = "query submissions by device_id",
            description = "This operation returns all submissions for a device_id",
            deprecated = false,
            hidden = false)
    public List<Route> queryDeviceID(@PathParam String device_id) {
        return entityManager.createQuery("select r from ROUTEALL r where device_id = '\"" + device_id + "\"'", Route.class).getResultList();
    }

    @GET
    @Path("/capacity-average/{route_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgCapacity",
            summary = "average capacity by route_id",
            description = "This operation returns the average capacity by route_id",
            deprecated = false,
            hidden = false)
    public Double avgCapacity(@PathParam String route_id) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id, Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgVibe",
            summary = "average vibe by route_id",
            description = "This operation returns the average vibe by route_id",
            deprecated = false,
            hidden = false)
    public Double avgVibe(@PathParam String route_id) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id, Double.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}/{direction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgCapacityDirected",
            summary = "average capacity by route_id and direction_id",
            description = "This operation returns the average capacity by route_id and direction_id",
            deprecated = false,
            hidden = false)
    public Double avgCapacityDirected(@PathParam String route_id, @PathParam String direction_id) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id + " where direction_id = '\"" + direction_id + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}/{direction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgVibeDirected",
            summary = "average vibe by route_id and direction_id",
            description = "This operation returns the average vibe by route_id and direction_id",
            deprecated = false,
            hidden = false)
    public Double avgVibeDirected(@PathParam String route_id, @PathParam String direction_id) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id + " where direction_id = '\"" + direction_id + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgCapacityDateTime",
            summary = "average capacity by route_id between start and end datetime",
            description = "This operation returns the average capacity by route_id between start and end datetime",
            deprecated = false,
            hidden = false)
    public Double avgCapacityDateTime(@PathParam String route_id, @PathParam String start, @PathParam String end) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "'", Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgVibeDateTime",
            summary = "average vibe by route_id between start and end datetime",
            description = "This operation returns the average vibe by route_id between start and end datetime",
            deprecated = false,
            hidden = false)
    public Double avgVibeDateTime(@PathParam String route_id, @PathParam String start, @PathParam String end) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "'", Double.class).getSingleResult();
    }

    @GET
    @Path("/count/{route_id}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "countDateTime",
            summary = "count of submissions by route_id between start and end datetime",
            description = "This operation returns the count of submissions by route_id and between start and end datetime",
            deprecated = false,
            hidden = false)
    public Double countDateTime(@PathParam String route_id, @PathParam String start, @PathParam String end) {
        return entityManager.createQuery("select count(*) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "'", Double.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}/{start}/{end}/{direction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgCapacityDateTimeDirected",
            summary = "average capacity by route_id, direction_id between start and end datetime",
            description = "This operation returns the average capacity by route_id, direction_id between start and end datetime",
            deprecated = false,
            hidden = false)
    public Double avgCapacityDateTimeDirected(@PathParam String route_id, @PathParam String start, @PathParam String end, @PathParam String direction_id) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "' and direction_id = '\"" + direction_id + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}/{start}/{end}/{direction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgVibeDateTimeDirected",
            summary = "average vibe by route_id, direction_id between start and end datetime",
            description = "This operation returns the average vibe by route_id, direction_id between start and end datetime",
            deprecated = false,
            hidden = false)
    public Double avgVibeDateTimeDirected(@PathParam String route_id, @PathParam String start, @PathParam String end, @PathParam String direction_id) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "' and direction_id = '\"" + direction_id + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/count/{route_id}/{start}/{end}/{direction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "countDateTimeDirected",
            summary = "count submissions by route_id, direction_id between start and end datetime",
            description = "This operation returns the count of submissions by route_id, direction_id between start and end datetime",
            deprecated = false,
            hidden = false)
    public Long countDateTimeDirected(@PathParam String route_id, @PathParam String start, @PathParam String end, @PathParam String direction_id) {
        return entityManager.createQuery("select count(*) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "' and direction_id = '\"" + direction_id + "\"'", Long.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}/{route_type}/{direction_id}/{run_id}/{stop_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgCapacityAllID",
            summary = "average capacity by route_id, route_type, direction_id, run_id and stop_id",
            description = "This operation returns the average capacity by route_id, route_type, direction_id, run_id and stop_id",
            deprecated = false,
            hidden = false)
    public Double avgCapacityAllID(@PathParam String route_id, @PathParam String route_type, @PathParam String direction_id, @PathParam String run_id, @PathParam String stop_id) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id + " where route_type = '\"" + route_type + "\"' and direction_id = '\"" + direction_id + "\"' and run_id = '\"" + run_id + "\"' and stop_id = '\"" + stop_id + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}/{route_type}/{direction_id}/{run_id}/{stop_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgVibeAllID",
            summary = "average vibe by route_id, route_type, direction_id, run_id and stop_id",
            description = "This operation returns the average vibe by route_id, route_type, direction_id, run_id and stop_id",
            deprecated = false,
            hidden = false)
    public Double avgVibeAllID(@PathParam String route_id, @PathParam String route_type, @PathParam String direction_id, @PathParam String run_id, @PathParam String stop_id) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id + " where route_type = '\"" + route_type + "\"' and direction_id = '\"" + direction_id + "\"' and run_id = '\"" + run_id + "\"' and stop_id = '\"" + stop_id + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/count/{route_id}/{route_type}/{direction_id}/{run_id}/{stop_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "countAllID",
            summary = "count of submissions route_id, route_type, direction_id, run_id and stop_id",
            description = "This operation returns the count of submissions by route_id, route_type, direction_id, run_id and stop_id",
            deprecated = false,
            hidden = false)
    public Long countAllID(@PathParam String route_id, @PathParam String route_type, @PathParam String direction_id, @PathParam String run_id, @PathParam String stop_id) {
        return entityManager.createQuery("select count(*) from ROUTE" + route_id + " where route_type = '\"" + route_type + "\"' and direction_id = '\"" + direction_id + "\"' and run_id = '\"" + run_id + "\"' and stop_id = '\"" + stop_id + "\"'", Long.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}/{route_type}/{direction_id}/{run_id}/{stop_id}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgCapacityAllIDDatetime",
            summary = "average capacity by route_id, route_type, direction_id and run_id between start and end datetime",
            description = "This operation returns the average capacity by route_id, route_type, direction_id and run_id between start and end datetime",
            deprecated = false,
            hidden = false)
    public Double avgCapacityAllIDDatetime(@PathParam String route_id, @PathParam String route_type, @PathParam String direction_id, @PathParam String run_id, @PathParam String stop_id, @PathParam String start, @PathParam String end) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id + " where route_type = '\"" + route_type + "\"' and direction_id = '\"" + direction_id + "\"' and run_id = '\"" + run_id + "\"' and stop_id = '\"" + stop_id + "\"' and departure_time between '" + start + "' and '" + end + "'", Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}/{route_type}/{direction_id}/{run_id}/{stop_id}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "avgVibeAllIDDatetime",
            summary = "average vibe by route_id, route_type, direction_id and run_id between start and end datetime",
            description = "This operation returns the average vibe by route_id, route_type, direction_id and run_id between start and end datetime",
            deprecated = false,
            hidden = false)
    public Double avgVibeAllIDDatetime(@PathParam String route_id, @PathParam String route_type, @PathParam String direction_id, @PathParam String run_id, @PathParam String stop_id, @PathParam String start, @PathParam String end) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id + " where route_type = '\"" + route_type + "\"' and direction_id = '\"" + direction_id + "\"' and run_id = '\"" + run_id + "\"' and stop_id = '\"" + stop_id + "\"' and departure_time between '" + start + "' and '" + end + "'", Double.class).getSingleResult();
    }

    @GET
    @Path("/count/{route_id}/{route_type}/{direction_id}/{run_id}/{stop_id}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "countAllIDDatetime",
            summary = "count of submissions by route_id, route_type, direction_id and run_id between start and end datetime",
            description = "This operation returns the count of submissions by route_id, route_type, direction_id and run_id between start and end datetime",
            deprecated = false,
            hidden = false)
    public Long countAllIDDatetime(@PathParam String route_id, @PathParam String route_type, @PathParam String direction_id, @PathParam String run_id, @PathParam String stop_id, @PathParam String start, @PathParam String end) {
        return entityManager.createQuery("select count(*) from ROUTE" + route_id + " where route_type = '\"" + route_type + "\"' and direction_id = '\"" + direction_id + "\"' and run_id = '\"" + run_id + "\"' and stop_id = '\"" + stop_id + "\"' and departure_time between '" + start + "' and '" + end + "'", Long.class).getSingleResult();
    }

}
