package com.github.niko247;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


@Ignore("Integration test")
public class TelegramSenderTest {
    //"TELEGRAM_TOKEN" env variables must be set
    @Test
    public void send() {
        //given
        var telegramSender = new TelegramSender();

        //when
        var sent = telegramSender.send("TEST MESSAGE");

        //then
        assertTrue(sent);
    }
}