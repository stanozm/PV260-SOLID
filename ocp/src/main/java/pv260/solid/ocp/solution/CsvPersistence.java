package pv260.solid.ocp.solution;

import java.io.BufferedWriter;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class CsvPersistence implements Persistence {

    private Path csvFile;

    public CsvPersistence(Path csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public void persist(Comment comment) throws PersistenceException {
        try (BufferedWriter writer = Files.newBufferedWriter(csvFile,
                                                             UTF_8,
                                                             CREATE,
                                                             APPEND,
                                                             WRITE)) {
            writer.append(formatCsv(comment));
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    private String formatCsv(Comment comment) {
        return comment.getAuthor() + ", " + comment.getEntered() + ", " + comment.getHeadline() + ", "
                + comment.getText() + System.lineSeparator();
    }
}
