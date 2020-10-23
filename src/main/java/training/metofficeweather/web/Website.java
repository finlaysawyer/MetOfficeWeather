package training.metofficeweather.web;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import training.metofficeweather.location.Location;
import training.metofficeweather.location.Root;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@Controller
@EnableAutoConfiguration
public class Website {

    private static final Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    private static final String API_LOCATIONS = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/sitelist?key=";

    @RequestMapping("/")
    ModelAndView home() {
        return new ModelAndView("index");
    }

    @RequestMapping("/weatherInfo")
    ModelAndView weatherInfo(@RequestParam("locationId") String locationName) {
        Root root = client.target(API_LOCATIONS)
                .request(MediaType.APPLICATION_JSON)
                .get(Root.class);

        Location[] locations = root.getLocations().getLocation();

        for (Location location : locations) {
            if (location.getName().equals(locationName)) {
                return new ModelAndView("info", "weatherInfo", new WeatherInfo(location.getId()));
            }
        }
        return new ModelAndView("/error");
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Website.class, args);
    }

}