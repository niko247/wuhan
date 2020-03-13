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

    public List<CoronaCase> fetchCases() throws IOException {
        var response = requestCoronavirusPage();
        var coronavirusData = response.parse().getElementById(CORONAVIRUS_DATA_ID);
        var text = coronavirusData.text();
        final CoronaResults coronaResults = new Gson().fromJson(text, CoronaResults.class);
        Type listType = new TypeToken<ArrayList<CoronaCase>>() {
        }.getType();
        return new Gson().fromJson(coronaResults.getParsedData(), listType);
    }

    private Connection.Response requestCoronavirusPage() throws IOException {
        return Jsoup.connect(URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET).userAgent(USER_AGENT)
                .execute();
    }
}
