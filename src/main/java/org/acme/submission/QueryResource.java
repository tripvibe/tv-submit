package org.acme.submission;

import org.acme.data.Route;
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
    @Path("{route_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Route> getRoutes(@PathParam String route_id) {
        return entityManager.createQuery("select r from ROUTE" + route_id + " r", Route.class).getResultList();
    }

    @GET
    @Path("/count/{route_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Long count(@PathParam String route_id) {
        return entityManager.createQuery("select count(*) from ROUTE" + route_id + " r", Long.class).getSingleResult();
    }

    @GET
    @Path("/count/{route_id}/{route_direction}")
    @Produces(MediaType.APPLICATION_JSON)
    public Long countDirected(@PathParam String route_id, @PathParam String route_direction) {
        return entityManager.createQuery("select count(*) from ROUTE" + route_id + " r where route_direction = '\"" + route_direction + "\"'", Long.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double avgCapacity(@PathParam String route_id) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id , Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double avgVibe(@PathParam String route_id) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id, Double.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}/{route_direction}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double avgCapacityDirected(@PathParam String route_id, @PathParam String route_direction) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id + " where route_direction = '\"" + route_direction + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}/{route_direction}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double avgVibeDirected(@PathParam String route_id, @PathParam String route_direction) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id +  " where route_direction = '\"" + route_direction + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}/{start}/{end}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double avgCapacityDateTime(@PathParam String route_id, @PathParam String start, @PathParam String end) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "'", Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}/{start}/{end}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double avgVibeDateTime(@PathParam String route_id, @PathParam String start, @PathParam String end) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "'", Double.class).getSingleResult();
    }

    @GET
    @Path("/capacity-average/{route_id}/{start}/{end}/{route_direction}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double avgCapacityDateTimeDirected(@PathParam String route_id, @PathParam String start, @PathParam String end, @PathParam String route_direction) {
        return entityManager.createQuery("select avg(capacity) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "' and route_direction = '\"" + route_direction + "\"'", Double.class).getSingleResult();
    }

    @GET
    @Path("/vibe-average/{route_id}/{start}/{end}/{route_direction}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double avgVibeDateTimeDirected(@PathParam String route_id, @PathParam String start, @PathParam String end, @PathParam String route_direction) {
        return entityManager.createQuery("select avg(vibe) from ROUTE" + route_id + " where departure_time between '" + start + "' and '" + end + "' and route_direction = '\"" + route_direction + "\"'", Double.class).getSingleResult();
    }

}
