/**
 * @author lxq
 * @create 2019/8/10 0:09
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * This example demonstrates a fully asynchronous execution of multiple HTTP exchanges
 * where the result of an individual operation is reported using a callback interface.
 */
@Slf4j
public class AsyncClientHttpExchangeFutureCallback {

    public static void main(final String[] args) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000).build();
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        try {
            httpclient.start();
            final HttpGet[] requests = new HttpGet[]{
                    new HttpGet("http://httpbin.org/ip"),
                    new HttpGet("https://httpbin.org/ip"),
                    new HttpGet("http://httpbin.org/headers")
            };
            final CountDownLatch latch = new CountDownLatch(requests.length);
            log.info("start at " + new Date());
            for (final HttpGet request : requests) {
                httpclient.execute(request, new FutureCallback<HttpResponse>() {

                    @Override
                    public void completed(final HttpResponse response) {
                        latch.countDown();
                        log.info(request.getRequestLine() + "->" + response.getStatusLine());
                    }

                    @Override
                    public void failed(final Exception ex) {
                        latch.countDown();
                        log.info(request.getRequestLine() + "->" + ex);
                    }

                    @Override
                    public void cancelled() {
                        latch.countDown();
                        log.info(request.getRequestLine() + " cancelled");
                    }

                });
            }
            Thread.sleep(5000);
            latch.await();
            log.info("Shutting down");
        } finally {
            httpclient.close();
        }
        log.info("Done");
    }

}
