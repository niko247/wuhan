package com.github.niko247;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

@Log4j2
public class PushManager implements MessageSender {
    public boolean send(String message) {
        try {
            var appKey = System.getenv("PUSH_APP_KEY");
            var appSecret = System.getenv("PUSH_APP_SECRET");

            Jsoup.connect("https://api.pushed.co/1/push")
                    .data("app_key", appKey)
                    .data("app_secret", appSecret)
                    .data("target_type", "app")
                    .data("content", message).ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute();
            return true;
        } catch (IOException e) {
            log.error(e);
            return false;
        }
    }
}