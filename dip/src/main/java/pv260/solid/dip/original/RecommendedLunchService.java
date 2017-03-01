
package pv260.solid.dip.original;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import pv260.solid.dip.original.model.DarkSkyForecastResponse;
import pv260.solid.dip.original.model.DarkSkyForecastResponse.DailyData;
import pv260.solid.dip.original.model.OpenWeatherMapResponse;

public class RecommendedLunchService {
private final DarkSkyForecastService weatherService;

    public RecommendedLunchService() {
        this.weatherService = new DarkSkyForecastService();
    }

    public String recomendLunchForTomorrow(){
         try {
            double temperatureAroundLunch = this.findTomorrowsTemperatureAroundLunch();
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

      private double findTomorrowsTemperatureAroundLunch() throws IOException{
          DailyData tomorrowRecord = this.        tomorrowsWeatherRecord();
          return (tomorrowRecord.getTemperatureMin()+tomorrowRecord.getTemperatureMax()) /2;
    }


    private DailyData tomorrowsWeatherRecord() throws IOException {
        DarkSkyForecastResponse forecast = this.weatherService.queryService();
        for (DailyData  record : forecast.getDaily().getData()) {
            LocalDateTime recordTime = LocalDateTime.ofEpochSecond(record.getTime(),
                                                                   0,
                                                                   ZoneOffset.UTC);
            if(recordTime.toLocalDate().equals(LocalDate.now().plusDays(1))){
                return record;
            }
        }
        throw new IllegalStateException("External service did not return record for tomorrow");
    }

}
