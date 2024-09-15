const socketUrl = "ws://localhost:8080/game";

const stompClient = new StompJs.Client({
  brokerURL: socketUrl, // clients connect to this websocket,
  debug(str) {
    console.debug(str);
  },
});

stompClient.onConnect = (frame) => {
  setConnected(true);
  console.log("Connected: " + frame);
  stompClient.subscribe("/from-server/hello", (greeting) => {
    console.log("received greeting: ", JSON.parse(greeting.body));
    showGreeting(JSON.parse(greeting.body).content);
  });
};

stompClient.onWebSocketError = (error) => {
  console.error("Error with websocket", error);
};

stompClient.onStompError = (frame) => {
  console.error("Broker reported error: " + frame.headers["message"]);
  console.error("Additional details: " + frame.body);
};

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#greetings").html("");
}

function connect() {
  console.log("trying to connect to '" + socketUrl + "'");
  stompClient.activate();
}

function disconnect() {
  stompClient.deactivate();
  setConnected(false);
  console.log("Disconnected");
}

function sendName() {
  stompClient.publish({
    destination: "/app/hello-world",
    body: JSON.stringify({ name: $("#name").val() }),
  });
}

function showGreeting(message) {
  $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
  $("form").on("submit", (e) => e.preventDefault());
  $("#connect").click(() => connect());
  $("#disconnect").click(() => disconnect());
  $("#send").click(() => sendName());
});
