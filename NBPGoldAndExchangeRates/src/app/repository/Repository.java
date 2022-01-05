package app.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Repository {
    public Repository(String uri) {
        this.uri = uri;
        client = HttpClient.newHttpClient();
    }

    String uri = "http://api.nbp.pl/api";
    HttpClient client;

    public void getExchangeRate(String echangeCode, Date start, Date end) throws Exception {
        get(uri + "/exchangerates/rates/A/" + echangeCode + "/" + start + "/" + end);
    }

    public List<String> getExchanges() throws Exception {
        String value = get(uri + "/exchangerates/tables/A");
        System.out.print(value);
        JsonObject exchangeTable = JsonParser.parseString(value).getAsJsonArray().get(0).getAsJsonObject();
        List<String> exchangeCodes = new ArrayList<String>();
        JsonArray sss = exchangeTable.get("rates").getAsJsonArray();
        for(var s : sss) {
            exchangeCodes.add(s.getAsJsonObject().get("code").getAsString());
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
