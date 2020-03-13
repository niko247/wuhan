package com.github.niko247;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class OldCasesManagerTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testGetNoData() throws IOException {
        //given
        var oldCasesManager = new OldCasesManager("not_existing");
        //when
        final var oldCases = oldCasesManager.get();
        //then
        assertThat(oldCases).isEmpty();

    }

    @Test
    public void testSave() throws IOException {
        //given
        var oldCasesManager = new OldCasesManager(folder.newFile().getPath());
        final var coronaCase = new CoronaCase();

        //when
        oldCasesManager.save(Collections.singletonList(coronaCase));
        final var oldCases = oldCasesManager.get();

        //then
        assertThat(oldCases).isNotEmpty();
        assertThat(oldCases.get()).hasSize(1);
    }
}