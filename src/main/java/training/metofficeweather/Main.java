package training.metofficeweather;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class Main {
    public static void main(String[] args) {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        String name = client.target("http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/310042?res=3hourly&key=")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println(name);
    }
}	
