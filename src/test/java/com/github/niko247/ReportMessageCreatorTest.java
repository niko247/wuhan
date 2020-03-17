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
        var coronaCase = createCase("COUNTY1", "4");
        var coronaCase2 = createCase("COUNTY2", "0");
        var coronaCase3 = createCase("COUNTY3", "");

        var currentCases = List.of(coronaCase, coronaCase2, coronaCase3);
        var oldCases = Collections.singletonList(coronaCase);

        //when
        var result = reportCreator.createIfNewCases(currentCases, oldCases);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo("Całkowita ilość: 3 (+2). https://bit.ly/33k664w");

    }

    @Test
    public void createIfNewCasesTest() {
        //given
        var reportCreator = new ReportMessageCreator();
        var coronaCase = createCase("COUNTY1", "2");
        var coronaCase2 = createCase("COUNTY2", "2");
        var coronaCase3 = createCase("COUNTY3", "2");

        var currentCases = List.of(coronaCase, coronaCase2, coronaCase3);
        var oldCases = Collections.singletonList(coronaCase);

        //when
        var result = reportCreator.createIfNewCases(currentCases, oldCases);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo("Całkowita ilość: 3 (+2). Zmarłych: 6 (+4). https://bit.ly/33k664w");

    }

    private CoronaCase createCase(String county, String deaths) {
        var coronaCase = new CoronaCase();
        coronaCase.setVoivodeship("VOIVODESHIP");
        coronaCase.setCounty(county);
        coronaCase.setCasesNumber(1);
        coronaCase.setDeathsNumber(deaths);
        return coronaCase;
    }
}