import {
  Client,
  frameCallbackType,
  IFrame,
  IPublishParams,
  messageCallbackType,
  StompHeaders,
  StompSubscription,
} from "@stomp/stompjs";
import {
  ServerWebsocketEndpointURL,
} from "../utils/constants.ts";

export default class ServerController {
  private client: Client;
  private onConnectCallback: frameCallbackType | undefined;
  private onDisconnectCallback: frameCallbackType | undefined;

  constructor(
    url: string = ServerWebsocketEndpointURL,
    onConnectCallback?: frameCallbackType,
    onDisconnectCallback?: frameCallbackType
  ) {
    this.client = new Client({
      brokerURL: url,
      connectHeaders: {
        // Su STOMP connect frame'u issiunciam username'a
        login: "",
      },
      //debug: function(str) {
      //  console.debug(str);
      //},
    });

    this.onConnect = this.onConnect.bind(this);
    this.onDisconnect = this.onDisconnect.bind(this);
    this.onStompError = this.onStompError.bind(this);
    this.onWebSocketError = this.onWebSocketError.bind(this);

    this.client.onConnect = this.onConnect;
    this.client.onStompError = this.onStompError;
    this.client.onWebSocketError = this.onWebSocketError;
    this.client.onDisconnect = this.onDisconnect;
    this.onConnectCallback = onConnectCallback;
    this.onDisconnectCallback = onDisconnectCallback;
  }

  private onDisconnect(frame: IFrame) {
    if (this.onDisconnectCallback) {
      this.onDisconnectCallback(frame);
    }
  }

  private onConnect(frame: IFrame) {
    if (this.onConnectCallback) {
      this.onConnectCallback(frame);
    }
  }

  private onStompError(frame: IFrame) {
    // errors from the server will be sent here
    console.log("Server reported error:", frame.headers["message"]);
    console.log("Details:", frame.body);
  }

  private onWebSocketError(frame: IFrame) {
    console.log("Websocket error:", frame);
  }

  public connect() {
    const inputFieldValue = (document.getElementById("username") as HTMLInputElement).value;
    if (!inputFieldValue) {
      const randomNumber = Math.floor(Math.random() * (100 - 1 + 1) + 1);
      window.username = "Guest" + randomNumber;
    } else {
      window.username = inputFieldValue;
    }

    this.client.connectHeaders = {
      login: window.username,
    }

    this.client.activate();
  }

  public async disconnect() {
    await this.client.deactivate();
  }

  public subscribe(
    endpoint: string,
    callback: messageCallbackType,
    headers?: StompHeaders
  ): StompSubscription {
    const subscription = this.client.subscribe(endpoint, callback, headers);
    return subscription;
  }

  public sendMessage(params: IPublishParams) {
    this.client.publish(params);
  }
}
