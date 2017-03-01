package pv260.solid.dip.original;

import pv260.solid.dip.original.model.DarkSkyForecastResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DarkSkyForecastService {

    private static final String SERVICE_URL = "https://api.forecast.io/forecast/";

    private static final String API_KEY = "fc2a39c15866166ea203cabadf93a236";

    private static final int CONNECTION_TIMEOUT = 500;

    private Gson jsonParser;

    public DarkSkyForecastService() {
        this.jsonParser = new Gson();
    }

    public DarkSkyForecastResponse queryService() throws IOException {
        URL remote = new URL(buildUrl(API_KEY,
                                      targetLongitude(),
                                      targetLatitude(),
                                      targetTime(),
                                      "units=si"));
        HttpURLConnection connection = (HttpURLConnection) remote.openConnection();
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        try (Reader responseReader = new InputStreamReader(connection.getInputStream(), UTF_8)) {
            return jsonParser.fromJson(responseReader, DarkSkyForecastResponse.class);
        }
    }

    private String buildUrl(String apiKey, double longitude, double latitude, String time, String... queryParams) {
        StringBuilder request =
                new StringBuilder().append(SERVICE_URL)
                                   .append(apiKey)
                                   .append('/').append(longitude).append(',').append(latitude)
                                   .append(',').append(time)
                                   .append('?');
        for (int i = 0; i < queryParams.length; i++) {
            if (i != 0) {
                request.append('&');
            }
            request.append(queryParams[i]);
        }
        return request.toString();
    }

    //these would be other services this depends on

    private static String targetTime() {
        return LocalDate.now().plusDays(2)
                            .atTime(12, 0)
                            .format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private static double targetLongitude() {
        return 49.1973419;
    }

    private static double targetLatitude() {
        return 16.6050103;
    }
}
