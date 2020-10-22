package training.metofficeweather.location;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Locations {
    @JsonProperty(value="Location")
    private Location[] locations;

    public Location[] getLocation() {
        return locations;
    }

    public void setLocation(Location[] location) {
        this.locations = location;
    }
}
