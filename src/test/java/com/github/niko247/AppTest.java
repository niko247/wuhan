package com.github.niko247;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppTest {
    @Mock
    private PushManager push;
    @Mock
    private CoronaResultsFetcher casesFetcher;
    @Mock
    private OldCasesManager oldCasesManager;

    @Test
    public void fetchPageAndReportTest() throws IOException {
        //given
        var app = new App(push, casesFetcher, oldCasesManager);

        var coronaCase = createCase("COUNTY1");
        var coronaCase2 = createCase("COUNTY2");
        var coronaCase3 = createCase("COUNTY3");

        when(casesFetcher.fetchCases()).thenReturn(List.of(coronaCase, coronaCase2, coronaCase3));
        when(oldCasesManager.get()).thenReturn(Optional.of(Collections.singletonList(coronaCase)));

        //when
        app.fetchPageAndReport();

        //then
        verify(casesFetcher).fetchCases();
        verify(oldCasesManager).get();
        verify(oldCasesManager).save(anyList());
        verify(push).send("Nowe przypadki, całkowita ilość 3 (+2). Lokacje:COUNTY2, COUNTY3");
    }

    private CoronaCase createCase(String county) {
        var coronaCase2 = new CoronaCase();
        coronaCase2.setVoivodeship("VOIVODESHIP");
        coronaCase2.setCounty(county);
        coronaCase2.setCasesNumber(1);
        return coronaCase2;
    }
}