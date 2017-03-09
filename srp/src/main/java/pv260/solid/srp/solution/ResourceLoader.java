package pv260.solid.srp.solution;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ResourceLoader {

    public String loadResource(Class<?> searchRoot,
                               String name) throws IOException {
        try (InputStream in = searchRoot.getResourceAsStream(name); Scanner scan = new Scanner(in).useDelimiter("\\A")) {
            String resource = scan.next();
            return resource;
        }
    }

}
