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
  PlayerSessionIdEndpoint,
  ServerWebsocketEndpointURL,
} from "./constants.ts";

class ConnectionSocket {
  private client: Client;
  private stompSubscriptions: StompSubscription[] = [];
  private connected: boolean = false;
  private sessionId: string | undefined;
  private sessionIdSubscription: StompSubscription | undefined;
  private onConnectCallback: frameCallbackType | undefined;
  private onDisconnectCallback: frameCallbackType | undefined;

  constructor(
    url: string = ServerWebsocketEndpointURL,
    onConnectCallback?: frameCallbackType,
    onDisconnectCallback?: frameCallbackType
  ) {
    this.client = new Client({
      brokerURL: url,
      debug: function (str) {
        console.debug(str);
      },
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
    // this.connected = false;

    if (this.onDisconnectCallback) {
      this.onDisconnectCallback(frame);
    }
  }

  private onConnect(frame: IFrame) {
    // this.connected = true;

    console.log("server", this);

    this.sessionIdSubscription = this.client.subscribe(
      PlayerSessionIdEndpoint,
      (message) => {
        this.sessionId = message.body as string;

        if (this.sessionIdSubscription) {
          this.sessionIdSubscription.unsubscribe();
          this.sessionIdSubscription = undefined;
        }
      }
    );

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

  public connectToServer() {
    console.log(this);
    this.client.activate();
  }

  public async disconnectFromServer() {
    await this.client.deactivate();
    this.connected = false;
  }

  public subscribe(
    endpoint: string,
    callback: messageCallbackType,
    headers?: StompHeaders
  ): StompSubscription {
    const subscription = this.client.subscribe(endpoint, callback, headers);
    this.stompSubscriptions.push(subscription);
    return subscription;
  }

  public sendMessage(params: IPublishParams) {
    this.client.publish(params);
  }

  public getConnectionStatus() {
    return this.connected;
  }

  public getSessionId() {
    return this.sessionId;
  }

  public setSessionId(sessionId: string) {
    this.sessionId = sessionId;
  }

  // public sendMessage(
  //   destination: string,
  //   body?: string,
  //   headers?: StompHeaders,
  //   binaryBody?: Uint8Array,
  //   skipContentLengthHeader?: boolean
  // ) {
  //   this.client.publish({
  //     destination,
  //     headers,
  //     body,
  //     binaryBody,
  //     skipContentLengthHeader,
  //   });
  // }
}

export default ConnectionSocket;
