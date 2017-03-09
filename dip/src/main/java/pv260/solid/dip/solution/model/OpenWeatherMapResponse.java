package pv260.solid.dip.solution.model;

import java.util.List;
import javax.xml.bind.annotation.*;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

@XmlRootElement(name = "weatherdata")
@XmlAccessorType(NONE)
public class OpenWeatherMapResponse {

    @XmlElement
    private Location location;

    @XmlElement(name = "time")
    @XmlElementWrapper(name = "forecast")
    private List<ForecastTime> times;

    public Location getLocation() {
        return location;
    }

    public List<ForecastTime> getTimes() {
        return times;
    }

    @Override
    public String toString() {
        return "OpenWeatherMapResponse{" + "location=" + location + ", times=" + times + '}';
    }

    @XmlType
    public static class Location{
        @XmlElement
        private String name;
        @XmlElement
        private String country;

        @Override
        public String toString() {
            return "Location{" + "name=" + name + ", country=" + country + '}';
        }
    }

    @XmlType
    public static class ForecastTime {

        @XmlAttribute
        private String day;

        @XmlElement
        private Temperature temperature;

        public String getDay() {
            return day;
        }

        public Temperature getTemperature() {
            return temperature;
        }

        @Override
        public String toString() {
            return "ForecastTime{" + "day=" + day + ", temperature=" + temperature + '}';
        }
    }

    @XmlType
    public static class Temperature {

        @XmlAttribute
        private double morn;

        @XmlAttribute
        private double day;

        @XmlAttribute
        private double eve;

        @XmlAttribute
        private double night;

        public double getMorn() {
            return morn;
        }

        public double getDay() {
            return day;
        }

        public double getEve() {
            return eve;
        }

        public double getNight() {
            return night;
        }

        @Override
        public String toString() {
            return "Temperature{" + "morn=" + morn + ", day=" + day + ", eve=" + eve + ", night=" + night + '}';
        }
    }
}
