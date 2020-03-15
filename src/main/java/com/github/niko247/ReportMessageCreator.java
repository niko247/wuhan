package com.github.niko247;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class ReportMessageCreator {

    public Optional<String> createIfNewCases(List<CoronaCase> cases, List<CoronaCase> casesOld) {
        var currentCases = countCasesNumber(cases);
        log.info("Checking if generate report for total cases:" + currentCases);
        var oldCases = countCasesNumber(casesOld);
        var newCasesDetected = currentCases != oldCases;
        if (newCasesDetected) {
            var newCases = CollectionUtils.subtract(cases, casesOld);
            var difference = currentCases - oldCases;
            return Optional.of(
                    String.format("Całkowita ilość %s (%+d). Lokacje:%s %s",
                            currentCases,
                            difference,
                            newCases.stream().map(CoronaCase::getCounty).
                                    collect(Collectors.joining(", ")),
                            "https://bit.ly/33k664w"));


        }
        return Optional.empty();
    }

    private int countCasesNumber(List<CoronaCase> cases) {
        return cases.stream().mapToInt(CoronaCase::getCasesNumber).sum();
    }
}
