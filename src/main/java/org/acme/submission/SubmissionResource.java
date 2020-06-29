package org.acme.submission;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.acme.data.Submission;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
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

@ApplicationScoped
@Path("/submission")
public class SubmissionResource {

    private static final Logger log = LoggerFactory.getLogger(SubmissionResource.class);

    /* Poll time for updating from source */
    @ConfigProperty(name = "pollValue", defaultValue = "10")
    public int pollValue;

    @Inject
    @Channel("tv-emit-out")
    Emitter<Submission> emitter;

    @POST
    public void create(Submission submission) {
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
    public Publisher<String> stream() {
        return rawData;
    }

    /**
     * internal read method from realtime datasource
     *
     * @return
     */
    private List<String> _read() {
        List<String> subs = new ArrayList<>();
        JSONObject submission = new JSONObject();
        submission.put("location_lat", -27.502);
        submission.put("location_lng", 152.897);
        submission.put("timestamp_created", getRandomTime());

        JSONObject sentiment = new JSONObject();
        sentiment.put("capacity", new Random().nextInt(100) + 1);
        sentiment.put("route_direction", (new Random().nextBoolean() ? "City":"Country"));
        sentiment.put("route_number", "216");
        sentiment.put("route_type", "Bus");
        sentiment.put("stop_name", "Sunshine Station - City via Dynon Rd");
        sentiment.put("vibe", new Random().nextInt(100) + 1);
        sentiment.put("departure_time", getRandomTime());

        JSONObject submitter = new JSONObject();
        submitter.put("device_id", "8316080933289526961");

        submission.put("sentiment", sentiment);
        submission.put("submitter", submitter);

        subs.add(submission.toString());

        // add another
        sentiment.put("route_type", "Tram");
        sentiment.put("route_number", "90");
        submission.put("sentiment", sentiment);
        submission.put("timestamp_created", getRandomTime());
        sentiment.put("departure_time", getRandomTime());
        submission.put("sentiment", sentiment);
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
