
package pv260.solid.dip.solution.model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WeatherReport {

    private final Map<LocalDateTime, Double> averageTemperature;

    public WeatherReport(Map<LocalDateTime, Double> averageTemperature) {
        this.averageTemperature = Collections.unmodifiableMap(new HashMap<>(averageTemperature));
    }

    public Map<LocalDateTime, Double> averageTemperatures() {
        return this.averageTemperature;
    }

    public double averageTemperatureFor(LocalDate date){
        LocalDateTime date0 = date.atStartOfDay();
        LocalDateTime date24 = date.plusDays(1).atStartOfDay();
        double acc = 0;
        int valuesForDate =0;
        for(Map.Entry<LocalDateTime, Double> timeTemp: this.averageTemperature.entrySet()){
            if(timeTemp.getKey().compareTo(date0) >= 0 && timeTemp.getKey().compareTo(date24) <= 0){
                acc += timeTemp.getValue();
                valuesForDate++;
            }
        }
        if(valuesForDate == 0){
        throw new IllegalStateException("External service did not return record for tomorrow");
        }
        return acc / valuesForDate;
    }
}
