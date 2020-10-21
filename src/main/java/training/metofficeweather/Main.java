package training.metofficeweather;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class Main {
    private static final String API = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/sitelist?key=";
    private static final Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

    public static void main(String[] args) {
        Root root = client.target(API)
                .request(MediaType.APPLICATION_JSON)
                .get(Root.class);

        Location[] locations = root.getLocations().getLocation();

        for (Location location : locations) {
            if (location.getId().equals("3660")) {
                System.out.println(location.getName());
            }
        }
    }
}	
