export const ServerWebsocketEndpointURL = `ws://${import.meta.env.VITE_SERVER_IP}:8080/game`;
export const ServerEndpoint = "/server";
export const ClientEndpoint = "/client";

export const UpdatePlayerEndpoint = `${ClientEndpoint}/update-player`
export const ServerPlayersEndpoint = `${ServerEndpoint}/players`;
