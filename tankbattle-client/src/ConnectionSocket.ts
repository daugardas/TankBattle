import {
  Client,
  Frame,
  frameCallbackType,
  messageCallbackType,
  StompHeaders,
  StompSubscription,
} from "@stomp/stompjs";
import {PlayerSessionIdEndpoint, ServerWebsocketEndpointURL} from "./constants.ts";

class ConnectionSocket {
  private client: Client;
  private stompSubscriptions: StompSubscription[] = [];
  private subscribeWhenConnected: {
    endpoint: string;
    callback: messageCallbackType;
    headers?: StompHeaders;
  }[] = [];
  private connected: boolean = false;
  private sessionId: string;
  private sessionIdSubscription: StompSubscription | null = null;

  constructor(
    url: string = ServerWebsocketEndpointURL,
    onConnectCallback?: frameCallbackType,
    subscriptions?: {
      endpoint: string;
      callback: messageCallbackType;
      headers?: StompHeaders;
    }[]
  ) {

    this.client = new Client({
      brokerURL: url,
      debug: function (str) {
        console.debug(str);
      },
      reconnectDelay: 5000,
    });

    this.client.onConnect = (frame) => {
      // console.log("connected to server:", frame);
      this.connected = true;

      this.sessionIdSubscription = this.client.subscribe(PlayerSessionIdEndpoint, (message) => {
        // console.log("sessionId", message.body);
        this.sessionId = message.body;

        if(this.sessionIdSubscription){
          this.sessionIdSubscription.unsubscribe();
          this.sessionIdSubscription = null;
        }
      })

      for (const sub of this.subscribeWhenConnected) {
        this.subscribe(sub.endpoint, sub.callback, sub.headers);
      }

      if (onConnectCallback !== undefined) {
        onConnectCallback(frame);
      }
    };

    this.client.onStompError = this.onStompError;
    this.client.onWebSocketError = this.onWebSocketError;

    if (subscriptions !== undefined)
      this.subscribeWhenConnected = subscriptions;
  }

  private onStompError(frame: Frame) {
    // errors from the server will be sent here
    console.log("Server reported error:", frame.headers["message"]);
    console.log("Details:", frame.body);
  }

  private onWebSocketError(frame: Frame) {
    console.log("Websocket error:", frame);
  }

  public connectToServer() {
    console.log("Trying to connect to server");
    this.client.activate();
  }

  public async disconnectFromServer() {
    console.log("Disconnecting from server");
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

  public sendMessage(
    destination: string,
    headers?: StompHeaders,
    body?: string,
    binaryBody?: Uint8Array,
    skipContentLengthHeader?: boolean
  ) {
    this.client.publish({
      destination,
      headers,
      body,
      binaryBody,
      skipContentLengthHeader,
    });
  }
}

export default ConnectionSocket;
