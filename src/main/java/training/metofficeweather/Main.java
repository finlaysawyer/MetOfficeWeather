package training.metofficeweather;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class Main {
    private static final String API = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/sitelist?key=";
    private static final Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    private static final String API_HOURLY = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/";
    private static final String API_HOURLY_ARGS = "?res=3hourly&key=";

    public static void main(String[] args) {
        String data = client.target(API_HOURLY + 310042 + API_HOURLY_ARGS)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonObject dataObject = JsonParser.parseString(data).getAsJsonObject();

        JsonArray dataArray = dataObject.get("SiteRep")
                .getAsJsonObject().get("DV")
                .getAsJsonObject().get("Location")
                .getAsJsonObject().get("Period").getAsJsonArray();

        for (JsonElement e : dataArray) {
            JsonArray hourlyForecast = e.getAsJsonObject().get("Rep").getAsJsonArray();
            String date = e.getAsJsonObject().get("value").toString();

            for (JsonElement element : hourlyForecast) {
                JsonObject day = element.getAsJsonObject();
                System.out.println(
                        "Date - " + date +
                                "\nFeels Like - " + day.get("F").getAsString() +
                                "\nWind Gust - " + day.get("G").getAsString() +
                                "\nRelative Humidity - " + day.get("H").getAsString() +
                                "\nTemperature: " + day.get("T").getAsString() +
                                "\nVisibility: " + day.get("V").getAsString() +
                                "\nWind Direction: " + day.get("D").getAsString() +
                                "\nWind Speed: " + day.get("S").getAsString() +
                                "\nMax UV: " + day.get("U").getAsString() +
                                "\nWeather Type: " + day.get("W").getAsString() +
                                "\nPrecipitation Probability: " + day.get("Pp").getAsString()
                );
            }
        }
    }
}	
