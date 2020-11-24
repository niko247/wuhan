package com.github.niko247;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoronaResultsFetcher {
    private final static String URL = "https://www.gov.pl/web/koronawirus/wykaz-zarazen-koronawirusem-sars-cov-2";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";

    public SummaryResults fetchCases() throws IOException {
        var response = requestCoronavirusPage();
        return extractSummaryData(response);
    }

    private SummaryResults extractSummaryData(Connection.Response response) throws IOException {
        Document document = response.parse();
        var summaryResults = new SummaryResults();
        var casesAndDeaths = document.getElementsMatchingText("Statystyki - Polska").last().
                nextElementSiblings().first().getElementsByTag("dd");
        var cases = parseNumber(casesAndDeaths, 0);
        var deaths = parseNumber(casesAndDeaths, 1);

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
