package com.github.niko247;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoronaResultsFetcher {
    private final static String URL = "https://www.gov.pl/web/koronawirus/wykaz-zarazen-koronawirusem-sars-cov-2";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
    private static final String CORONAVIRUS_DATA_ID = "registerData";
    private static final Type LIST_TYPE = new TypeToken<ArrayList<CoronaCase>>() {
    }.getType();
    private static final String TOTAL_CASES_VOIVODESHIP = "Cała Polska";

    public List<CoronaCase> fetchCases() throws IOException {
        var response = requestCoronavirusPage();
        var text = extractRawData(response);
        var coronaCases = convertToParsed(text);
        coronaCases.removeIf(c -> TOTAL_CASES_VOIVODESHIP.equals(c.getVoivodeship()));
        return coronaCases;
    }

    private List<CoronaCase> convertToParsed(String rawDataText) {
        var coronaResults = new Gson().fromJson(rawDataText, CoronaResults.class);
        return new Gson().fromJson(coronaResults.getParsedData(), LIST_TYPE);
    }

    private String extractRawData(Connection.Response response) throws IOException {
        var coronavirusData = response.parse().getElementById(CORONAVIRUS_DATA_ID);
        return coronavirusData.text();
    }

    private Connection.Response requestCoronavirusPage() throws IOException {
        return Jsoup.connect(URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET).userAgent(USER_AGENT)
                .execute();
    }
}
