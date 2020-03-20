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
        var coronaCases = fetcher.fetchCases();

        //then
        assertThat(coronaCases).isNotEmpty();
        var firstCase = coronaCases.get(0);
        assertThat(coronaCases.stream().noneMatch(c -> "Cała Polska".equals(c.getVoivodeship()))).isTrue();
        assertThat(firstCase.getVoivodeship()).isNotBlank();
        assertThat(firstCase.getCasesNumber()).isPositive();
    }
}