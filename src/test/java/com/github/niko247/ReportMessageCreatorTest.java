package com.github.niko247;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ReportMessageCreatorTest {

    @Test
    public void createIfNewCasesTest() {
        //given
        var reportCreator = new ReportMessageCreator();
        var coronaCase = createCase("COUNTY1");
        var coronaCase2 = createCase("COUNTY2");
        var coronaCase3 = createCase("COUNTY3");

        final var currentCases = List.of(coronaCase, coronaCase2, coronaCase3);
        final var oldCases = Collections.singletonList(coronaCase);

        //when
        var result = reportCreator.createIfNewCases(currentCases, oldCases);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo("Całkowita ilość 3 (+2). Lokacje:COUNTY2, COUNTY3");

    }

    private CoronaCase createCase(String county) {
        var coronaCase2 = new CoronaCase();
        coronaCase2.setVoivodeship("VOIVODESHIP");
        coronaCase2.setCounty(county);
        coronaCase2.setCasesNumber(1);
        return coronaCase2;
    }
}