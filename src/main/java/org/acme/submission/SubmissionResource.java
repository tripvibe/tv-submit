package org.acme.submission;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.reactive.messaging.kafka.OutgoingKafkaRecord;
import io.vertx.core.json.JsonObject;
import org.acme.data.Sentiment;
import org.acme.data.Submission;
import org.acme.data.Submitter;
import org.acme.rest.SCSubmitService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Path("/submission")
@ApplicationScoped
public class SubmissionResource {

    private static final Logger log = LoggerFactory.getLogger(SubmissionResource.class);

    @Inject
    @RestClient
    SCSubmitService scSubmitService;

    @Inject
    @Channel("tv-emit-out")
    Emitter<Submission> emitter;

    @POST
    @Operation(operationId = "create",
            summary = "create a sentiment submission",
            description = "This operation allows the UI to submit sentiment submissions",
            deprecated = false,
            hidden = false)
    public Response create(Submission submission) {
        try {
            String cacheKey = String.format("%s-%s-%s-%s-%s", submission.getSentiment().getRoute_id(), submission.getSentiment().getRoute_type(), submission.getSentiment().getDirection_id(), submission.getSentiment().getRun_id(), submission.getSentiment().getStop_id());
            scSubmitService.evictSingle(cacheKey);
            scSubmitService.evictSingle(submission.getSentiment().getRoute_id());
            cacheKey = String.format("%s-%s-%s-%s-%s-%s", submission.getSentiment().getRoute_id(), submission.getSentiment().getRoute_type(), submission.getSentiment().getDirection_id(), submission.getSentiment().getRun_id(), submission.getSentiment().getStop_id(), submission.getSentiment().getDeparture_time());
            scSubmitService.evictSingle(cacheKey);

        } catch (Exception ex) {
            log.warn("evictCache not available: " + ex.getMessage());
        }
        submission.setTimestamp_created(getNow());
        emitter.send(KafkaRecord.of(submission.getSentiment().getRoute_id(), submission));
        return Response.ok(submission).status(201).build();
    }

    private String getNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        long now = new Date().getTime();
        Date date = new Date(now);
        return df.format(date);
    }
}
