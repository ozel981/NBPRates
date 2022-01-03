package app.repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

public class Repository {
    Repository(String uri) {
        this.uri = uri;
        client = HttpClient.newHttpClient();
    }

    String uri = "http://api.nbp.pl/api";
    HttpClient client;

    public void getExchangeRate(String echangeCode, Date start, Date end) throws Exception {
        get(uri + "/exchangerates/rates/A/" + echangeCode + "/" + start + "/" + end);
    }

    public String get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

}
