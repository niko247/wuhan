package com.github.niko247;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ReportMessageCreatorTest {
    @Test
    public void createNoDeathssTest() {
        //given
        var reportCreator = new ReportMessageCreator();
        var coronaCase = createCase("4");
        var coronaCase2 = createCase("0");
        var coronaCase3 = createCase("");

        var currentCases = List.of(coronaCase, coronaCase2, coronaCase3);
        var oldCases = Collections.singletonList(coronaCase);

        //when
        var result = reportCreator.createIfNewCases(currentCases, oldCases);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo("Całkowita liczba: 3 (+2).");

    }

    @Test
    public void createIfNewCasesTest() {
        //given
        var reportCreator = new ReportMessageCreator();
        var coronaCase = createCase("2");
        var coronaCase2 = createCase("2");
        var coronaCase3 = createCase("2");

        var currentCases = List.of(coronaCase, coronaCase2, coronaCase3);
        var oldCases = Collections.singletonList(coronaCase);

        //when
        var result = reportCreator.createIfNewCases(currentCases, oldCases);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo("Całkowita liczba: 3 (+2). Zmarłych: 6 (+4).");

    }

    private CoronaCase createCase(String deaths) {
        var coronaCase = new CoronaCase();
        coronaCase.setVoivodeship("VOIVODESHIP");
        coronaCase.setCasesNumber("1");
        coronaCase.setDeathsNumber(deaths);
        return coronaCase;
    }
}