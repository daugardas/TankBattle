export class Coordinate {
  public x: number;
  public y: number;

  constructor(x: number = 0, y: number = 0) {
    this.x = x;
    this.y = y;
  }
}

export abstract class AbstractEntity {
  public loc: Coordinate;
  public speed: Coordinate;
  public size: Coordinate;
  public color: string;

  constructor(
    loc: Coordinate,
    speed: Coordinate,
    size: Coordinate,
    color: string
  ) {
    this.loc = loc;
    this.speed = speed;
    this.size = size;
    this.color = color;
  }

  public abstract draw(ctx: CanvasRenderingContext2D): void;
}

export abstract class AbstractPlayer extends AbstractEntity {
  public uuid: string;
  public username: string;

  constructor(
    uuid: string,
    username: string,
    loc?: Coordinate,
    speed?: Coordinate,
    size?: Coordinate,
    color?: string
  ) {
    super(
      loc || new Coordinate(0, 0),
      speed || new Coordinate(1, 1),
      size || new Coordinate(20, 20),
      color || "red"
    );
    this.uuid = uuid;
    this.username = username;
  }

  public abstract update(): void;

  public abstract convertToJSON(): string;
}

export class Player extends AbstractPlayer {
  constructor(
    uuid: string,
    username: string,
    loc?: Coordinate,
    speed?: Coordinate,
    size?: Coordinate,
    color?: string
  ) {
    super(uuid, username, loc, speed, size, color);
  }

  public draw(ctx: CanvasRenderingContext2D) {
    ctx.fillStyle = this.color;
    ctx.fillRect(
      this.loc.x - this.size.x / 2,
      this.loc.y - this.size.y / 2,
      this.size.x,
      this.size.y
    );
  }

  public update() {
    // Do nothing for now
  }

  public convertToJSON() {
    return JSON.stringify({
      uuid: this.uuid,
      username: this.username,
      coord: this.loc,
    });
  }
}
export class CurrentPlayer extends Player {
  // Player movement direction. X -> [0], Y -> [1]
  public movementBuffer: number[] = [0, 0];

  private keysHeldDownMap: Map<string, boolean> = new Map();

  constructor(
    uuid: string,
    username: string,
    loc?: Coordinate,
    speed?: Coordinate,
    size?: Coordinate,
    color?: string
  ) {
    super(uuid, username, loc, speed, size, color || "blue");

    console.log("speed", this.speed);

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
    this.loc.x += this.movementBuffer[0] * this.speed.x;
    this.loc.y += this.movementBuffer[1] * this.speed.y;
  }
}
