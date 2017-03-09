
package pv260.solid.dip.solution;

import java.io.IOException;
import pv260.solid.dip.solution.model.WeatherReport;

public interface WeatherService {

    WeatherReport query() throws IOException;

}
