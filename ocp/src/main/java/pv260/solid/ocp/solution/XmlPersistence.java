package pv260.solid.ocp.solution;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import static org.jdom2.output.Format.getPrettyFormat;
import org.jdom2.output.XMLOutputter;

public class XmlPersistence implements Persistence {

    private Path xmlFile;

    public XmlPersistence(Path xmlFile) {
        this.xmlFile = xmlFile;
    }

    @Override
    public void persist(Comment comment) throws PersistenceException {
        try {
            Document currentDoc = getCurrentDoc();
            appendComment(comment,
                          currentDoc);
            writeXmlToFile(currentDoc,
                           xmlFile);
        } catch (IOException | JDOMException e) {
            throw new PersistenceException(e);
        }
    }

    private Document getCurrentDoc() throws IOException,
                                    JDOMException {
        if (!Files.exists(xmlFile)) {
            Document doc = new Document(new Element("comments"));
            writeXmlToFile(doc,
                           xmlFile);
            return doc;
        }
        return new SAXBuilder().build(xmlFile.toFile());
    }

    private void appendComment(Comment commentData,
                               Document doc) {
        Element commentElement = new Element("comment");
        commentElement.addContent(new Element("author").setText(commentData.getAuthor()));
        commentElement.addContent(new Element("entered").setText(String.valueOf(commentData.getEntered()
                                                                                           .getTime())));
        commentElement.addContent(new Element("headline").setText(commentData.getHeadline()));
        commentElement.addContent(new Element("text").setText(commentData.getText()));
        doc.getRootElement()
           .addContent(commentElement);
    }

    private void writeXmlToFile(Document doc,
                                Path file) throws IOException {
        XMLOutputter xmlOut = new XMLOutputter();
        xmlOut.setFormat(getPrettyFormat());
        try (Writer w = Files.newBufferedWriter(file)) {
            xmlOut.output(doc,
                          w);
        }
    }
}
