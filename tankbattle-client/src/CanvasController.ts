export class CanvasController {
  private windowWidth: number;
  private windowHeight: number;
  private canvasEl: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private renderIntervalId: number | undefined;
  private refreshRate: number = 60;

  public render: ((ctx: CanvasRenderingContext2D) => void) | undefined = undefined;

  constructor() {
    this.canvasEl = (document.getElementById("game-canvas") as HTMLCanvasElement);
    this.ctx = this.canvasEl.getContext("2d") as CanvasRenderingContext2D;
    this.windowWidth = this.canvasEl.width;
    this.windowHeight = this.canvasEl.height;
  }

  public startRender() {
    this.renderIntervalId = setInterval(() => {
      if (this.render === undefined) {
        console.error("Render method is undefined. Cannot start rendering.");
        this.stopRender();
        return;
      }
      this.render(this.ctx);
    }, 1000 / this.refreshRate);
  }

  public stopRender() {
    clearInterval(this.renderIntervalId);
  }

  public getWidth() {
    return this.windowWidth;
  }

  public getHeight() {
    return this.windowHeight;
  }
}