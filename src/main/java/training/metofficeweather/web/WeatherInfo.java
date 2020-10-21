package training.metofficeweather.web;

import org.glassfish.jersey.jackson.JacksonFeature;
import training.metofficeweather.Location;
import training.metofficeweather.Root;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class WeatherInfo {
    private final String locationId;
    private static final String API = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/sitelist?key=";
    private static final Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

    public WeatherInfo(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationId() {
        return locationId;
    }

    public static String getLocationInfo(String locationId) {
        Root root = client.target(API)
                .request(MediaType.APPLICATION_JSON)
                .get(Root.class);

        Location[] locations = root.getLocations().getLocation();

        for (Location location : locations) {
            if (location.getId().equals(locationId)) {
                return location.getName() + " " + location.getRegion();
            }
        }
        return null;
    }
}
