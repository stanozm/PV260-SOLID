package pv260.solid.dip.solution;

import java.io.IOException;
import java.time.LocalDate;

public class RecommendedLunchService {

    private final WeatherService weatherService;

    public RecommendedLunchService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public String recomendLunchForTomorrow() {
        try {
            double temperatureAroundLunch = this.weatherService.query().averageTemperatureFor(tomorrow());
            if (temperatureAroundLunch < -15) {
                return "You will need a lot of energy to keep warm, tomorrow you should eat something very nutritious.";
            } else if (temperatureAroundLunch < 15) {
                return "No day like tomorrow for some chicken.";
            } else if (temperatureAroundLunch < 30) {
                return "It will be quite hot tomorrow, be sure to order a cold beer with your lunch.";
            } else {
                return "You probably will not be hungry at all in such a hot weather. Just get an ice cream!";
            }
        } catch (IOException e) {
            return "Error when calculating best lunch recommendation for tomorrow";
        }
    }

    //other services

    private static LocalDate tomorrow() {
        return  LocalDate.now().plusDays(1);
    }


}
