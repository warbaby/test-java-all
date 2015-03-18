package jmh;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;
import org.openjdk.jmh.annotations.*;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutionException;

/**
 * @author WangBei
 * @since 2015/3/17
 */
@Fork(2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JMH_HttpClient {

	public static final String HTTP_WWW_BAIDU_COM = "http://www.baidu.com";

	@Benchmark
	public void measureJavaURLConn() throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(HTTP_WWW_BAIDU_COM).openConnection();
		conn.getResponseCode();
		conn.disconnect();
	}

	@Benchmark
	public int measureHttpClient() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(HTTP_WWW_BAIDU_COM);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		response.close();
		return statusCode;
	}

	@State(Scope.Benchmark)
	public static class HttpClientHolderState {
		RequestConfig config;
		CloseableHttpClient httpClient;

		@Setup
		public synchronized void setup() {
			config = RequestConfig.custom().setConnectTimeout(20000).setSocketTimeout(10000).build();
			httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		}

		@TearDown
		public synchronized void tearDown() throws IOException {
			config = null;
			httpClient.close();
		}
	}

	@Benchmark
	public int measureHttpClientSingle(HttpClientHolderState state) throws IOException {
		HttpGet httpGet = new HttpGet(HTTP_WWW_BAIDU_COM);
		CloseableHttpResponse response = state.httpClient.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		response.close();
		return statusCode;
	}

	@State(Scope.Benchmark)
	public static class PooledHttpClientHolderState {

		CloseableHttpClient httpClient;

		@Setup
		public synchronized void setup() {
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			// Increase max total connection to 200
			cm.setMaxTotal(20);
			// Increase default max connection per route to 20
			cm.setDefaultMaxPerRoute(20);
			// Increase max connections for www.baidu.com:80 to 50
			//HttpHost localhost = new HttpHost("www.baidu.com", 80);
			//cm.setMaxPerRoute(new HttpRoute(localhost), 50);

			httpClient = HttpClients.custom()
			        .setConnectionManager(cm)
			        .build();
		}

		@TearDown
		public synchronized void tearDown() throws IOException {
			httpClient.close();
		}
	}

	@Benchmark
	public int measurePooledHttpClient(PooledHttpClientHolderState state) throws IOException {
		HttpGet httpGet = new HttpGet(HTTP_WWW_BAIDU_COM);
		CloseableHttpResponse response = state.httpClient.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		response.close();
		return statusCode;
	}

	@State(Scope.Benchmark)
	public static class AsyncHttpClientState {
		AsyncHttpClient client;
		@Setup
		public synchronized void setup() {
			client = new AsyncHttpClient();
		}
		@TearDown
		public synchronized void tearDown() {
			client.close();
		}
	}

	@Benchmark
	public int measureAsyncHttpClient(AsyncHttpClientState state) throws ExecutionException, InterruptedException {
		AsyncHttpClient client = state.client;
		ListenableFuture<Response> future = client.prepareGet(HTTP_WWW_BAIDU_COM).execute();
		return future.get().getStatusCode();
	}

	/*
	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			  .include(JMH_HttpClient.class.getSimpleName())
			  .forks(3)
			  .warmupIterations(3)
			  .measurementIterations(5)
			  .build();

		new Runner(opt).run();
	}
	*/
}
