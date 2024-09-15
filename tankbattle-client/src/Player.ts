import Tank from "./models/Tank.ts";

export default class Player {
  // private


  constructor() {
    this.tank = new Tank();

    document.addEventListener("keydown", (event) => {
      const keyName = event.key;

      console.log(keyName);

      if (event.key === "a") {
        // this.tank.setSpeed(-1, 0);
        // if(this.keysDown.)
        this.buffer[0] = -1;
      }

      if (event.key === "w") {
        this.buffer[1] = -1;
      }

      if (event.key === "d") {
        this.buffer[0] = 1;
      }

      if (event.key === "s") {
        this.buffer[1] = 1;
      }


      this.tank.setSpeed(this.buffer[0], this.buffer[1]);
    })

    document.addEventListener("keyup", (event) => {
      if (event.key === "a") {
        // this.tank.setSpeed(-1, 0);
        // if(this.keysDown.)
        this.buffer[0] = 0;
      }

      if (event.key === "w") {
        this.buffer[1] = 0;
      }

      if (event.key === "d") {
        this.buffer[0] = 0;
      }

      if (event.key === "s") {
        this.buffer[1] = 0;
      }

      this.tank.setSpeed(this.buffer[0], this.buffer[1]);
    })
  }

  public getTank() {
    return this.tank;
  }

  public updateTankCoordinate(x: number, y: number) {
    this.tank.setCoord(x, y);
  }
}