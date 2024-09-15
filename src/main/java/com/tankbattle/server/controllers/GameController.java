package com.tankbattle.server.controllers;

import com.tankbattle.server.models.Player;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class GameController {

    private ArrayList<Player> players = new ArrayList();

    @MessageMapping("/disconnect")
    public void disconnectPlayer(Player player) throws  Exception {
        this.players.remove(player.getId());
    }


    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getId());
        //Player newPlayer = new Player(session.getId(), players.size(), String.format("user%d", players.size()));

    }

    @MessageMapping("/connect") // is client i cia
    public void connectPlayer(Player player) throws  Exception {
        this.players.add(player);
        for (int i = 0; i < this.players.size(); i++) {
            System.out.println(this.players.get(i).toString());
        }
    }
}
