package com.github.niko247;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

@Log4j2
public class TelegramSender implements MessageSender {
    private static final String API_URL = "https://api.telegram.org/bot";

    @Override
    public boolean send(String message) {
        var appKey = System.getenv("TELEGRAM_TOKEN");
        var requestUrl = API_URL + appKey + "/" + "sendMessage";
        try {
            Jsoup.connect(requestUrl)
                    .data("chat_id", "@wuhanPL")
                    .data("text", message)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute();
            return true;
        } catch (
                IOException e) {
            log.error(e);
            return false;
        }

    }
}
