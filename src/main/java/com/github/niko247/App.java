package com.github.niko247;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
public class App {
    private final static long REFRESH_MINUTES = 15;

    private final PushManager push;
    private final CoronaResultsFetcher casesFetcher;
    private final OldCasesManager oldCasesManager;
    private final ReportMessageCreator reportMessageCreator;

    public App() {
        this(new PushManager(), new CoronaResultsFetcher(), new OldCasesManager(), new ReportMessageCreator());
    }

    public App(PushManager pushManager, CoronaResultsFetcher casesFetcher, OldCasesManager oldCasesManager,
               ReportMessageCreator reportMessageCreator) {
        this.push = pushManager;
        this.casesFetcher = casesFetcher;
        this.oldCasesManager = oldCasesManager;
        this.reportMessageCreator = reportMessageCreator;
    }

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

    public void fetchPageAndReport() throws IOException {
        final List<CoronaCase> currentCases = casesFetcher.fetchCases();
        reportAndSave(currentCases);

    }

    private void reportAndSave(List<CoronaCase> cases) throws IOException {
        var casesOld = oldCasesManager.get().orElse(cases);
        reportMessageCreator.createIfNewCases(cases, casesOld).ifPresent(message -> {
                    log.info(message);
                    reportServices(message);
                }
        );
        oldCasesManager.save(cases);
    }


    private void reportServices(String message) {
        push.send(message);
    }


}
