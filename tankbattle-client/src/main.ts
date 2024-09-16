import "./style.css";
import ConnectionSocket from "./ConnectionSocket.ts";
import {
  ForCurrentClientEndpoint, NewPlayerEndpoint,
  PlayerConnectEndpoint,
  PlayerDisconnectEndpoint,
  ServerWebsocketEndpointURL,
} from "./constants.ts";
import { IMessage } from "@stomp/stompjs";
import Game from "./Game.ts";
import Player from "./Player.ts";

let connectedToServer = false;
let connectButton = document.getElementById("connect-button");
let disconnectButton = document.getElementById("disconnect-button");
let canvasElement =
  (document.getElementById("game-canvas") as HTMLCanvasElement) || null;
let connectedToServerStatusElement = document.getElementById(
  "connected-to-server"
);

if (!connectButton || !disconnectButton) {
  throw new Error("connect button elements are missing");
}

if (!connectedToServerStatusElement) {
  throw new Error("connect to server status text element is missing");
}

if (!canvasElement) {
  let newCanvasEl = new HTMLCanvasElement();
  newCanvasEl.id = "game-canvas";
  document.body.appendChild(newCanvasEl);
  canvasElement = newCanvasEl;
}

connectButton.addEventListener("click", connectToServer);
disconnectButton.addEventListener("click", disconnectFromServer);

refreshConnectionStatus();

function refreshConnectionStatus() {
  // @ts-ignore
  connectButton.className = `${connectedToServer ? "hidden" : ""}`;
  // @ts-ignore
  disconnectButton.className = `${connectedToServer ? "" : "hidden"}`;
  // @ts-ignore
  connectedToServerStatusElement.innerText = `${connectedToServer}`;
}

// function receiveGreeting(message: IMessage) {
//   const greeting: Greeting = JSON.parse(message.body) as Greeting;
//   console.log("received greeting: ", greeting);
// }

function connectToServer() {
  socket.connectToServer();
}

async function disconnectFromServer() {
  const player: {
    id: number;
    username: string;
    coord: {
      x: number;
      y: number;
    }
  } = {
    id: 0, username: "hello world", coord: { x: 0, y: 0 }
  }
  socket.sendMessage(PlayerDisconnectEndpoint, undefined, JSON.stringify(player));
  await socket.disconnectFromServer();
  connectedToServer = false;
  refreshConnectionStatus();
}

function successfullyConnected() {

  connectedToServer = true;
  refreshConnectionStatus();

  const player: {
    id: number;
    username: string;
    coord: {
      x: number;
      y: number;
    }
  } = {
    id: 0, username: "hello world", coord: { x: 0, y: 0 }
  }

  // socket.sendMessage(PlayerConnectEndpoint, undefined, JSON.stringify(player));
  // socket.subscribe(`${ForCurrentClientEndpoint}/player`, (message) => {
  //   console.log("for current client player: ", message.body);
  // })

  socket.subscribe("/from-client/for-all-clients/players", (message) => {
    console.log("received players", message.body);
  })

  socket.subscribe("/user/for-specific-client/player", (message) => {
    console.log("received player: ", message.body);
  })

  socket.subscribe(NewPlayerEndpoint, (message) => {
    console.log("new player joining: ", message.body);
  })

  socket.sendMessage("/from-client/create-new-player", undefined, JSON.stringify(player));
}

let socket: ConnectionSocket = new ConnectionSocket(
  ServerWebsocketEndpointURL,
  (frame) => {
    successfullyConnected();
  }
);

let game: Game = new Game(canvasElement);

game.start();
