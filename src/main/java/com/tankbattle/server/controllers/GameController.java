package com.tankbattle.server.controllers;

import com.tankbattle.server.models.Greeting;
import com.tankbattle.server.models.Guest;
import com.tankbattle.server.models.Player;
import jakarta.websocket.OnClose;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;


@Controller
public class GameController {

    private ArrayList<Player> players = new ArrayList();

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

    @OnClose

    @MessageMapping("/connect") // is client i cia
//    @SendTo("/syncfrom/connect") // clientas subscribina cia
    public void connectPlayer(Player player) throws  Exception {
        System.out.println("player object: " + player.toString());
        this.players.add(player);
        for (int i = 0; i < this.players.size(); i++) {
            System.out.println(this.players.get(i).toString());
        }
    }
}
