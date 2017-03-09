package pv260.solid.srp.solution;

import pv260.solid.srp.original.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Main {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(OrderServlet.class,
                                      "/");
        handler.addServletWithMapping(ConfirmationServlet.class,
                                      "/confirm");

        server.start();
        server.join();
    }
}
