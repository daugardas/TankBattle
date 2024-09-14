export interface IGreeting {
  content: string;
}

class Greeting implements IGreeting {
  public content: string;

  constructor(content: string) {
    this.content = content;
  }
}

export default Greeting;
