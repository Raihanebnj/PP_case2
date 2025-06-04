package be.ehb.pp_case2.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;


import java.util.*;


@Service
public class WeatherService {


    // Coördinaten van Vlaamse steden
    private static final Map<String, double[]> STEDEN = Map.of(
            "Antwerpen", new double[]{51.22, 4.40},
            "Gent", new double[]{51.05, 3.72},
            "Brugge", new double[]{51.21, 3.22},
            "Leuven", new double[]{50.88, 4.70},
            "Hasselt", new double[]{50.93, 5.34},
            "Brussel", new double[]{50.85, 4.35},
            "Kortrijk", new double[]{50.83, 3.26},
            "Aalst", new double[]{50.94, 4.04},
            "Mechelen", new double[]{51.03, 4.48},
            "Sint-Niklaas", new double[]{51.17, 4.14}
    );


    public String getRegenVooruitzicht() {
        int dagen = 5; // aantal dagen vooruit
        Map<String, List<Double>> stadNaarNeerslag = new LinkedHashMap<>();
        List<String> datums = new ArrayList<>();


        // RestTemplate aanmaken (voor elke stad)
        RestTemplate restTemplate = new RestTemplate();


        // Voor elke stad API-call doen
        for (var entry : STEDEN.entrySet()) {
            String stad = entry.getKey();
            double[] coords = entry.getValue();
            String url = String.format(
                    Locale.US, // Let op: punt ipv komma
                    "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&daily=precipitation_sum&forecast_days=%d&timezone=auto",
                    coords[0], coords[1], dagen
            );
            String response = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(response);
            JSONArray neerslag = json.getJSONObject("daily").getJSONArray("precipitation_sum");
            JSONArray times = json.getJSONObject("daily").getJSONArray("time");


            // Haal de datums 1x uit de eerste stad
            if (datums.isEmpty()) {
                for (int i = 0; i < dagen; i++) {
                    datums.add(times.getString(i));
                }
            }


            // Voeg neerslagdata toe voor deze stad
            List<Double> waarden = new ArrayList<>();
            for (int i = 0; i < dagen; i++) {
                waarden.add(neerslag.getDouble(i));
            }
            stadNaarNeerslag.put(stad, waarden);
        }


        // Bereken dagelijks gemiddelde
        StringBuilder resultaat = new StringBuilder("Gemiddelde voorspelde neerslag voor Vlaanderen (in mm):\n");
        for (int dag = 0; dag < dagen; dag++) {
            double som = 0.0;
            for (List<Double> waardes : stadNaarNeerslag.values()) {
                som += waardes.get(dag);
            }
            double gemiddelde = som / stadNaarNeerslag.size();
            resultaat.append(String.format("%s: %.1f mm\n", datums.get(dag), gemiddelde));
        }


        // (Optioneel: individuele stadswaardes tonen)
        resultaat.append("\nDetails per stad:\n");
        for (String stad : stadNaarNeerslag.keySet()) {
            resultaat.append(stad).append(": ");
            List<Double> waardes = stadNaarNeerslag.get(stad);
            for (int dag = 0; dag < dagen; dag++) {
                resultaat.append(String.format("%.1f", waardes.get(dag)));
                if (dag != dagen - 1) resultaat.append(", ");
            }
            resultaat.append(" mm\n");
        }


        return resultaat.toString();
    }
}
