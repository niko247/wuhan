package com.github.niko247;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CoronaResultsFetcherTest {

    @Test
    public void testFetchCases() throws IOException {
        //given
        var fetcher = new CoronaResultsFetcher();

        //when
        final var coronaCases = fetcher.fetchCases();

        //then
        assertThat(coronaCases).isNotEmpty();
        final var firstCase = coronaCases.get(0);
        assertThat(firstCase.getVoivodeship()).isNotBlank();
        assertThat(firstCase.getCounty()).isNotBlank();
        assertThat(firstCase.getCasesNumber()).isPositive();
    }
}