package pv260.solid.ocp.solution;

import java.nio.file.Paths;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        Comment comment = new Comment("My comment",
                                      "This is interesting...",
                                      new Date(),
                                      "Pepa Zdepa");
        try {
            new CsvPersistence(Paths.get("comments.csv")).persist(comment);
            new XmlPersistence(Paths.get("comments.xml")).persist(comment);
        } catch (PersistenceException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
