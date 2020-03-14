package com.github.niko247;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


@Ignore("Integration test")
public class PushManagerTest {
    //"PUSH_APP_KEY" "PUSH_APP_SECRET" env variables must be set
    @Test
    public void send() {
        //given
        var pushManager = new PushManager();

        //when
        var sent = pushManager.send("TEST MESSAGE");

        //then
        assertTrue(sent);
    }
}