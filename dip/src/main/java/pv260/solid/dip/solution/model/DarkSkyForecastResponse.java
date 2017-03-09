
package pv260.solid.dip.solution.model;

import java.util.List;

public class DarkSkyForecastResponse {
    private double latitude;
    private double longitude;
    private String timezone;
    private Daily daily;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public Daily getDaily() {
        return daily;
    }

    @Override
    public String toString() {
        return "DarkSkyForecastResponse{" + "latitude=" + latitude + ", longitude=" + longitude + ", timezone=" + timezone + ", daily=" + daily + '}';
    }

    public static class Daily {

        private String summary;

        private List<DailyData> data;

        public String getSummary() {
            return summary;
        }

        public List<DailyData> getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Daily{" + "summary=" + summary + ", data=" + data + '}';
        }
    }

    public static class DailyData {

        private long time;

        private double temperatureMin;

        private double temperatureMax;

        public long getTime() {
            return time;
        }

        public double getTemperatureMin() {
            return temperatureMin;
        }

        public double getTemperatureMax() {
            return temperatureMax;
        }

        @Override
        public String toString() {
            return "DailyData{" + "time=" + time + '}';
        }

    }

}
