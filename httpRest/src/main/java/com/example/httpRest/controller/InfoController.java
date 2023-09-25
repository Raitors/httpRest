package com.example.httpRest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.LongStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {
    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port:-1}")
    private int port;

    @GetMapping("port")
    public int getPort() {
        return port;
    }

    @GetMapping("/calculate")
    public void calculate() {
        long startTime = System.currentTimeMillis();
        Stream.iterate(1, a -> a + 1).
                limit(100_000_000).
                reduce(0, (a, b) -> a + b);

        long timeConsumed = System.currentTimeMillis() - startTime;
        logger.info("Time: " + timeConsumed);


        startTime = System.currentTimeMillis();
        LongStream.rangeClosed(1, 100_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);
        timeConsumed = System.currentTimeMillis() - startTime;
        logger.info("optimization: " + timeConsumed);

    }


}
