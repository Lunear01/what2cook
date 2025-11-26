package recipeapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;
import java.io.IOException;

/**
 * Service responsible for performing HTTP GET requests.
 * Minimal, reuses client's lifecycle as your original code does.
 */
public class HttpService {
    private static final OkHttpClient CLIENT = new OkHttpClient();

    /**
     * Sends a GET request to the specified URL.
     * @param url the HttpUrl to send the request to
     * @return response body as a String
     * @throws IOException if the request was unsuccessful
     */
    public String get(HttpUrl url) throws IOException {
        final Request request = new Request.Builder().url(url).build();
        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP error " + response.code() + ": " + response.message());
            }
            return response.body().string();
        }
    }
}
