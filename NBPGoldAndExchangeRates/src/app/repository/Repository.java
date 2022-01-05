package app.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    public Repository(String uri) {
        this.uri = uri;
        client = HttpClient.newHttpClient();
    }

    String uri;
    HttpClient client;

    public List<Double> getExchangeRate(String echangeCode, String start, String end) throws Exception {
        List<Double> midrates = new ArrayList<Double>();
        String value = get(uri + "/exchangerates/rates/A/" + echangeCode + "/" + start + "/" + end);
        JsonObject exchange = JsonParser.parseString(value).getAsJsonObject();
        JsonArray rates = exchange.get("rates").getAsJsonArray();
        for(JsonElement rate : rates) {
            midrates.add(rate.getAsJsonObject().get("mid").getAsDouble());
        }
        return midrates;
    }

    public List<String> getExchanges() throws Exception {
        String value = get(uri + "/exchangerates/tables/A");
        JsonObject exchangeTable = JsonParser.parseString(value).getAsJsonArray().get(0).getAsJsonObject();
        List<String> exchangeCodes = new ArrayList<String>();
        JsonArray rates = exchangeTable.get("rates").getAsJsonArray();
        for(JsonElement rate : rates) {
            exchangeCodes.add(rate.getAsJsonObject().get("code").getAsString());
        }
        return exchangeCodes;
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
