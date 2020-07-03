package org.acme.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RegisterRestClient
public interface SCSubmitService {

    @GET
    @Path("/evict-single/{route_id}")
    @Produces(MediaType.APPLICATION_JSON)
    void evictSingle(@PathParam String route_id);

}
