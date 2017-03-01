package pv260.solid.lsp.original;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class JSONSerializer implements SimpleSerializer {

    private static final char TYPE_SEPARATOR = '@';

    private final Gson jsonSerializer;

    public JSONSerializer() {
        this.jsonSerializer = new GsonBuilder().create();
    }

    @Override
    public void serializeInto(Object instance, OutputStream into) throws IOException {
        into.write(typeLiteral(instance.getClass()).getBytes(StandardCharsets.UTF_8));
        into.write(this.jsonSerializer.toJson(instance).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Object deserializeFrom(InputStream from) throws IOException {
        Reader reader = new InputStreamReader(from);
        return this.jsonSerializer.fromJson(reader, readType(reader));
    }

    @Override
    public String toString() {
        return "JSON";
    }

    private static String typeLiteral(Class<?> type) {
        return type.getName() + TYPE_SEPARATOR;
    }

    private static Class<?> readType(Reader from) throws IOException {
        StringBuilder type = new StringBuilder();
        while (true) {
            int codepoint = from.read();
            if (codepoint == TYPE_SEPARATOR) {
                break;
            }
            type.appendCodePoint(codepoint);
        }
        try {
            return Class.forName(type.toString());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
