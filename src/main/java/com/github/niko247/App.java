package com.github.niko247;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
public class App {
    private final static long REFRESH_MINUTES = 15;

    private final PushManager push;
    private final CoronaResultsFetcher casesFetcher;
    private final OldCasesManager oldCasesManager;

    public static void main(String[] args) {
        new App().main();
    }

    public void main() {
        var loopNr = 1;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                log.info("Fetching data in loop:" + loopNr++);
                fetchPageAndReport();
                Thread.sleep(TimeUnit.MINUTES.toMillis(REFRESH_MINUTES));
            } catch (IOException e) {
                log.error(e);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public App() {
        this(new PushManager(), new CoronaResultsFetcher(), new OldCasesManager());
    }

    public App(PushManager pushManager, CoronaResultsFetcher casesFetcher, OldCasesManager oldCasesManager) {
        this.push = pushManager;
        this.casesFetcher = casesFetcher;
        this.oldCasesManager = oldCasesManager;
    }

    public void fetchPageAndReport() throws  IOException {
        final List<CoronaCase> currentCases = casesFetcher.fetchCases();
        generateReport(currentCases);

    }

    private void generateReport(List<CoronaCase> cases) throws IOException {
        var casesOld = oldCasesManager.get().orElse(cases);
        compareAndSaveIfChanged(cases, casesOld);
    }


    private void compareAndSaveIfChanged(List<CoronaCase> cases, List<CoronaCase> casesOld) throws IOException {
        var currentCases = countCasesNumber(cases);
        log.info("Wszystkie obecne przypadki:" + currentCases);
        var oldCases = countCasesNumber(casesOld);
        final var newCasesDetected = currentCases != oldCases;
        if (newCasesDetected) {
            var newCases = CollectionUtils.subtract(cases, casesOld);
            var message =
                    String.format("Nowe przypadki, całkowita ilość %s (+%d). Lokacje:%s",
                            currentCases,
                            currentCases - oldCases,
                            newCases.stream().map(CoronaCase::getCounty).
                                    collect(Collectors.joining(", ")));
            log.info(message);
            reportServices(message);
        }
        oldCasesManager.save(cases);
    }

    private void reportServices(String message) {
        push.send(message);
    }

    private int countCasesNumber(List<CoronaCase> cases) {
        return cases.stream().mapToInt(CoronaCase::getCasesNumber).sum();
    }

}
