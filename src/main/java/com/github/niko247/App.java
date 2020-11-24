package com.github.niko247;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class App {
    private static final long REFRESH_MINUTES = 15;

    private final Collection<MessageSender> messageSenders;
    private final CoronaResultsFetcher casesFetcher;
    private final OldCasesManager oldCasesManager;
    private final ReportMessageCreator reportMessageCreator;

    public App() {
        this(Collections.singletonList(new TelegramSender()), new CoronaResultsFetcher(),
                new OldCasesManager(), new ReportMessageCreator());

    }

    public App(Collection<MessageSender> messageSenders, CoronaResultsFetcher casesFetcher, OldCasesManager oldCasesManager,
               ReportMessageCreator reportMessageCreator) {
        this.messageSenders = messageSenders;
        this.casesFetcher = casesFetcher;
        this.oldCasesManager = oldCasesManager;
        this.reportMessageCreator = reportMessageCreator;

        var outputFolder = Optional.ofNullable(System.getenv("OUTPUT_FOLDER")).orElse("./");
        var outputPath = Path.of(outputFolder).resolve("corona_temp.json");

        this.oldCasesManager.setPath(outputPath);
    }

    public static void main(String[] args) {
        new App().main();
    }

    public void main() {
        var loopNr = new AtomicInteger(1);
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            log.info("Fetching data in loop:" + loopNr.getAndIncrement());
            try {
                fetchPageAndReport();
            } catch (Exception e) {
                log.error(e);
            }
        }, 0, REFRESH_MINUTES, TimeUnit.MINUTES);
    }

    public void fetchPageAndReport() throws IOException {
        var currentCases = casesFetcher.fetchCases();
        reportAndSave(currentCases);

    }

    private void reportAndSave(SummaryResults cases) throws IOException {
        var casesOld = oldCasesManager.get().orElse(cases);
        reportMessageCreator.createIfNewCases(cases, casesOld).ifPresent(message -> {
                    log.info(message);
                    reportServices(message);
                }
        );
        oldCasesManager.save(cases);
    }


    private void reportServices(String message) {
        messageSenders.forEach(ms -> ms.send(message));
    }


}
