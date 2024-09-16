import Vector2 from "../utils/Vector2";

export interface ServerPlayer {
  username: string,
  location: Vector2
}

export abstract class AbstractEntity {
  public location: Vector2;
  public speed: Vector2;
  public size: Vector2;
  public color: string;

  constructor(
    location: Vector2,
    speed: Vector2,
    size: Vector2,
    color: string
  ) {
    this.location = location;
    this.speed = speed;
    this.size = size;
    this.color = color;
  }

  public abstract draw(ctx: CanvasRenderingContext2D): void;
}

export abstract class AbstractPlayer extends AbstractEntity {
  public username: string;

  constructor(
    username: string,
    location?: Vector2,
    speed?: Vector2,
    size?: Vector2,
    color?: string
  ) {
    super(
      location || new Vector2(0, 0),
      speed || new Vector2(1, 1),
      size || new Vector2(20, 20),
      color || "red"
    );
    this.username = username;
  }

  public abstract update(): void;

  public abstract convertToJSON(): string;
}

export class Player extends AbstractPlayer {
  constructor(
    username: string,
    location?: Vector2,
    speed?: Vector2,
    size?: Vector2,
    color?: string
  ) {
    super(username, location, speed, size, color);
  }

  public draw(ctx: CanvasRenderingContext2D) {
    ctx.fillStyle = this.color;
    ctx.fillRect(
      this.location.x - this.size.x / 2,
      this.location.y - this.size.y / 2,
      this.size.x,
      this.size.y
    );
  }

  public update() {
    // Do nothing for now
  }

  public convertToJSON() {
    return JSON.stringify({
      username: this.username,
      location: this.location,
    });
  }
}
export class CurrentPlayer extends Player {
  // Player movement direction. X -> [0], Y -> [1]
  public movementBuffer: number[] = [0, 0];

  private keysHeldDownMap: Map<string, boolean> = new Map();

  constructor(
    username: string,
    location?: Vector2,
    speed?: Vector2,
    size?: Vector2,
    color?: string
  ) {
    super(username, location, speed, size, color || "blue");

    this.keysHeldDownMap
      .set("a", false)
      .set("d", false)
      .set("w", false)
      .set("s", false);

    document.addEventListener("keydown", (event) => {
      switch (event.key) {
        case "a":
          this.movementBuffer[0] = -1;
          this.keysHeldDownMap.set("a", true);
          break;
        case "d":
          this.movementBuffer[0] = 1;
          this.keysHeldDownMap.set("d", true);
          break;
        case "w":
          this.movementBuffer[1] = -1;
          this.keysHeldDownMap.set("w", true);
          break;
        case "s":
          this.movementBuffer[1] = 1;
          this.keysHeldDownMap.set("s", true);
          break;
      }
    });

    document.addEventListener("keyup", (event) => {
      switch (event.key) {
        case "a":
          this.keysHeldDownMap.set("a", false);
          this.movementBuffer[0] = this.keysHeldDownMap.get("d") ? 1 : 0;
          break;
        case "d":
          this.keysHeldDownMap.set("d", false);
          this.movementBuffer[0] = this.keysHeldDownMap.get("a") ? -1 : 0;
          break;
        case "w":
          this.keysHeldDownMap.set("w", false);
          this.movementBuffer[1] = this.keysHeldDownMap.get("s") ? 1 : 0;
          break;
        case "s":
          this.keysHeldDownMap.set("s", false);
          this.movementBuffer[1] = this.keysHeldDownMap.get("w") ? -1 : 0;
          break;
      }
    });
  }

  public update() {
    this.location.x += this.movementBuffer[0] * this.speed.x;
    this.location.y += this.movementBuffer[1] * this.speed.y;
  }
}
