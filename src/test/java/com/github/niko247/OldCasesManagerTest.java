package com.github.niko247;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class OldCasesManagerTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testGetNoData() throws IOException {
        //given
        var oldCasesManager = new OldCasesManager("not_existing");
        //when
        var oldCases = oldCasesManager.get();
        //then
        assertThat(oldCases).isEmpty();

    }

    @Test
    public void testSave() throws IOException {
        //given
        var oldCasesManager = new OldCasesManager(folder.newFile().getPath());
        var coronaCase = new SummaryResults(36, 21);

        //when
        oldCasesManager.save(coronaCase);
        final var oldCases = oldCasesManager.get();

        //then
        assertThat(oldCases).isNotEmpty();
        assertThat(oldCases.get().totalDeaths()).isEqualTo(21);
        assertThat(oldCases.get().totalCases()).isEqualTo(36);

    }
}