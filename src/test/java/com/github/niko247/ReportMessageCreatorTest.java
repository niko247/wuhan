package com.github.niko247;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ReportMessageCreatorTest {
    @Test
    public void createNoDeathsTest() {
        //given
        var reportCreator = new ReportMessageCreator();
        var cases = new SummaryResults(3000, 5);
        var casesOld = new SummaryResults(1, 5);

        //when
        var result = reportCreator.createIfNewCases(cases, casesOld);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo("Całkowita liczba: 3 000 (+2 999).");

    }

    @Test
    public void createIfNewCasesTest() {
        //given
        var reportCreator = new ReportMessageCreator();
        var cases = new SummaryResults(3, 6);
        var casesOld = new SummaryResults(1, 2);

        //when
        var result = reportCreator.createIfNewCases(cases, casesOld);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo("Całkowita liczba: 3 (+2). Zmarłych: 6 (+4).");

    }

}