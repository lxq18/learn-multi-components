/**
 * @author lxq
 * @create 2019/8/9 23:31
 */
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;

/**
 * This example demonstrates an asynchronous HTTP request / response exchange with
 * a full content streaming.
 */
@Slf4j
public class AsyncClientHttpExchangeStreaming {

    public static void main(final String[] args) throws Exception {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            Future<Boolean> future = httpclient.execute(
                    HttpAsyncMethods.createGet("http://httpbin.org/"),
                    new MyResponseConsumer(), null);
            Boolean result = future.get();
            if (result != null && result.booleanValue()) {
                log.info("Request successfully executed");
            } else {
                log.info("Request failed");
            }
            log.info("Shutting down");
        } finally {
            httpclient.close();
        }
        log.info("Done");
    }

    static class MyResponseConsumer extends AsyncCharConsumer<Boolean> {

        @Override
        protected void onResponseReceived(final HttpResponse response) {
            log.info("onResponseReceived");
        }

        @Override
        protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
            log.info("onCharReceived");
            while (buf.hasRemaining()) {
                System.out.print(buf.get());
            }
        }

        @Override
        protected void releaseResources() {
            log.info("releaseResources");
        }

        @Override
        protected Boolean buildResult(final HttpContext context) {
            return Boolean.TRUE;
        }

    }
}
