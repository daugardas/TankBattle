export default class ConnectionStatusController {
  private connectButtonEl = document.getElementById(
    "connect-button"
  ) as HTMLButtonElement;
  private disconnectButtonEl = document.getElementById(
    "disconnect-button"
  ) as HTMLButtonElement;
  private statusDivElement = document.getElementById(
    "connected-to-server"
  ) as HTMLSpanElement;

  constructor(connectToServer: () => void, disconnectFromServer: () => void) {
    this.connectButtonEl.addEventListener("click", connectToServer);
    this.disconnectButtonEl.addEventListener("click", disconnectFromServer);

    this.refreshConnectionStatus(false);
  }

  public refreshConnectionStatus(connectionStatus: boolean) {
    this.connectButtonEl.className = `${connectionStatus ? "hidden" : ""}`;
    this.disconnectButtonEl.className = `${connectionStatus ? "" : "hidden"}`;
    this.statusDivElement.innerText = `${connectionStatus}`;
  }
}
