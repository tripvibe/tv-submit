package org.acme.data;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name="all_tripvibe")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Route extends PanacheEntityBase {

    private final static Logger log = LoggerFactory.getLogger(Route.class);

    private Float location_lng;
    private Float location_lat;
    private Integer vibe;
    private Integer capacity;
    private String route_direction;
    private String route_type;
    private String route_number;
    private String timestamp_created;
    private String departure_time;
    private String stop_name;

    @Id
    private String device_id;

    public Route() {
    }

    public Float getLocation_lng() {
        return location_lng;
    }

    public void setLocation_lng(Float location_lng) {
        this.location_lng = location_lng;
    }

    public Float getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(Float location_lat) {
        this.location_lat = location_lat;
    }

    public Integer getVibe() {
        return vibe;
    }

    public void setVibe(Integer vibe) {
        this.vibe = vibe;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRoute_direction() {
        return route_direction;
    }

    public void setRoute_direction(String route_direction) {
        this.route_direction = route_direction;
    }

    public String getRoute_type() {
        return route_type;
    }

    public void setRoute_type(String route_type) {
        this.route_type = route_type;
    }

    public String getRoute_number() {
        return route_number;
    }

    public void setRoute_number(String route_number) {
        this.route_number = route_number;
    }

    public String getTimestamp_created() {
        return timestamp_created;
    }

    public void setTimestamp_created(String timestamp_created) {
        this.timestamp_created = timestamp_created;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    /* reactive ps client busted

    public Route(Float location_lng, Float location_lat, Integer vibe, Integer capacity, String route_direction, String route_type, String route_number, String timestamp_created, String departure_time, String stop_name, String device_id) {
        this.location_lng = location_lng;
        this.location_lat = location_lat;
        this.vibe = vibe;
        this.capacity = capacity;
        this.route_direction = route_direction;
        this.route_type = route_type;
        this.route_number = route_number;
        this.timestamp_created = timestamp_created;
        this.departure_time = departure_time;
        this.stop_name = stop_name;
        this.device_id = device_id;
    }

    private static Route from(Row row) {
        return new Route(row.getFloat("location_lng"), row.getFloat("location_lat"), row.getInteger("vibe"), row.getInteger("capacity"), row.getString("route_direction"), row.getString("route_type"), row.getString("route_number"), row.getString("timestamp_created"), row.getString("departure_time"), row.getString("stop_name"), row.getString("device_id"));
    }

    public static Multi<Route> findAll(PgPool client) {
        return client.query("SELECT * from ROUTE216").execute()
                .onItem().produceMulti(set -> Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                .onItem().apply(Route::from);
    }
    */
}
