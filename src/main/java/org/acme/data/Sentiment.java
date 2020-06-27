package org.acme.data;

import java.util.Date;

public class Sentiment {

    public Sentiment() {
    }

    public Sentiment(String stop_name, String route_type, String route_number, String route_direction, String departure_time, Integer capacity, Integer vibe) {
        this.stop_name = stop_name;
        this.route_type = route_type;
        this.route_number = route_number;
        this.route_direction = route_direction;
        this.departure_time = departure_time;
        this.capacity = capacity;
        this.vibe = vibe;
    }

    // route service fields
    public String stop_name;
    public String route_type;
    public String route_number;
    public String route_direction;
    public String departure_time;

    // sentiment
    public Integer capacity;
    public Integer vibe;

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
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

    public String getRoute_direction() {
        return route_direction;
    }

    public void setRoute_direction(String route_direction) {
        this.route_direction = route_direction;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public Integer getVibe() {
        return vibe;
    }

    public void setVibe(Integer vibe) {
        this.vibe = vibe;
    }
}
