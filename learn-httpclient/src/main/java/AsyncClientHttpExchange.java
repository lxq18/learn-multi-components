import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.Future;

/**
 * @author lxq
 * @create 2019/8/9 23:20
 */
@Slf4j
public class AsyncClientHttpExchange {
    public static void main(final String[] args) throws Exception {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            HttpGet request = new HttpGet("http://httpbin.org/get");
            Future<HttpResponse> future = httpclient.execute(request, null);
            HttpResponse response = future.get();
            log.info("Response: " + response.getStatusLine());
            String str = EntityUtils.toString(response.getEntity());
            log.info(str);
            log.info("Shutting down");
        } finally {
            httpclient.close();
        }
        log.info("Done");
    }
}
