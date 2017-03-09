
package pv260.solid.srp.problems;

import java.io.IOException;
import static java.lang.String.format;
import java.net.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class PotentialAttack {

    public static void main(String[] args) throws MalformedURLException, IOException {


        String fakeOrder = "user=999&cost=0";
        String request = format("POST / HTTP/1.1\n"
                + "Host: localhost:8080\n"
                + "Accept: */*\n"
                + "Content-Length: %s\n"
                + "\n"
                + "%s",
                                fakeOrder.getBytes(UTF_8).length,
                                fakeOrder);
        System.out.println("send \n"+request);

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 8080));
        socket.getOutputStream().write(request.getBytes(UTF_8));
        socket.getOutputStream().flush();
        socket.close();

    }

}
