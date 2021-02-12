package com.github.niko247;

import com.google.gson.JsonParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CoronaResultsFetcher {
    private final static String URL = "https://services-eu1.arcgis.com/zk7YlClTgerl62BY/arcgis/rest/services/global_corona_widok2/FeatureServer/0/query?f=json&where=Data%20BETWEEN%20(CURRENT_TIMESTAMP%20-%20INTERVAL%20%2724%27%20HOUR)%20AND%20CURRENT_TIMESTAMP&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&resultOffset=0&resultRecordCount=1&resultType=standard";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";

    public SummaryResults fetchCases() throws IOException {
        var response = requestCoronavirusPage();
        return extractSummaryData(response);
    }

    private SummaryResults extractSummaryData(Connection.Response response) throws IOException {
        Document document = response.parse();
        var summaryResults = new SummaryResults();
        var attrs = JsonParser.parseString(document.body().text()).getAsJsonObject().get("features").
                getAsJsonArray().get(0).getAsJsonObject().get("attributes").getAsJsonObject();

        var cases = Integer.parseInt(attrs.get("LICZBA_ZAKAZEN").getAsString());
        var deaths = Integer.parseInt(attrs.get("LICZBA_ZGONOW").getAsString());

        summaryResults.setTotalCases(cases);
        summaryResults.setTotalDeaths(deaths);
        return summaryResults;
    }

    private int parseNumber(Elements casesAndDeaths, int index) {
        return Integer.parseInt(casesAndDeaths.get(index).text().replace(" ", ""));
    }

    private Connection.Response requestCoronavirusPage() throws IOException {
        return Jsoup.connect(URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET).userAgent(USER_AGENT)
                .execute();
    }
}
