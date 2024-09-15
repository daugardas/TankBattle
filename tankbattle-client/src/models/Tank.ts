export default class Tank {
  private speedX: number = 0;
  private speedY: number = 0;
  private coordinate: Coordinate;
 public size: number = 20;
  constructor(initX: number = 0, initY: number = 0) {
    this.coordinate = {x: initX, y: initY};
  }

  public getCoord() {
    return this.coordinate;
  }

  public setCoord(x: number, y: number) {
    this.coordinate = {x, y};
  }

  public setSpeed(x: number, y: number) {
    this.speedX = x;
    this.speedY = y;
  }



  public update() {
    this.coordinate = {x: this.coordinate.x + this.speedX, y: this.coordinate.y + this.speedY}
  }
}

export interface Coordinate {
  x: number;
  y: number;
}