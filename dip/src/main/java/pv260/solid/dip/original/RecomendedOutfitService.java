package pv260.solid.dip.original;

import java.io.IOException;
import java.time.LocalDate;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import pv260.solid.dip.original.model.OpenWeatherMapResponse;
import pv260.solid.dip.original.model.OpenWeatherMapResponse.ForecastTime;
import pv260.solid.dip.original.model.OpenWeatherMapResponse.Temperature;

public class RecomendedOutfitService {

    private final OpenWeatherMapService weatherService;

    public RecomendedOutfitService() {
        this.weatherService = new OpenWeatherMapService();
    }

    public String recomendOutfitForTomorrow() {
        try {
            double averageTemperature = this.findTomorrowsAverageTemperature();
            if (averageTemperature < -10) {
                return "It will be super cold, weak a jacket or two!";
            } else if (averageTemperature < 0) {
                return "It will be rather chilly, better wear a coat.";
            } else if (averageTemperature < 15) {
                return "Weather will be very pleasant, weak a light jacket and jeans.";
            } else if (averageTemperature < 25) {
                return "Tomorrow will be a beautiful day, shirt and shorst should be fine.";
            } else {
                return "It will be really hot, better grab a swimsuit and run to the beach!";
            }
        } catch (IOException e) {
            return "Error when calculating best outfit for tomorrow";
        }
    }

    private double findTomorrowsAverageTemperature() throws IOException {
        Temperature tomorrowTemperature = this.obtainTomorrowTemperatureRecord();
        return (tomorrowTemperature.getMorn()
                + tomorrowTemperature.getDay()
                + tomorrowTemperature.getEve()) / 3;
    }

    private Temperature obtainTomorrowTemperatureRecord() throws IOException {
        OpenWeatherMapResponse tomorrowWeather = this.weatherService.query();
        for (ForecastTime record : tomorrowWeather.getTimes()) {
            if (isTomorrow(LocalDate.parse(record.getDay(), ISO_DATE))) {
                return record.getTemperature();
            }
        }
        throw new IllegalStateException("External service did not return record for tomorrow");
    }

    //other services

    private static boolean isTomorrow(LocalDate date) {
        return date.equals(LocalDate.now().plusDays(1));
    }

}
