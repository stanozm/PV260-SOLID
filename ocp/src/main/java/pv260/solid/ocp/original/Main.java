package pv260.solid.ocp.original;

import java.nio.file.Paths;
import java.util.Date;
import static pv260.solid.ocp.original.PersistenceType.CSV;
import static pv260.solid.ocp.original.PersistenceType.XML;

public class Main {

    public static void main(String[] args) {
        Comment comment = new Comment("My comment",
                                      "This is interesting...",
                                      new Date(),
                                      "Pepa Zdepa");
        new Persistence(XML,
                        null,
                        Paths.get("comments.xml")).persist(comment);
        new Persistence(CSV,
                        Paths.get("comments.csv"),
                        null).persist(comment);
    }
}
