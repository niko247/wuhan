package com.github.niko24;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
public class App {
    private final static long REFRESH_MINUTES = 15;
    private final static String URL = "https://www.gov.pl/web/koronawirus/wykaz-zarazen-koronawirusem-sars-cov-2";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
    private static final String CORONAVIRUS_DATA_ID = "registerData";
    private static final String TEMP_FILE = "corona_temp.json";
    private static final PushProvider push = new PushProvider();

    public static void main(String[] args) {
        var loopNr = 1;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                log.info("Fetching data in loop:" + loopNr++);
                final List<CoronaCase> cases = fetchCases();
                generateReport(cases);
                Thread.sleep(TimeUnit.MINUTES.toMillis(REFRESH_MINUTES));
            } catch (IOException e) {
                log.error(e);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static List<CoronaCase> fetchCases() throws IOException {
        final Connection.Response re = requestCoronavirusPage();

        final Element coronavirusData = re.parse().getElementById(CORONAVIRUS_DATA_ID);
        final String text = coronavirusData.text();
        final CoronaResults coronaResults = new Gson().fromJson(text, CoronaResults.class);
        Type listType = new TypeToken<ArrayList<CoronaCase>>() {
        }.getType();
        return new Gson().fromJson(coronaResults.getParsedData(), listType);
    }

    private static Connection.Response requestCoronavirusPage() throws IOException {
        return Jsoup.connect(URL)
                .ignoreContentType(true)
                .method(Connection.Method.GET).userAgent(USER_AGENT)
                .execute();
    }

    private static void generateReport(List<CoronaCase> cases) throws IOException {
        var casesOld = getOldCases(cases);
        compareAndSaveIfChanged(cases, casesOld);
    }

    private static List<CoronaCase> getOldCases(List<CoronaCase> cases) throws IOException {
        final var tmpFile = Path.of(TEMP_FILE);
        List<CoronaCase> casesOld;
        if (Files.exists(tmpFile)) {
            Type listType = new TypeToken<ArrayList<CoronaCase>>() {
            }.getType();
            final var jsonContent = Files.readString(tmpFile);
            casesOld = new Gson().fromJson(jsonContent, listType);
        } else {
            casesOld = cases;
        }
        return casesOld;
    }

    private static void saveCurrentResult(List<CoronaCase> cases, Path tmpFile) throws IOException {
        final var jsonContent = new Gson().toJson(cases);
        Files.writeString(tmpFile, jsonContent);
    }

    private static void compareAndSaveIfChanged(List<CoronaCase> cases, List<CoronaCase> casesOld) throws IOException {
        final var currentCases = countCasesNumber(cases);
        log.info("Wszystkie obecne przypadki:" + currentCases);
        final var oldCases = countCasesNumber(casesOld);
        if (currentCases != oldCases) {
            final var tmpFile = Path.of(TEMP_FILE);
            saveCurrentResult(cases, tmpFile);
            final var newCases = CollectionUtils.subtract(cases, casesOld);
            final var message =
                    String.format("Nowy przypadek wykryty, całkowita ilość przypadków %s Nowe przypadki (+%i): %s",
                            currentCases,
                            currentCases - oldCases,
                            newCases.stream().map(c -> "Powiat/Miasto: " + c.getCounty()).
                                    collect(Collectors.joining(", ")));
            log.info(message);
            reportServices(message);
        }
    }

    private static void reportServices(String message) {
        push.send(message);
    }

    private static int countCasesNumber(List<CoronaCase> cases) {
        return cases.stream().mapToInt(CoronaCase::getCasesNumber).sum();
    }

}
