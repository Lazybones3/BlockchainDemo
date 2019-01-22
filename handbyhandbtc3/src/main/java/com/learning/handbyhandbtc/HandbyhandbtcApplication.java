package com.learning.handbyhandbtc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

@SpringBootApplication
public class HandbyhandbtcApplication {
    public static String port;

    public static void main(String[] args) {

//        SpringApplication.run(HandbyhandbtcApplication.class, args);
        Scanner scanner = new Scanner(System.in);
        port = scanner.nextLine();
        new SpringApplicationBuilder(HandbyhandbtcApplication.class).properties("server.port="+port).run(args);
    }

}

