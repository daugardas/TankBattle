export const ServerWebsocketEndpointURL = "ws://localhost:8080/game";
export const ForAllClientsEndpoint = "/for-all-clients"
export const ForCurrentClientEndpoint = "/from-client";
export const PlayerSyncFromEndpoint = "/from-client";
export const PlayerSessionIdEndpoint = `${PlayerSyncFromEndpoint}/session-id`;
export const PlayerConnectEndpoint = `${PlayerSyncFromEndpoint}/connect`;
export const PlayerDisconnectEndpoint = `${PlayerSyncFromEndpoint}/disconnect`;
export const NewPlayerEndpoint = `${ForAllClientsEndpoint}/new-player`;