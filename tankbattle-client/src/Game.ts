import Player from "./Player.ts";

export default class Game {
  private canvasEl: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private otherPlayers: Player[] = [];
  private currPlayer: Player;
  private drawingSpeed: number = 1;
  private windowWidth: number;
  private windowHeight: number;

  constructor(canvasEl: HTMLCanvasElement) {
    this.canvasEl = canvasEl;
    const ctx = canvasEl.getContext("2d");
    if (ctx){
      this.ctx = ctx;
    }
    this.windowWidth = canvasEl.width;
    this.windowHeight = canvasEl.height;

    this.currPlayer = new Player();
  }

  private draw(){
    // console.log("player tank coord:", this.currPlayer.getTank().getCoord());
    this.ctx.reset();

    const t = this.currPlayer.getTank();
    this.ctx.fillStyle = `#000000`;
    const tx = t.getCoord().x;
    const ty = t.getCoord().y;
    this.ctx.fillRect(tx - t.size / 2, ty - t.size / 2, t.size, t.size);

    t.update();
  }

  public start() {
    let intervalId = setInterval(() => {
      this.draw();
    }, 16)
  }
}
