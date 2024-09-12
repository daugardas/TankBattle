package com.tankbattle.server.controllers;

import com.tankbattle.server.models.Greeting;
import com.tankbattle.server.models.Guest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    // client sends messages to /app/hello-world
    @MessageMapping("/hello-world")
    // client listens on this path for messages,
    // so the server sends one to everyone who listens
    @SendTo("/from-server/hello")
    public Greeting greeting(Guest guest) throws Exception {
        System.out.println("Received guest object: " + guest.toString());
        Greeting greeting = new Greeting("Hello, " + guest.getName() + "!");
        return greeting;
    }
}
