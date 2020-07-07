package org.acme.submission;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.acme.data.Submission;
import org.acme.rest.SCSubmitService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.SseElementType;
import org.json.JSONObject;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

@Path("/submission")
@ApplicationScoped
public class SubmissionResource {

    private static final Logger log = LoggerFactory.getLogger(SubmissionResource.class);

    /* Poll time for updating from source */
    @ConfigProperty(name = "pollValue", defaultValue = "10")
    public int pollValue;

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
    public void create(Submission submission) {
        try {
            scSubmitService.evictSingle(submission.getSentiment().getRoute_id());
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        submission.setTimestamp_created(getNow());
        emitter.send(submission);
    }

    /**
     * Blocking read from source
     *
     * @return
     */
    private Multi<String> read() {
        Multi<String> blocking = Multi.createFrom().iterable(_read()).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
        return blocking;
    }

    /**
     * Send data to kafka topic
     *
     * @return
     * @throws IOException
     */
    @Outgoing("tv-out")
    public Multi<String> generate() throws IOException {
        Multi<Long> ticks = Multi.createFrom().ticks().every(Duration.ofSeconds(pollValue)).onOverflow().drop();
        return ticks.onItem().produceMulti(
                x -> read()
        ).merge();
    }

    @Inject
    @Channel("tv-in")
    Publisher<String> rawData;

    /**
     * Read kafka topic and send as SSE
     *
     * @return
     */
    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON) //avro/binary
    @Operation(operationId = "stream",
            summary = "stream all sentiment submissions",
            description = "This operation allows you to stream server side events for all sentiment submissions",
            deprecated = false,
            hidden = false)
    public Publisher<String> stream() {
        return rawData;
    }

    private List<String> _read() {
        List<String> subs = new ArrayList<>();
        JSONObject submission = new JSONObject();
        submission.put("location_lat", -27.502);
        submission.put("location_lng", 152.897);
        submission.put("timestamp_created", getRandomTime());

        JSONObject sentiment = new JSONObject();
        sentiment.put("capacity", new Random().nextInt(100) + 1);
        sentiment.put("direction", (new Random().nextBoolean() ? "City" : "Country"));
        sentiment.put("direction_id", (new Random().nextBoolean() ? "13" : "63"));
        sentiment.put("route_number", "216");
        sentiment.put("route_type", "2");
        sentiment.put("route_id", "887");
        sentiment.put("run_id", "RUN367");
        sentiment.put("stop_name", "Sunshine Station - City via Dynon Rd");
        sentiment.put("stop_id", "3155");
        sentiment.put("vibe", new Random().nextInt(100) + 1);
        sentiment.put("departure_time", getRandomTime());
        sentiment.put("estimated_departure_time", getRandomTime());
        sentiment.put("at_platform", false);
        sentiment.put("platform_number", "");

        JSONObject submitter = new JSONObject();
        submitter.put("device_id", "8316080933289526961");

        submission.put("sentiment", sentiment);
        submission.put("submitter", submitter);

        subs.add(submission.toString());

        // add another
        submitter.put("device_id", "9372372349923333");

        sentiment.put("route_type", "Tram");
        sentiment.put("route_number", "90");
        sentiment.put("departure_time", getRandomTime());
        sentiment.put("capacity", new Random().nextInt(50) + 51);
        sentiment.put("direction", (new Random().nextBoolean() ? "City" : "Country"));
        sentiment.put("direction_id", (new Random().nextBoolean() ? "13" : "63"));
        sentiment.put("route_number", "90");
        sentiment.put("route_type", "3");
        sentiment.put("route_id", "11795");
        sentiment.put("run_id", "RUN12");
        sentiment.put("stop_name", "Dandenong");
        sentiment.put("stop_id", "21");
        sentiment.put("vibe", new Random().nextInt(50) + 51);
        sentiment.put("departure_time", getRandomTime());
        sentiment.put("estimated_departure_time", getRandomTime());
        sentiment.put("at_platform", true);
        sentiment.put("platform_number", "1");

        submission.put("timestamp_created", getRandomTime());
        submission.put("sentiment", sentiment);
        submission.put("submitter", submitter);

        subs.add(submission.toString());

        return subs;
    }

    private String getNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        long now = new Date().getTime();
        Date date = new Date(now);
        return df.format(date);
    }

    private String getRandomTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        long now = new Date().getTime();
        long minutes = (new Random().nextInt(3600) + 1) * 60000;
        Date date = new Date(now + minutes);
        return df.format(date);
    }
}
