package pv260.solid.dip.original;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import pv260.solid.dip.original.model.OpenWeatherMapResponse;

public class OpenWeatherMapService {

    private static final String SERVICE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";

    private static final String API_KEY = "44db6a862fba0b067b1930da0d769e98";

    private static final int CONNECTION_TIMEOUT = 500;

    private JAXBContext jaxb;

    public OpenWeatherMapService() {
        try {
            this.jaxb = JAXBContext.newInstance(OpenWeatherMapResponse.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public OpenWeatherMapResponse query() throws IOException {
        URL remote = new URL(buildUrl(API_KEY,
                                      targetLatitude(),
                                      targetLongitude()
                             ));
        HttpURLConnection connection = (HttpURLConnection) remote.openConnection();
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        try (InputStream responseStream = connection.getInputStream()) {
            StreamSource responseSource = new StreamSource(responseStream);
            Unmarshaller unmarshaller = this.jaxb.createUnmarshaller();
            JAXBElement<OpenWeatherMapResponse> responseParsed = unmarshaller.unmarshal(responseSource,
                                                                                        OpenWeatherMapResponse.class);
            return (OpenWeatherMapResponse) JAXBIntrospector.getValue(responseParsed);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    private String buildUrl(String apiKey, double longitude, double latitude) {
        return new StringBuilder().append(SERVICE_URL)
                                  .append("?lat=").append(latitude)
                                  .append("&lon=").append(longitude)
                                  .append("&mode=xml")
                                  .append("&units=metric")
                                  .append("&cnt=2")
                                  .append("&appid=").append(apiKey).toString();
    }

    //these would be other services this depends on

    private static double targetLongitude() {
        return 49.1973419;
    }

    private static double targetLatitude() {
        return 16.6050103;
    }
}
