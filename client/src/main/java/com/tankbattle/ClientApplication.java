package com.tankbattle;

import java.util.Scanner;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.tankbattle.views.GameWindow;

public class ClientApplication
{
    public static void main( String[] args )
    {
        GameWindow gameWindow = new GameWindow();
    }
}
