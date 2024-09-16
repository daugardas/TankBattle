import { Coordinate, CurrentPlayer, Player } from "./Player.ts";
import ConnectionSocket from "./ConnectionSocket.ts";
import {
  AllPlayersUpdateEndpoint,
  NewPlayerEndpoint,
  OtherPlayerDisconnectEndpoint,
  PlayerDisconnectEndpoint,
  ServerWebsocketEndpointURL
} from "./constants.ts";
import { IFrame, StompSubscription } from "@stomp/stompjs";
import { CanvasController } from "./CanvasController.ts";
import ConnectionStatusController from "./ConnectionStatusController.ts";

export default class Game {
  private connectionStatusController: ConnectionStatusController;
  private canvas: CanvasController;

  private otherPlayers: Player[] = [];
  private currPlayer: CurrentPlayer | undefined;

  private server: ConnectionSocket;
  private allPlayersSubscription: StompSubscription | undefined;
  private currPlayerSubscription: StompSubscription | undefined;
  private newPlayerJoinedSubscription: StompSubscription | undefined;
  private playerDisconnectedSubscription: StompSubscription | undefined;

  constructor() {
    this.server = new ConnectionSocket(
      ServerWebsocketEndpointURL,
      this.handleConnectedToServer.bind(this),
      this.handleDisconnectedFromServer.bind(this)
    );
    this.connectionStatusController = new ConnectionStatusController(
      this.server.connectToServer.bind(this.server),
      this.server.disconnectFromServer.bind(this.server)
    );
    this.canvas = new CanvasController();
    this.canvas.render = this.draw.bind(this);
  }

  private handleConnectedToServer(frame: IFrame) {
    this.connectionStatusController.refreshConnectionStatus(true);
    this.allPlayersSubscription = this.server.subscribe(
      AllPlayersUpdateEndpoint,
      (message) => {
        // console.log("received players", message.body);
        const receivedPlayers: {
          uuid: string;
          username: string;
          coord: { x: number; y: number };
        }[] = JSON.parse(message.body);

        this.otherPlayers = [];

        for (const player of receivedPlayers) {
          if (player.uuid !== this.server.getSessionId()) {
            this.otherPlayers.push(
              new Player(
                player.uuid,
                player.username,
                new Coordinate(player.coord.x, player.coord.y)
              )
            );
          }
        }
      }
    );

    this.currPlayerSubscription = this.server.subscribe(
      "/user/for-specific-client/player",
      (message) => {
        // console.log("received player: ", message.body);
        const receivedPlayer: {
          uuid: string;
          username: string;
          coord: { x: number; y: number };
        } = JSON.parse(message.body);

        this.server.setSessionId(receivedPlayer.uuid);

        if (receivedPlayer.uuid === this.server.getSessionId()) {

          this.currPlayer = new CurrentPlayer(
            receivedPlayer.uuid,
            receivedPlayer.username,
            new Coordinate(
              receivedPlayer.coord.x,
              receivedPlayer.coord.y
            )
          );
        }
      }
    );

    this.newPlayerJoinedSubscription = this.server.subscribe(
      NewPlayerEndpoint,
      (message) => {
        // console.log("new player joining: ", message.body);

        const newPlayer: {
          uuid: string;
          username: string;
          coord: { x: number; y: number };
        } = JSON.parse(message.body);

        if (newPlayer.uuid !== this.server.getSessionId()) {
          this.otherPlayers.push(
            new Player(
              newPlayer.uuid,
              newPlayer.username,
              new Coordinate(newPlayer.coord.x, newPlayer.coord.y)
            )
          );
        }
      }
    );

    this.playerDisconnectedSubscription = this.server.subscribe(OtherPlayerDisconnectEndpoint, (message)=>{
      const otherPlayerUuid = message.body as string;
      this.otherPlayers = this.otherPlayers.filter((p) => p.uuid !== otherPlayerUuid);


    })

    this.server.sendMessage({
      destination: "/from-client/create-new-player",
      body: new Player("", "hello").convertToJSON(),
    });
  }

  private handleDisconnectedFromServer(frame: IFrame) {
    this.connectionStatusController.refreshConnectionStatus(false);
    this.allPlayersSubscription?.unsubscribe();
    this.allPlayersSubscription = undefined;
    this.currPlayerSubscription?.unsubscribe();
    this.currPlayerSubscription = undefined;
    this.newPlayerJoinedSubscription?.unsubscribe();
    this.newPlayerJoinedSubscription = undefined;
    this.playerDisconnectedSubscription?.unsubscribe();
    this.playerDisconnectedSubscription = undefined;

    this.otherPlayers = [];
    this.currPlayer = undefined;
  }

  private sendCurrentPlayerToServer(){
    if(this.currPlayer){
      const sendingPlayerBody = this.currPlayer.convertToJSON();
      // console.log("sending current player to server:", sendingPlayerBody);
      this.server.sendMessage({
        destination: "/from-client/update-player",
        body: sendingPlayerBody
      });
    }
  }

  private drawPlayer(player: Player, ctx: CanvasRenderingContext2D) {
    ctx.fillStyle = player.color;
    ctx.fillRect(
      player.loc.x - player.size.x / 2,
      player.loc.y - player.size.y / 2,
      player.size.x,
      player.size.y
    );
  }

  private draw(ctx: CanvasRenderingContext2D) {
    ctx.reset();
    this.sendCurrentPlayerToServer();
    if (this.currPlayer) {
      this.drawPlayer(this.currPlayer, ctx);
      this.currPlayer.update();
    }

    for (let i = 0; i < this.otherPlayers.length; i++) {
      this.drawPlayer(this.otherPlayers[i], ctx);
      this.otherPlayers[i].update();
    }


  }

  public startGame() {
    this.canvas.startRender();
  }
}
