package com.github.niko247;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CoronaResultsFetcher {
    private static final String URL = "https://services-eu1.arcgis.com/zk7YlClTgerl62BY/arcgis/rest/services/global_corona_widok2/FeatureServer/0/query?f=json&where=Data%20BETWEEN%20(CURRENT_TIMESTAMP%20-%20INTERVAL%20%2724%27%20HOUR)%20AND%20CURRENT_TIMESTAMP&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&resultOffset=0&resultRecordCount=1&resultType=standard";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";

    public SummaryResults fetchCases() throws IOException {
        var response = requestCoronavirusPage();
        return extractSummaryData(response);
    }

    private SummaryResults extractSummaryData(Connection.Response response) throws IOException {
        Document document = response.parse();

        var attrs = new ObjectMapper().readTree(document.body().text()).get("features").get(0).get("attributes");

        var cases = attrs.get("LICZBA_ZAKAZEN").asInt();
        var deaths =attrs.get("LICZBA_ZGONOW").asInt();

        return new SummaryResults(cases, deaths);
    }

    private Connection.Response requestCoronavirusPage() throws IOException {
        return Jsoup.connect(URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET).userAgent(USER_AGENT)
                .execute();
    }
}
