package training.metofficeweather.web;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public class WeatherInfo {
    private final String locationId;

    public WeatherInfo(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getMetOfficeData(String locationId) throws IOException {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        String data = client.target("http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/"
                + locationId + "?res=3hourly&key=KEY")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        return data;
    }
}
