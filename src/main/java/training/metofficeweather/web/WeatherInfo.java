package training.metofficeweather.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.glassfish.jersey.jackson.JacksonFeature;
import training.metofficeweather.location.Location;
import training.metofficeweather.location.Root;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class WeatherInfo {
    private final String locationId;
    private static final String API_LOCATIONS = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/sitelist?key=c394cd7c-9d64-49bf-94d1-aa05dce0adad";
    private static final String API_HOURLY = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/";
    private static final String API_HOURLY_ARGS = "?res=3hourly&key=";
    private static final Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

    public WeatherInfo(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationId() {
        return locationId;
    }

    public static String getLocationInfo(String locationId) {
        Root root = client.target(API_LOCATIONS)
                .request(MediaType.APPLICATION_JSON)
                .get(Root.class);

        Location[] locations = root.getLocations().getLocation();

        for (Location location : locations) {
            if (location.getId().equals(locationId)) {
                return location.getName() + ", " + location.getUnitaryAuthArea() + " (" + location.getId() + ")";
            }
        }
        return null;
    }

    public static List<List<String>> getWeatherForecast(String locationId) {
        String data = client.target(API_HOURLY + locationId + API_HOURLY_ARGS)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonObject dataObject = JsonParser.parseString(data).getAsJsonObject();

        JsonArray dataArray = dataObject.get("SiteRep")
                .getAsJsonObject().get("DV")
                .getAsJsonObject().get("Location")
                .getAsJsonObject().get("Period").getAsJsonArray();

        List<List<String>> forecastDays = new ArrayList<>();

        for (JsonElement e : dataArray) {
            int hour = 0;
            JsonArray hourlyForecast = e.getAsJsonObject().get("Rep").getAsJsonArray();

            List<String> forecastList = new ArrayList<>();
            forecastList.add(e.getAsJsonObject().get("value").getAsString().replace("Z", ""));

            for (JsonElement element : hourlyForecast) {
                JsonObject day = element.getAsJsonObject();
                forecastList.add(
                        "<strong>" + Integer.toString(hour)+ ":00</strong>" +
                        "<br><i class=\"fas fa-temperature-high\"></i>&nbsp; Feels Like: " + day.get("F").getAsString() + "°C" +
                        "<br><i class=\"fas fa-temperature-low\"></i>&nbsp; Temperature: " + day.get("T").getAsString() + "°C" +
                        "<br><i class=\"fas fa-tint\"></i>&nbsp;&nbsp; Relative Humidity - " + day.get("H").getAsString() +
                        "<br><i class=\"fas fa-glasses\"></i>&nbsp; Visibility: " + day.get("V").getAsString() +
                        "<br><i class=\"fas fa-wind\"></i>&nbsp; Wind Gust: " + day.get("G").getAsString() +
                        "<br><i class=\"fas fa-wind\"></i>&nbsp; Wind Direction: " + day.get("D").getAsString() +
                        "<br><i class=\"fas fa-wind\"></i>&nbsp; Wind Speed: " + day.get("S").getAsString() +
                        "<br><i class=\"fas fa-sun\"></i>&nbsp; Max UV: " + day.get("U").getAsString() +
                        "<br><i class=\"fas fa-cloud\"></i>&nbsp; Weather Type: " + day.get("W").getAsString() +
                        "<br><i class=\"fas fa-cloud-showers-heavy\"></i>&nbsp; Precipitation Probability: " + day.get("Pp").getAsString()
                );
                hour += 3;
            }

            forecastDays.add(forecastList);
        }
        return forecastDays;
    }
}
