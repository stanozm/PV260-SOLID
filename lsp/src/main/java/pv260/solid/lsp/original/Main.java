package pv260.solid.lsp.original;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import java.util.List;
import pv260.solid.lsp.original.TestObjectsBag.BitmapPicture;
import pv260.solid.lsp.original.TestObjectsBag.Friend;

public class Main {

    public static void main(String[] args) throws Exception {
        SimpleSerializer json = new JSONSerializer();
        SimpleSerializer csv = new CSVSerializer();
        SimpleSerializer compressedJson = new CompressingSerializer(new JSONSerializer());
        SimpleSerializer compressedCsv = new CompressingSerializer(new CSVSerializer());

        List<Object> objectsToSerialize = asList(new Friend("Lemon and Cherry",
                                                            13_17_8,
                                                            new String[]{"English",
                                                                         "Czech",
                                                                         "漢語",
                                                                         "Türkçe"}),
                                                 new BitmapPicture("Mona Lisa",
                                                                   new char[][]{{'o', '-', '-', 'o'},
                                                                                {'|', 'o', 'O', '|'},
                                                                                {'|', '/', '\\', '|'},
                                                                                {'o', '-', '-', 'o'}}));

        for (SimpleSerializer serializer : asList(json, csv, compressedJson, compressedCsv)) {
            for (Object serializedObject : objectsToSerialize) {
                demonstrateSerialization(serializer, serializedObject);
            }
        }
    }

    private static void demonstrateSerialization(final SimpleSerializer serializer, final Object objectToSerializer) throws IOException {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println(format("Serializer:%s",
                                  serializer));
        System.out.println(format("Object:\n%s",
                                  objectToSerializer));
        try {
            System.out.println("Serialized form:");
            serializer.serializeInto(objectToSerializer, System.out);
            System.out.println();
        } catch (Exception e) {
            System.out.println(format("Error when serializing:\n%s",
                                      e));
        }
        try {
            PipedInputStream pipeIn = new PipedInputStream();
            PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
            serializer.serializeInto(objectToSerializer, pipeOut);
            pipeOut.flush();
            pipeOut.close();
            System.out.println(format("Deserialized back:\n%s",
                                      serializer.deserializeFrom(pipeIn)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(format("Error when deserializing:\n%s",
                                      e));
        }
    }
}
