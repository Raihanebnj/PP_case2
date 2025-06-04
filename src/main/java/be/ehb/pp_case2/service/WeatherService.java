package be.ehb.pp_case2.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class WeatherService {

    private final String apiKey = "44a74c719e73403e9d784330250406";

    public String getRegenVooruitzicht(String locatie) {
        String url = String.format(
                "http://api.weatherapi.com/v1/forecast.json?key=%s&q=%s&days=1&aqi=no&alerts=no",
                apiKey, locatie);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JSONObject json = new JSONObject(response);
        double mmRegenVandaag = json
                .getJSONObject("forecast")
                .getJSONArray("forecastday")
                .getJSONObject(0)
                .getJSONObject("day")
                .getDouble("totalprecip_mm");

        String dag = json
                .getJSONObject("forecast")
                .getJSONArray("forecastday")
                .getJSONObject(0)
                .getString("date");

        return String.format("Voorspelde neerslag voor %s in %s: %.1f mm", dag, locatie, mmRegenVandaag);
    }
}
