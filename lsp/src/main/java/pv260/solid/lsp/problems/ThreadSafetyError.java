package pv260.solid.lsp.problems;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.concurrent.CountDownLatch;
import pv260.solid.lsp.original.CSVSerializer;

public class ThreadSafetyError {

    public static void main(String[] args) throws IOException, InterruptedException {

        CSVSerializer serializer = new CSVSerializer();
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();

        CountDownLatch wait = new CountDownLatch(1);

        int[] vals1 = new int[100];
        for (int i = 0; i < 100; i++) {
            vals1[i] = i;
        }
        Value val1 = new Value(vals1);

        Thread serThread1 = new Thread(() -> {
            try {
                wait.await();
                serializer.serializeInto(val1, out1);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        int[] vals2 = new int[100];
        for (int i = 0; i < 100; i++) {
            vals2[i] = i+100;
        }
        Value val2 = new Value(vals2);

        Thread serThread2 = new Thread(() -> {
            try {
                wait.await();
                serializer.serializeInto(val2, out2);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        serThread1.start();
        serThread2.start();
        wait.countDown();

        serThread1.join();
        serThread2.join();

        System.out.println("1: " + new String(out1.toByteArray(), UTF_8));
        System.out.println("2: " + new String(out2.toByteArray(), UTF_8));

    }

    public static class Value {

        private final int[] vals;

        public Value(int[] vals) {
            this.vals = vals;
        }

    }

}
