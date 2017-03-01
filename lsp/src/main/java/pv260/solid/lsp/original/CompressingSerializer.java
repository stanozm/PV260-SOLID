package pv260.solid.lsp.original;

import java.io.*;
import java.util.zip.*;

/**
 * Wrapper which adds compression to any SimpleSerializer
 * <p>
 * For maximum space efficiency the serialized form is transformed to ASCII before compression
 * <p>
 * NOTE: It is only beneficial to use this wrapper for large objects
 *       as the compression ratio grows with size of input
 */
public class CompressingSerializer implements SimpleSerializer {

    private final SimpleSerializer delegate;

    /**
     * @param delegate which will handle the serialization itself, its output will be compressed
     */
    public CompressingSerializer(SimpleSerializer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void serializeInto(Object instance, OutputStream into) throws IOException {
        GZIPOutputStream compressedAsciiOutput = new GZIPOutputStream(into);
        OutputStream asciiOutput = new Utf8ToAsciiWriter(compressedAsciiOutput);
        this.delegate.serializeInto(instance, asciiOutput);
        compressedAsciiOutput.finish();
    }

    @Override
    public Object deserializeFrom(InputStream from) throws IOException {
        GZIPInputStream decompressedInput = new GZIPInputStream(from);
        return this.delegate.deserializeFrom(decompressedInput);
    }

    @Override
    public String toString() {
        return this.delegate.toString()+" with added compression";
    }



    private static class Utf8ToAsciiWriter extends OutputStream {

        private static final int IS_ASCII = 0b1000_0000;

        private static final int FIRST_OF_MULTIBYTE = 0b1100_0000;

        private static final int UNKNOWN_CHAR = '?';

        private final OutputStream delegate;

        public Utf8ToAsciiWriter(OutputStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public void write(int b) throws IOException {
            if ((b & IS_ASCII) == 0) {
                this.delegate.write(b);
            } else if ((b & FIRST_OF_MULTIBYTE) == FIRST_OF_MULTIBYTE) {
                this.delegate.write(UNKNOWN_CHAR);
            }
        }

        @Override
        public void flush() throws IOException {
            this.delegate.flush();
        }

        @Override
        public void close() throws IOException {
            this.delegate.close();
        }
    }
}
