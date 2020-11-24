package com.github.niko247;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    @Mock
    private ReportMessageCreator reportMessageCreator;
    @Mock
    private SummaryResults currentCases;
    @Mock
    private SummaryResults oldCases;

    @Test
    public void fetchPageAndReportTest() throws IOException {
        //given
        var app = new App(Collections.singleton(push), casesFetcher, oldCasesManager, reportMessageCreator);
        when(casesFetcher.fetchCases()).thenReturn(currentCases);
        when(oldCasesManager.get()).thenReturn(Optional.of(oldCases));
        var report = "REPORT";
        when(reportMessageCreator.createIfNewCases(currentCases, oldCases)).
                thenReturn(Optional.of(report));

        //when
        app.fetchPageAndReport();

        //then
        verify(casesFetcher).fetchCases();
        verify(oldCasesManager).get();
        verify(oldCasesManager).save(currentCases);
        verify(push).send(report);
    }


}