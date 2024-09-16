import { CurrentPlayer, Player, ServerPlayer } from "../models/Player.ts";
import Vector2 from "../utils/Vector2.ts";
import ServerController from "../controllers/ServerController.ts";
import {
  ServerPlayersEndpoint,
  ServerWebsocketEndpointURL,
  UpdatePlayerEndpoint
} from "../utils/constants.ts";
import { StompSubscription } from "@stomp/stompjs";
import { CanvasController } from "../controllers/CanvasController.ts";
import ConnectionStatusController from "../controllers/ConnectionStatusController.ts";

export default class GameController {
  private connectionStatusController: ConnectionStatusController;
  private canvas: CanvasController;

  private otherPlayers: Player[] = [];
  private currPlayer: CurrentPlayer | undefined;

  private server: ServerController;
  private allPlayersSubscription: StompSubscription | undefined;

  constructor() {
    this.server = new ServerController(
      ServerWebsocketEndpointURL,
      this.handleConnectedToServer.bind(this),
      this.handleDisconnectedFromServer.bind(this)
    );
    this.connectionStatusController = new ConnectionStatusController(
      this.server.connect.bind(this.server),
      this.server.disconnect.bind(this.server)
    );
    this.canvas = new CanvasController();
    this.canvas.render = this.draw.bind(this);
  }

  private handleConnectedToServer() {
    this.connectionStatusController.refreshConnectionStatus(true);
    const username = window.username;

    this.allPlayersSubscription = this.server.subscribe(
      ServerPlayersEndpoint,
      (message) => {
        const receivedPlayers: ServerPlayer[] = JSON.parse(message.body);

        this.otherPlayers = [];

        for (const player of receivedPlayers) {
          if (player.username !== username) {
            this.otherPlayers.push(
              new Player(
                player.username,
                new Vector2(player.location.x, player.location.y)
              )
            );
          }
          else if (!this.currPlayer) {
            this.currPlayer = new CurrentPlayer(username, player.location)
            console.log(this.currPlayer);
          }
        }
      }
    );
  }

  private sendCurrentPlayerToServer() {
    if (this.currPlayer) {
      const sendingPlayerBody = this.currPlayer.convertToJSON();
      // console.log("sending current player to server:", sendingPlayerBody);
      this.server.sendMessage({
        destination: UpdatePlayerEndpoint,
        body: sendingPlayerBody
      });
    }
  }

  private handleDisconnectedFromServer() {
    this.connectionStatusController.refreshConnectionStatus(false);
    this.allPlayersSubscription?.unsubscribe();
    this.allPlayersSubscription = undefined;

    this.otherPlayers = [];
    this.currPlayer = undefined;
  }

  private draw(ctx: CanvasRenderingContext2D) {
    ctx.reset();
    this.sendCurrentPlayerToServer();
    if (this.currPlayer) {
      this.currPlayer.draw(ctx)
      this.currPlayer.update();
    }

    for (let i = 0; i < this.otherPlayers.length; i++) {
      this.otherPlayers[i].draw(ctx)
      this.otherPlayers[i].update();
    }
  }

  public startGame() {
    this.canvas.startRender();
  }
}
