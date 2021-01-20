package com.github.niko247;

import lombok.extern.log4j.Log4j2;

import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

@Log4j2
public class ReportMessageCreator {
    private SummaryResults cases;
    private SummaryResults casesOld;

    public Optional<String> createIfNewCases(SummaryResults cases, SummaryResults casesOld) {
        this.cases = cases;
        this.casesOld = casesOld;

        logCurrentCases();

        var totalMessage = createDifferenceReport(this::countCasesNumber, "Całkowita liczba");
        var deathsMessage = createDifferenceReport(this::countDeathsNumber, "Zmarłych");

        if (totalMessage.isPresent() || deathsMessage.isPresent()) {
            return Stream.of(totalMessage, deathsMessage).filter(Optional::isPresent).map(Optional::get).
                    reduce((a, b) -> a + " " + b);
        }
        return Optional.empty();
    }

    private void logCurrentCases() {
        var currentCases = countCasesNumber(cases);
        log.info("Checking if generate report for total cases:" + currentCases);
    }

    private Optional<String> createDifferenceReport(ToIntFunction<SummaryResults> counter, String messagePrefix) {
        var currentCounterResult = counter.applyAsInt(cases);
        var oldCounterResult = counter.applyAsInt(casesOld);

        var difference = currentCounterResult - oldCounterResult;
        if (difference != 0) {
            return Optional.of(String.format("%s: %,d (%+,d).", messagePrefix, currentCounterResult, difference));
        }
        return Optional.empty();
    }

    private int countCasesNumber(SummaryResults cases) {
        return cases.getTotalCases();
    }

    private int countDeathsNumber(SummaryResults cases) {
        return cases.getTotalDeaths();
    }


}
